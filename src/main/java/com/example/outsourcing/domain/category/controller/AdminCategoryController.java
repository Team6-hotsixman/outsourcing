package com.example.outsourcing.domain.category.controller;

import com.example.outsourcing.domain.category.dto.request.CreateCategoryRequest;
import com.example.outsourcing.domain.category.dto.request.UpdateCategoryRequest;
import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.category.service.AdminCategoryService;
import com.example.outsourcing.domain.common.annotation.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Admin
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> saveCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        CategoryResponse categoryResponse = adminCategoryService.saveCategory(createCategoryRequest);

        return ResponseEntity.ok(categoryResponse);
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable long categoryId,
                                                           @RequestBody UpdateCategoryRequest updateCategoryRequest
    ){
        CategoryResponse updatedCategory =  adminCategoryService.updateCategory(categoryId, updateCategoryRequest);

        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long categoryId) {
        adminCategoryService.deleteCategory(categoryId);

        return ResponseEntity.ok().build();
    }
}
