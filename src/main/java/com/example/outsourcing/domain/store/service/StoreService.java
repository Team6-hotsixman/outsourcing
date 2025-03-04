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
import com.example.outsourcing.domain.common.service.SearchKeywordRankingService;
import com.example.outsourcing.domain.store.dto.request.StoreDeleteRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreStatusUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.response.StoreNoticeResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreStatusResponseDto;
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

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    private final CategoryService categoryService;

    private final ImageService imageService;

    private final UserRepository userRepository;

    private final KaKaoMapApiService kaKaoMapApiService;
    private final UserAddressRepository userAddressRepository;
    private final SearchKeywordRankingService searchKeywordRankingService;

    /* hyen ho start */
    @Transactional
    public StoreResponseDto saveStore(User authUser, StoreSaveRequestDto dto) {
        CategoryResponse categoryResponse = categoryService.getCategoryById(dto.getCategoryId());
        Image image = imageService.getImageById(dto.getImageId());
        User user = userRepository.findById(1L).orElseThrow();

        //주소지를 좌표로 변환
        Point location =kaKaoMapApiService.getPoint(dto.getAddress());

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



    @Transactional
    public StoreResponseDto updateStore(Long storeId, User user, StoreUpdateRequestDto requestDto) {
        Store store = storeRepository.findById(storeId).orElseThrow(NotFoundStoreException::new);
        // 현재 사용자와 가게 주인과 비교
        // if (!store.getUser().getId().equals(user.getId())) {
        //      throw new UnauthorizedStoreOwnerException();
        //}

        if (requestDto.getImageId() != null) {
            Image newImage = imageService.getImageById(requestDto.getImageId());
            store.updateImage(newImage);
        }
        if (requestDto.getCategoryId() != null) {
            CategoryResponse categoryResponse = categoryService.getCategoryById(requestDto.getCategoryId());
            store.updateCategory(new Category(categoryResponse));
        }
        if (requestDto.getStoreName() != null && !requestDto.getStoreName().isBlank()) {
            store.updateStoreName(requestDto.getStoreName());
        }
        if (requestDto.getMinOrderPrice() != null) {
            store.updateMinOrderPrice(requestDto.getMinOrderPrice());
        }
        if (requestDto.getOpenTime() != null) {
            store.updateOpenTime(requestDto.getOpenTime());
        }
        if (requestDto.getCloseTime() != null) {
            store.updateCloseTime(requestDto.getCloseTime());
        }

        return StoreResponseDto.of(store);
    }

    @Transactional
    public StoreStatusResponseDto updateStoreStatus(User authUser, Long storeId, StoreStatusUpdateRequestDto requestDto) {
        Store store = storeRepository.findById(storeId).orElseThrow(NotFoundStoreException::new);
        // 현재 사용자와 가게 주인과 비교
        // if (!store.getUser().getId().equals(user.getId())) {
        // throw new UnauthorizedStoreOwnerException();
        // }

        // 현재 상태와 같으면 예외처리
        if (store.getStoreStatus().equals(requestDto.getStoreStatus())) {
            throw new StoreStatusAlreadySameException();
        }

        if (StoreStatus.OPEN.equals(requestDto.getStoreStatus())) {
            store.openStore();
        }
        if (StoreStatus.CLOSE.equals(requestDto.getStoreStatus())) {
            store.closeStore();
        }

        return new StoreStatusResponseDto(store.getStoreStatus());
    }

    @Transactional
    public StoreNoticeResponseDto updateStoreNotice(User authUser, Long storeId, StoreNoticeResponseDto requestDto) {
        Store store = storeRepository.findById(storeId).orElseThrow(NotFoundStoreException::new);
        // 현재 사용자와 가게 주인과 비교
        // if (!store.getUser().getId().equals(user.getId())) {
        //    throw new UnauthorizedStoreOwnerException();
//      //  }
        store.updateNotice(requestDto.getStoreNotice());

        return new StoreNoticeResponseDto(store.getStoreNotice());
    }

    @Transactional
    public void deleteStore(User authUser, Long storeId, StoreDeleteRequestDto requestDto) {
        Store store = storeRepository.findById(storeId).orElseThrow(NotFoundStoreException::new);
        // 현재 사용자와 가게 주인과 비교
//        if (!store.getUser().getId().equals(user.getId())) {
//            throw new UnauthorizedStoreOwnerException();
//        }
        // 현재 사용자 비번 확인 필요 인코딩 기능 추가시 추가 예정
        store.shutDownStore();

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
