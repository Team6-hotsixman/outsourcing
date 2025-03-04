package com.example.outsourcing.domain.review.dto.response;

import com.example.outsourcing.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewResponse {
    private final long id;
    private final long userId;
    private final String userName;
    private final int rate;
    private final String contents;
    private final List<String> images;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    public static ReviewResponse of(Review review) {
        List<String> images = review.getImages().stream().map(m -> m.getImage().getImagePath()).toList();
        return new ReviewResponse(
                review.getId(),
                review.getUser().getId(),
                review.getUser().getName(),
                review.getRate(),
                review.getContent(),
                images,
                review.getCreatedAt(),
                review.getModifiedAt()
        );
    }
}
