package com.example.outsourcing.domain.store.entity;

import com.example.outsourcing.domain.common.entity.BaseEntity;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import org.locationtech.jts.geom.Point;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalTime;

@Entity
@Getter
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
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false, columnDefinition = "POINT SRID 4326")
    private Point location;

    @Builder
    public Store(
            User user,
            Image image,
            Category category,
            String storeName,
            StoreStatus storeStatus,
            String storeNotice,
            String address,
            int minOrderPrice,
            LocalTime openTime,
            LocalTime closeTime,
            Point location
    ) {
        this.user = user;
        this.image = image;
        this.category = category;
        this.storeName = storeName;
        this.storeStatus = storeStatus;
        this.storeNotice = storeNotice;
        this.address = address;
        this.minOrderPrice = minOrderPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.location = location;
    }
}
