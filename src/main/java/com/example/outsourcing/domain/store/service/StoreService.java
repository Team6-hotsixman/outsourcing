package com.example.outsourcing.domain.store.service;


import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.common.exception.NotFoundStoreException;
import com.example.outsourcing.domain.common.service.KaKaoMapApiService;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.service.MenuService;
import com.example.outsourcing.domain.common.service.SearchKeywordRankingService;
import com.example.outsourcing.domain.order.enums.OrderStatus;
import com.example.outsourcing.domain.store.dto.response.*;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.OrderBy;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.UserAddress;
import com.example.outsourcing.domain.user.enums.AddressStatus;
import com.example.outsourcing.domain.user.repository.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    private final MenuService menuService;

    private final KaKaoMapApiService kaKaoMapApiService;
    private final UserAddressRepository userAddressRepository;
    private final SearchKeywordRankingService searchKeywordRankingService;

    /* hyen ho start */
    public StoreResponseDto getStoreAndMenu(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(NotFoundStoreException::new);

        List<Menu> menus = menuService.getMenus(storeId);

        return StoreResponseDto.of(store, menus);
    }

    public Store getStore(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(NotFoundStoreException::new);
    }
    /* hyen ho end */

    @Transactional(readOnly = true)
    public List<StoreResponseDto> searchStore(long userId, String searchKeyword, Pageable page, OrderBy orderBy) {
        //사용자의 기본 배송지를 가져온다
        UserAddress address = userAddressRepository.findByUserIdAndAddressStatus(userId, AddressStatus.DEFAULT)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_DEFAULT_ADDRESS));

        //사용자의 기본 배송지 좌표
        Point point = kaKaoMapApiService.getPoint(address.getAddress());


        if(searchKeyword == null || searchKeyword.isEmpty()){
            //거리만 조건으로 쿼리 수행
            return storeRepository.findStoresByArea(point, page, orderBy, OrderStatus.COMPLETED, StoreStatus.OPEN);
        }

        List<StoreResponseForNativeQuery> stores = List.of();

        if(orderBy != OrderBy.RATE) {
            stores = storeRepository.searchOrderByDistance(point, searchKeyword, OrderStatus.COMPLETED.name(), StoreStatus.OPEN.name(), page.getOffset(), page.getPageSize());
        } else {
            stores = storeRepository.searchOrderByRate(point, searchKeyword, OrderStatus.COMPLETED.name(), StoreStatus.OPEN.name(), page.getOffset(), page.getPageSize());
        }

        if(!stores.isEmpty()){
            searchKeywordRankingService.increaseCount(searchKeyword);
        }

        return stores.stream().map(StoreResponseDto::of).toList();
    }

    @Transactional(readOnly = true)
    public List<StoreResponseDto> searchStoreByCategory(long userId, long categoryId, Pageable page, OrderBy orderBy) {
        //사용자의 기본 배송지를 가져온다
        UserAddress address = userAddressRepository.findByUserIdAndAddressStatus(userId, AddressStatus.DEFAULT)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_DEFAULT_ADDRESS));

        Point point = kaKaoMapApiService.getPoint(address.getAddress());

        return storeRepository.findStoresByCategory(point, categoryId, page, orderBy, OrderStatus.COMPLETED, StoreStatus.OPEN);
    }

    @Transactional(readOnly = true)
    public List<StoreResponseDto> findNearStore(int top, Point location) {
        List<StoreResponseDto> topNearStores = storeRepository.findTopNearStores(top, location, OrderStatus.COMPLETED, StoreStatus.OPEN);
        return topNearStores;
    }

    @Transactional(readOnly = true)
    public List<StoreResponseDto> findTopSellerStores(int top, Point location) {
        List<StoreResponseDto> topSellerStores = storeRepository.findTopSellerStores(top, location, OrderStatus.COMPLETED, StoreStatus.OPEN);
        return topSellerStores;
    }
}
