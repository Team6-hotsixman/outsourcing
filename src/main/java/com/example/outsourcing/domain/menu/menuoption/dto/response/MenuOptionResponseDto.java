package com.example.outsourcing.domain.menu.menuoption.dto.response;

import com.example.outsourcing.domain.menu.menuoption.entity.MenuOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuOptionResponseDto {

    private final String optionName;

    private final Integer price;

    private final String description;

    private final String menuName;

    private final boolean isAvailable;

    private final String imagePath;

    public static MenuOptionResponseDto of(MenuOption menuOption) {
        return new MenuOptionResponseDto(
                menuOption.getOptionName(),
                menuOption.getPrice(),
                menuOption.getDescription(),
                menuOption.getMenu().getMenuName(),
                menuOption.isAvailable(),
                menuOption.getImage().getImagePath()
        );
    }
}
