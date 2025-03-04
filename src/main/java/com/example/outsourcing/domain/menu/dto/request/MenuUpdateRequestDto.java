package com.example.outsourcing.domain.menu.dto.request;

import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuUpdateRequestDto {

    private final String menuName;

    private final Integer price;

    private final String description;

    private final Long imageId;

    private final boolean isAvailable;

    private final Long storeId;

    private final Long categoryId;

    public static Menu toEntity(MenuUpdateRequestDto requestDto, Category category, Image image) {
        return Menu.builder()
                .menuName(requestDto.getMenuName())
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .isAvailable(requestDto.isAvailable())
                .image(image)
                .category(category)
                .build();
    }
}
