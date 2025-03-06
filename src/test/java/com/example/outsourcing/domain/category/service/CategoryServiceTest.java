package com.example.outsourcing.domain.category.service;

import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.category.repository.CategoryRepository;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryService categoryService;

    @Test
    void getAllCategories_카테고리리스트를가져온다(){
        //given
        Category c1 = createCategory(1L, "1");
        Category c2 = createCategory(2L, "2");
        List<Category> categories = List.of(c1, c2);
        given(categoryRepository.findAll()).willReturn(categories);
        //when
        List<CategoryResponse> allCategories = categoryService.getCategories();
        //then
        assertEquals(categories.size(), allCategories.size());
        assertEquals(categories.get(0).getId(), allCategories.get(0).getId());
    }
    @Test
    void getAllCategories_빈리스트를가져온다(){
        //given
        List<Category> categories = List.of();
        given(categoryRepository.findAll()).willReturn(categories);
        //when
        List<CategoryResponse> allCategories = categoryService.getCategories();
        //then
        assertEquals(0, allCategories.size());
    }

    @Test
    void getCategoryById_id로카테고리를가져온다(){
        //given
        long categoryId = 1L;
        Category c1 = createCategory(categoryId, "1");
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(c1));
        //when
        CategoryResponse categoryResponse = categoryService.getCategory(categoryId);
        //then
        assertEquals(categoryId, categoryResponse.getId());
    }

    @Test
    void getCategoryById_id값으로카테고리를찾을수없을때예외를발생(){
        //given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());
        //when & then
        assertThrows(ApplicationException.class, ()->categoryService.getCategory(anyLong()),"없는 카테고리 입니다.");
    }


    Category createCategory(long id, String name){
        Category category = Category.builder()
                .categoryName(name).build();
        ReflectionTestUtils.setField(category, "id",id);

        return category;
    }
}