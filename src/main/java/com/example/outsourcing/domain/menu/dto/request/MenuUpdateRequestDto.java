package com.example.outsourcing.domain.menu.dto.request;

import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuUpdateRequestDto {

    private final java.lang.String menuName;

    private final Integer price;

    private final java.lang.String description;

    private final Image image;

    private final boolean isAvailable;

    private final Long storeId;

    private final Long categoryId;

    public static Menu toEntity(MenuUpdateRequestDto requestDto, Store store, Category category) {
        return Menu.builder()
                .menuName(requestDto.getMenuName())
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .isAvailable(requestDto.isAvailable())
                .store(store)
                .image(requestDto.getImage())
                .category(category)
                .build();
    }
}
