package com.example.outsourcing.domain.category.service;

import com.example.outsourcing.domain.category.dto.request.CreateCategoryRequest;
import com.example.outsourcing.domain.category.dto.request.UpdateCategoryRequest;
import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.category.repository.CategoryRepository;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminCategoryServiceTest {
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    AdminCategoryService adminCategoryService;

    @Test
    void saveCategory_카테고리를저장한다() {
        //given
        long categoryId = 1;
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("new Category");
        Category category = createCategory(categoryId, createCategoryRequest.getCategoryName());

        given(categoryRepository.existsByCategoryName(anyString())).willReturn(false);
        given(categoryRepository.save(any(Category.class))).willReturn(category);
        //when
        CategoryResponse categoryResponse = adminCategoryService.saveCategory(createCategoryRequest);
        //then

        assertEquals(categoryId, categoryResponse.getId());
        assertEquals("new Category", categoryResponse.getCategoryName());

    }
    @Test
    void saveCategory_카테고리이름이중복될경우예외를던진다(){
        //given
        long categoryId = 1;
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("new Category");
        given(categoryRepository.existsByCategoryName(anyString())).willReturn(true);

        //when & then
        assertThrows(ApplicationException.class,
                () -> adminCategoryService.saveCategory(createCategoryRequest),
                "이미 존재하는 카테고리입니다.");
    }

    @Test
    void updateCategory_카테고리를업데이트한다(){
        //given
        long categoryId = 1;
        UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest("update Category");
        Category category = createCategory(categoryId, "new Category");
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));
        given(categoryRepository.save(any(Category.class))).willReturn(any(Category.class));
        //when
        CategoryResponse categoryResponse = adminCategoryService.updateCategory(categoryId, updateCategoryRequest);
        //then
        assertEquals(categoryId, categoryResponse.getId());
        assertEquals("update Category", categoryResponse.getCategoryName());
    }

    @Test
    void updateCategory_id로카테고리를찾을수없으면예외를던진다(){
        //given
        long categoryId = 1;
        UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest("update Category");
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

        //when &  then
        assertThrows(ApplicationException.class,
                ()-> adminCategoryService.updateCategory(categoryId, updateCategoryRequest),
                ErrorCode.NOT_FOUND_CATEGORY.getMessage());
    }

    @Test
    void deleteCategory_카테고리를삭제한다(){
        //given
        doNothing().when(categoryRepository).deleteById(anyLong());
        //when
        adminCategoryService.deleteCategory(1L);
        //then
        verify(categoryRepository, times(1)).deleteById(anyLong());
    }




    Category createCategory(long id, String name){
        Category category = Category.builder()
                .categoryName(name).build();
        ReflectionTestUtils.setField(category, "id",id);

        return category;
    }
}