package com.example.outsourcing.domain.store.service;


import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.service.ImageService;
import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.category.service.CategoryService;
import com.example.outsourcing.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    private final CategoryService categoryService;

    private final ImageService imageService;

    /* hyen ho start */
    public StoreResponseDto saveStore(User authUser, StoreSaveRequestDto dto) {
        Category category = categoryService.getCategoryById(dto.getCatogoryId());
        Image image = imageService.getImageById(dto.getImageId());
        Store store = Store.builder()
                .image(image)
                .category(category)
                .storeName(dto.getStoreName())
                .storeStatus(dto.getStoreStatus())
                .storeNotice(dto.getStoreNotice())
                .address(dto.getAddress())
                .minOrderPrice(dto.getMinOrderPrice())
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .build();
        storeRepository.save(store);
        return StoreResponseDto.of(store);
    }
    /* hyen ho end */
}
