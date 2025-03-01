package com.example.outsourcing.domain.category.entity;


import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String categoryName;

    @Builder
    private Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category(CategoryResponse categoryResponse) {
        this.id = categoryResponse.getId();
        this.categoryName = categoryResponse.getCategoryName();
    }

    public void updateCategoryName(String newCategoryName) {
        this.categoryName = newCategoryName;
    }


}
