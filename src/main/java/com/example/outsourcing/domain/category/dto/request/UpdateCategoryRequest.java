package com.example.outsourcing.domain.category.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateCategoryRequest {
    String newCategoryName;
}
