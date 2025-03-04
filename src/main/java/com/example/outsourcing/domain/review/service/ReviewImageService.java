package com.example.outsourcing.domain.review.service;

import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.service.ImageService;
import com.example.outsourcing.domain.review.entity.Review;
import com.example.outsourcing.domain.review.entity.ReviewImage;
import com.example.outsourcing.domain.review.repository.ReviewImageRepository;
import com.example.outsourcing.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReviewImageService {
    private ImageService imageService;
    private final ReviewImageRepository reviewImageRepository;
}
