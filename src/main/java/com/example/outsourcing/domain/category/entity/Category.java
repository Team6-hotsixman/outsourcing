package com.example.outsourcing.domain.category.entity;


import com.example.outsourcing.domain.category.enums.CategoryName;
import com.example.outsourcing.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private CategoryName category;

    @Builder
    private Category(CategoryName category) {
        this.category = category;
    }

}
