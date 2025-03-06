package com.example.outsourcing.domain.store.service;


import com.example.outsourcing.domain.auth.config.PasswordEncoder;
import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.category.service.CategoryService;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.exception.*;
import com.example.outsourcing.domain.common.service.ImageService;
import com.example.outsourcing.domain.common.service.KaKaoMapApiService;
import com.example.outsourcing.domain.store.dto.request.StoreDeleteRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreStatusUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.response.SaveStoreResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreNoticeResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreStatusResponseDto;
import com.example.outsourcing.domain.store.dto.response.UpdateStoreResponseDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerStoreService {
    private final StoreRepository storeRepository;

    private final CategoryService categoryService;

    private final KaKaoMapApiService kaKaoMapApiService;

    private final ImageService imageService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SaveStoreResponseDto saveStore(AuthUser authUser, StoreSaveRequestDto dto) {
        CategoryResponse categoryResponse = categoryService.getCategory(dto.getCategoryId());
        Image image = imageService.getImageById(dto.getImageId());
        User user = User.fromAuthUser(authUser);
        Point point = kaKaoMapApiService.getPoint(dto.getAddress());

        // 한 사장님이 최대 3개의 가게만 운영 가능하도록 제한
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
                .location(point)
                .build();
        storeRepository.save(store);
        return SaveStoreResponseDto.of(store);
    }

    @Transactional
    public UpdateStoreResponseDto updateStore(Long storeId, AuthUser authUser, StoreUpdateRequestDto requestDto) {
        Store store = storeRepository.findById(storeId).orElseThrow(NotFoundStoreException::new);
        User user = User.fromAuthUser(authUser);

        // 현재 사용자와 가게 주인과 비교
         if (!store.getUser().getId().equals(user.getId())) {
              throw new UnauthorizedStoreOwnerException();
        }

        if (requestDto.getImageId() != null) {
            Image newImage = imageService.getImageById(requestDto.getImageId());
            store.updateImage(newImage);
        }
        if (requestDto.getCategoryId() != null) {
            CategoryResponse categoryResponse = categoryService.getCategory(requestDto.getCategoryId());
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

        return UpdateStoreResponseDto.of(store);
    }

    @Transactional
    public StoreStatusResponseDto updateStoreStatus(AuthUser authUser, Long storeId, StoreStatusUpdateRequestDto requestDto) {
        Store store = storeRepository.findById(storeId).orElseThrow(NotFoundStoreException::new);
        User user = User.fromAuthUser(authUser);

        // 현재 사용자와 가게 주인과 비교
         if (!store.getUser().getId().equals(user.getId())) {
         throw new UnauthorizedStoreOwnerException();
         }

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
    public StoreNoticeResponseDto updateStoreNotice(AuthUser authUser, Long storeId, StoreNoticeResponseDto requestDto) {
        Store store = storeRepository.findById(storeId).orElseThrow(NotFoundStoreException::new);
        User user = User.fromAuthUser(authUser);

        // 현재 사용자와 가게 주인과 비교
        if (!store.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedStoreOwnerException();
        }
        store.updateNotice(requestDto.getStoreNotice());

        return new StoreNoticeResponseDto(store.getStoreNotice());
    }

    @Transactional
    public void deleteStore(AuthUser authUser, Long storeId, StoreDeleteRequestDto requestDto) {
        Store store = storeRepository.findById(storeId).orElseThrow(NotFoundStoreException::new);
        User user = User.fromAuthUser(authUser);

        // 현재 사용자와 가게 주인과 비교
        if (!store.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedStoreOwnerException();
        }
        // 현재 사용자 비번 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.MISS_MATCH_PASSWORD);
        }
        store.shutDownStore();
    }
}
