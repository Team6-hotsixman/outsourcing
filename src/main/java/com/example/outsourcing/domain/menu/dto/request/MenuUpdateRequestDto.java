package com.example.outsourcing.domain.menu.dto.request;

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

}
