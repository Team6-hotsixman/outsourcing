package com.example.outsourcing.domain.review.entity;

import com.example.outsourcing.domain.common.entity.BaseEntity;
import com.example.outsourcing.domain.common.entity.Image;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;


    public ReviewImage(Review review, Image image) {
        this.review = review;
        this.image = image;
    }

    public void delete(){
        this.review.getImages().remove(this);
    }

}
