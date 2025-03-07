package com.example.outsourcing.domain.menu.service;

import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.category.repository.CategoryRepository;
import com.example.outsourcing.domain.category.service.CategoryService;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.common.repository.ImageRepository;
import com.example.outsourcing.domain.common.service.ImageService;
import com.example.outsourcing.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcing.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcing.domain.menu.dto.response.MenuResponseDto;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.repository.MenuRepository;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final CategoryService categoryService;
    private final ImageService imageService;

    @Transactional
    public MenuResponseDto saveMenu(MenuSaveRequestDto requestDto, MultipartFile file) {
        Store store = storeRepository.findById(requestDto.getStoreId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_STORE));

        CategoryResponse categoryResponse = categoryService.getCategory(requestDto.getCategoryId());

        Category category = new Category(categoryResponse);

        Image image = imageService.uploadFile(file);

        Menu menu = MenuSaveRequestDto.toEntity(requestDto, store, category, image);

        menuRepository.save(menu);
        return MenuResponseDto.of(menu);
    }

    @Transactional
    public MenuResponseDto updateMenu(Long menuId, MenuUpdateRequestDto requestDto, MultipartFile file) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_MENU));

        CategoryResponse categoryResponse = categoryService.getCategory(requestDto.getCategoryId());

        Category category = new Category(categoryResponse);

        Image image = imageService.uploadFile(file);

        menu.updateMenuName(requestDto.getMenuName());
        menu.updatePrice(requestDto.getPrice());
        menu.updateDescription(requestDto.getDescription());
        menu.updateIsAvailable(requestDto.isAvailable());
        menu.updateCategory(category);
        menu.updateImage(image);

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
