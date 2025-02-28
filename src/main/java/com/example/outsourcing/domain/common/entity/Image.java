package com.example.outsourcing.domain.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    String imagePath;

    public Image(String imagePath) {
        this.imagePath = imagePath;
    }
}
