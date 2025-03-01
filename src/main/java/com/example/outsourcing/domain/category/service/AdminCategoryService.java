package com.example.outsourcing.domain.category.service;

import com.example.outsourcing.domain.category.dto.request.CreateCategoryRequest;
import com.example.outsourcing.domain.category.dto.request.UpdateCategoryRequest;
import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.category.repository.CategoryRepository;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponse addCategory(CreateCategoryRequest createCategoryRequest) {
        if(categoryRepository.existsByCategoryName(createCategoryRequest.getCategoryName())){
            throw new ApplicationException(ErrorCode.DUPLICATE_CATEGORY);
        }
        Category category = Category.builder()
                .categoryName(createCategoryRequest.getCategoryName())
                .build();
        category = categoryRepository.save(category);

        return CategoryResponse.of(category);
    }

    public CategoryResponse updateCategory(long categoryId, UpdateCategoryRequest updateCategoryRequest) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_CATEGORY));

        category.updateCategoryName(updateCategoryRequest.getNewCategoryName());
        categoryRepository.save(category);

        return CategoryResponse.of(category);
    }

    public void deleteCategory(long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
