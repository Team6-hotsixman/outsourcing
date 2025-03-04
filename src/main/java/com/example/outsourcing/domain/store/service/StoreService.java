package com.example.outsourcing.domain.store.service;


import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.exception.StoreLimitExceededException;
import com.example.outsourcing.domain.common.exception.NotFoundStoreException;
import com.example.outsourcing.domain.common.exception.StoreStatusAlreadySameException;
import com.example.outsourcing.domain.common.service.ImageService;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.service.MenuService;
import com.example.outsourcing.domain.store.dto.request.StoreDeleteRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreStatusUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.response.*;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.category.service.CategoryService;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    private final MenuService menuService;

    /* hyen ho start */
    public StoreResponseDto getStore(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(NotFoundStoreException::new);

        List<Menu> menus = menuService.getMenus(storeId);

        return StoreResponseDto.of(store, menus);
    }
    /* hyen ho end */
}
