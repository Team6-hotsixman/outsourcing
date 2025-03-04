package com.example.outsourcing.domain.store.service;


import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.common.exception.StoreLimitExceededException;
import com.example.outsourcing.domain.common.exception.NotFoundStoreException;
import com.example.outsourcing.domain.common.exception.StoreStatusAlreadySameException;
import com.example.outsourcing.domain.common.service.KaKaoMapApiService;
import com.example.outsourcing.domain.common.service.ImageService;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.service.MenuService;
import com.example.outsourcing.domain.common.service.SearchKeywordRankingService;
import com.example.outsourcing.domain.store.dto.request.StoreDeleteRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreStatusUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.response.*;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.OrderBy;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.category.service.CategoryService;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.entity.UserAddress;
import com.example.outsourcing.domain.user.enums.AddressStatus;
import com.example.outsourcing.domain.user.repository.UserAddressRepository;
import com.example.outsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    private final MenuService menuService;

    private final KaKaoMapApiService kaKaoMapApiService;
    private final UserAddressRepository userAddressRepository;
    private final SearchKeywordRankingService searchKeywordRankingService;

    /* hyen ho start */
    public StoreResponseDto getStore(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(NotFoundStoreException::new);

        List<Menu> menus = menuService.getMenus(storeId);

        return StoreResponseDto.of(store, menus);
    }
    /* hyen ho end */

    @Transactional(readOnly = true)
    public List<StoreResponseDto> searchStore(long userId, String searchKeyword, int size, OrderBy orderBy) {
        //사용자의 기본 배송지를 가져온다
        UserAddress address = userAddressRepository.findByUserIdAndAddressStatus(userId, AddressStatus.DEFAULT)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_DEFAULT_ADDRESS));

        //사용자의 기본 배송지 좌표
        Point point = kaKaoMapApiService.getPoint(address.getAddress());

        List<StoreResponseDto> storesByArea = List.of();

        if(searchKeyword == null || searchKeyword.isEmpty()){
            storesByArea = storeRepository.findStoresByArea(point,size, orderBy);
        } else {
            storesByArea = storeRepository.findStoresBySearch(point, searchKeyword, size, orderBy);
            if(!storesByArea.isEmpty()) {
                searchKeywordRankingService.increaseCount(searchKeyword);
            }
        }

        return storesByArea;
    }

    @Transactional(readOnly = true)
    public List<StoreResponseDto> searchStoreByCategory(long userId, long categoryId,  int size, OrderBy orderBy) {
        //사용자의 기본 배송지를 가져온다
        UserAddress address = userAddressRepository.findByUserIdAndAddressStatus(userId, AddressStatus.DEFAULT)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_DEFAULT_ADDRESS));

        Point point = kaKaoMapApiService.getPoint(address.getAddress());

        return storeRepository.findStoresByCategory(point, categoryId, size, orderBy);
    }
}
