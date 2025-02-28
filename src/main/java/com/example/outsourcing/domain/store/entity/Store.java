package com.example.outsourcing.domain.store.entity;

import com.example.outsourcing.domain.common.entity.BaseEntity;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.store.enums.StoreStatus;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String storeName;

    @Enumerated(EnumType.STRING)
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
        this.user = user;
        this.image = image;
        this.category = category;
        this.storeStatus = storeStatus;
        this.storeNotice = storeNotice;
        this.address = address;
        this.minOrderPrice = minOrderPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
