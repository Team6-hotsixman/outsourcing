package com.example.outsourcing.domain.menu.menuoption.dto.request;

import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.menuoption.entity.MenuOption;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuOptionSaveRequestDto {

    @NotNull
    @Size(max = 20)
    private final String optionName;

    @NotNull
    @Min(value = 1000)
    private final Integer price;

    private final String description;

    @NotNull
    private final Long imageId;

    @NotNull
    private final Long menuId;

    public static MenuOption toEntity(MenuOptionSaveRequestDto requestDto, Menu menu, Image image) {
        return MenuOption.builder()
                .optionName(requestDto.getOptionName())
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .menu(menu)
                .image(image)
                .build();
    }

}
