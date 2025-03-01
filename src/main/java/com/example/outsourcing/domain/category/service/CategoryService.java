package com.example.outsourcing.domain.category.service;

import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category getCategoryById(Long categoryId) {
        //예외처리 추가 해야함
        return categoryRepository.findById(categoryId).orElseThrow();
    }
}
