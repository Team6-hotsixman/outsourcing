package com.example.outsourcing.domain.store.entity;

import com.example.outsourcing.domain.common.entity.BaseEntity;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = id, nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = id, nullable = false)
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = id, nullable = false)
    private Category category;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private StoreStatus storeStatus;

    private String storeNotice;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int minOrderPrice;

    @Column(nullable = false)
    private LocalDateTime openTime;

    @Column(nullable = false)
    private LocalDateTime closeTime;

    @Builder
    public Store(
            User user,
            Image image,
            Category category,
            StoreStatus storeStatus,
            String storeNotice,
            String address,
            int minOrderPrice,
            LocalDateTime openTime,
            LocalDateTime closeTime
    ) {
        this.ownerId = ownerId;
        this.imageId = imageId;
        this.categoryId = categoryId;
        this.storeStatus = storeStatus;
        this.storeNotice = storeNotice;
        this.address = address;
        this.minOrderPrice = minOrderPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
