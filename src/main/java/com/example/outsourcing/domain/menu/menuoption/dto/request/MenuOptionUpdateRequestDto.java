package com.example.outsourcing.domain.menu.menuoption.dto.request;

import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.menuoption.entity.MenuOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuOptionUpdateRequestDto {

    private final String optionName;

    private final Integer price;

    private final String description;

    private final Long imageId;

    private final boolean isAvailable;

    public static MenuOption of(MenuOptionUpdateRequestDto requestDto, Menu menu, Image image) {
        return MenuOption.builder()
                .optionName(requestDto.getOptionName())
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .menu(menu)
                .image(image)
                .isAvailable(requestDto.isAvailable())
                .build();
    }
}
