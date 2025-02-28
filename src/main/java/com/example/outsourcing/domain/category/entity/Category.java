package com.example.outsourcing.domain.category.entity;


import com.example.outsourcing.domain.category.enums.CategoryName;
import com.example.outsourcing.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false)
    CategoryName category;

    public Category(CategoryName category) {
        this.category = category;
    }

}
