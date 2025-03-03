package com.example.outsourcing.domain.menu.service;

import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.category.repository.CategoryRepository;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.common.exception.UnauthorizedUserException;
import com.example.outsourcing.domain.common.repository.ImageRepository;
import com.example.outsourcing.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcing.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcing.domain.menu.dto.response.MenuResponseDto;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.repository.MenuRepository;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public MenuResponseDto saveMenu(User user, MenuSaveRequestDto requestDto) {
        Store store = storeRepository.findById(requestDto.getStoreId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_STORE));

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_CATEGORY));

        Image image = imageRepository.findById(requestDto.getImageId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_IMAGE));

        // 권한 검증 - 추후 AOP 분리
        if (!store.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedUserException();
        }

        Menu menu = MenuSaveRequestDto.toEntity(requestDto, store, category, image);

        menuRepository.save(menu);
        return MenuResponseDto.of(menu);
    }

    @Transactional
    public MenuResponseDto updateMenu(Long menuId, User user, MenuUpdateRequestDto requestDto) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_USER));

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_CATEGORY));

        Store store = menu.getStore();

        // 권한 검증 - 추후 AOP 분리
        if (!store.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedUserException();
        };

        menu.updateMenu(requestDto, category);

        return MenuResponseDto.of(menu);
    }

    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_MENU));
        menuRepository.delete(menu);
    }
}
