package com.example.outsourcing.domain.store.service;


import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.common.exception.StoreLimitExceededException;
import com.example.outsourcing.domain.common.service.GeoCodingService;
import com.example.outsourcing.domain.common.service.ImageService;
import com.example.outsourcing.domain.common.service.SearchKeywordRankingService;
import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.category.service.CategoryService;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.entity.UserAddress;
import com.example.outsourcing.domain.user.enums.AddressStatus;
import com.example.outsourcing.domain.user.repository.UserAddressRepository;
import com.example.outsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    private final CategoryService categoryService;

    private final ImageService imageService;

    private final UserRepository userRepository;

    private final GeoCodingService geoCodingService;
    private final UserAddressRepository userAddressRepository;
    private final SearchKeywordRankingService searchKeywordRankingService;

    /* hyen ho start */
    @Transactional
    public StoreResponseDto saveStore(User authUser, StoreSaveRequestDto dto) {
        CategoryResponse categoryResponse = categoryService.getCategoryById(dto.getCatogoryId());
        Image image = imageService.getImageById(dto.getImageId());
        User user = userRepository.findById(1L).orElseThrow();

        //주소지를 좌표로 변환
        Point location =geoCodingService.getPoint(dto.getAddress());

        if (storeRepository.countStoresByUserId(user.getId()) >= 3) {
            throw new StoreLimitExceededException();
        }

        Store store = Store.builder()
                .user(user)
                .image(image)
                .category(new Category(categoryResponse))
                .storeName(dto.getStoreName())
                .storeStatus(dto.getStoreStatus())
                .storeNotice(dto.getStoreNotice())
                .address(dto.getAddress())
                .minOrderPrice(dto.getMinOrderPrice())
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .location(location)
                .build();
        storeRepository.save(store);
        return StoreResponseDto.of(store);
    }


    /* hyen ho end */

    public List<StoreResponseDto> getAllStores(long userId, String searchKeyword, double distance, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        //사용자의 기본 배송지를 가져온다
        UserAddress address = userAddressRepository.findByUserIdAndAddressStatus(userId, AddressStatus.DEFAULT)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_DEFAULT_ADDRESS));

        //주소지를 좌표로 변환
        Point center = geoCodingService.getPoint(address.getAddress());

        Page<Store> stores;
        //좌표기준 distance내분에있는 가게리스트 반환
        if(searchKeyword == null || searchKeyword.isEmpty()) {
            stores = storeRepository.findStoresByArea(center, distance, pageable);
        } else {
            stores = storeRepository.findStoresByAreaAndSearch(center, distance, searchKeyword, pageable);
            //검색결과가 존재하고 검색어가 null이 아니라면 redis에 검색어 등록
            if(!stores.isEmpty()){
                searchKeywordRankingService.increaseCount(searchKeyword);
            }
        }

        return stores.getContent().stream().map(StoreResponseDto::of).toList();
    }
}
