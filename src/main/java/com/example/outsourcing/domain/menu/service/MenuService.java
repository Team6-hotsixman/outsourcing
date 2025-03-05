package com.example.outsourcing.domain.menu.service;

import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.category.repository.CategoryRepository;
import com.example.outsourcing.domain.common.dto.AuthUser;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public MenuResponseDto saveMenu(MenuSaveRequestDto requestDto) {
        Store store = storeRepository.findById(requestDto.getStoreId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_STORE));

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_CATEGORY));

        Image image = imageRepository.findById(requestDto.getImageId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_IMAGE));

        Menu menu = MenuSaveRequestDto.toEntity(requestDto, store, category, image);

        menuRepository.save(menu);
        return MenuResponseDto.of(menu);
    }

    @Transactional
    public MenuResponseDto updateMenu(Long menuId, MenuUpdateRequestDto requestDto) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_MENU));

        Optional<Category> category = categoryRepository.findById(requestDto.getCategoryId());

        Optional<Image> image = imageRepository.findById(requestDto.getImageId());

        menu.updateMenuName(requestDto.getMenuName());
        menu.updatePrice(requestDto.getPrice());
        menu.updateDescription(requestDto.getDescription());
        menu.updateIsAvailable(requestDto.isAvailable());
        menu.updateCategory(category.orElse(null));
        menu.updateImage(image.orElse(null));

        return MenuResponseDto.of(menu);
    }

    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_MENU));

        menuRepository.delete(menu);
    }

    public List<Menu> getMenus(Long storeId) {
        return menuRepository.findByStoreId(storeId);
    }
}
