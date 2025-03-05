package com.example.outsourcing.domain.category.dto.response;


import com.example.outsourcing.domain.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {

    private final long id;
    private final String categoryName;

    public static CategoryResponse of(Category category) {
        return new CategoryResponse(category.getId(), category.getCategoryName());
    }
}
