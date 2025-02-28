package com.example.outsourcing.domain.category.entity;


import com.example.outsourcing.domain.category.enums.CategoryName;
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

    @Enumerated(EnumType.STRING)
    private CategoryName category;

    @Builder
    private Category(CategoryName category) {
        this.category = category;
    }

}
