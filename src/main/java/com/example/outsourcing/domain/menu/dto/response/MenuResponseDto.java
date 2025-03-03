package com.example.outsourcing.domain.menu.dto.response;

import com.example.outsourcing.domain.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuResponseDto {

    private String menuName;

    private Integer price;

    private String description;

    private boolean isAvailable;

    private String storeName;

    private String category;

    private Long image;


    public static MenuResponseDto of(Menu menu) {
        return new MenuResponseDto(
                menu.getMenuName(),
                menu.getPrice(),
                menu.getDescription(),
                menu.isAvailable(),
                menu.getStore().getStoreName(),
                menu.getCategory().getCategoryName(),
                menu.getImage().getId());
    }

}
