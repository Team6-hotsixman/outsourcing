package com.example.outsourcing.domain.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewCreateRequest {
    String content;
    int rate;
}
