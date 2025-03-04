package com.example.outsourcing.domain.menu.dto.request;

import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.store.entity.Store;
import lombok.Getter;

@Getter
public class MenuSaveRequestDto {

    private final String menuName;

    private final Integer price;

    private final String description;

    private final Long categoryId;

    private final Long storeId;

    private final Long imageId;

    public MenuSaveRequestDto(String menuName, Integer price, String description, Long categoryId, Long storeId, Long imageId) {
        this.menuName = menuName;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
        this.storeId = storeId;
        this.imageId = imageId;
    }

    public static Menu toEntity(MenuSaveRequestDto requestDto, Store store, Category category, Image image) {
        return Menu.builder()
                .menuName(requestDto.getMenuName())
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .isAvailable(true)
                .store(store)
                .image(image)
                .category(category)
                .build();
    }
}
