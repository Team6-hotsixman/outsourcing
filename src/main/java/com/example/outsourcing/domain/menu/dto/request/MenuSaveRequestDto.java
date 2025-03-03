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

    private final Image image;

    public MenuSaveRequestDto(String menuName, Integer price, String description, Long categoryId, Long storeId, Image image) {
        this.menuName = menuName;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
        this.storeId = storeId;
        this.image = image;
    }

    public static Menu toEntity(MenuSaveRequestDto requestDto, Store store, Category category) {
        return Menu.builder()
                .menuName(requestDto.getMenuName())
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .isAvailable(true)
                .store(store)
                .image(requestDto.getImage())
                .category(category)
                .build();
    }
}
