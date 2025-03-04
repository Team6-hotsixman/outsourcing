package com.example.outsourcing.domain.review.entity;

import com.example.outsourcing.domain.common.entity.BaseEntity;
import com.example.outsourcing.domain.order.entity.Orders;
import com.example.outsourcing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int rate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> images = new ArrayList<>();


    @Builder
    private Review(String content, int rate, User user, Orders order) {
        this.content = content;
        this.rate = rate;
        this.user = user;
        this.order = order;
    }

    public void updateContent(String contents) {
        if(!this.content.equals(contents)) {
            this.content = contents;
        }
    }

    public void updateRate(int rate) {
        if(this.rate != rate) {
            this.rate = rate;
        }
    }
}
