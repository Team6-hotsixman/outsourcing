package com.example.outsourcing.domain.review.controller;

import com.example.outsourcing.domain.common.annotation.Auth;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.review.dto.request.ReviewCreateRequest;
import com.example.outsourcing.domain.review.dto.request.ReviewUpdateRequest;
import com.example.outsourcing.domain.review.dto.response.ReviewResponse;
import com.example.outsourcing.domain.review.repository.ReviewRepository;
import com.example.outsourcing.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value ="/reviews/orders/{orderId}")
    public ResponseEntity<ReviewResponse> save(@Auth AuthUser authUser,
                                               @PathVariable long orderId,
                                               @RequestPart(value = "create", required = false) ReviewCreateRequest reviewCreateRequest,
                                               @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        ReviewResponse reviewResponse = reviewService.save(orderId, authUser, reviewCreateRequest, images);
        return ResponseEntity.ok(reviewResponse);
    }

    @GetMapping("/reviews/stores/{storeId}")
    public ResponseEntity<List<ReviewResponse>> getAll(@PathVariable long storeId,
                                                       @RequestParam(name = "last",required = false, defaultValue = "") Optional<LocalDateTime> last,
                                                       @RequestParam(name = "start", required = false, defaultValue ="0") int start,
                                                       @RequestParam(name = "end", required = false, defaultValue ="5") int end){
        LocalDateTime lastDate = last.orElse(LocalDateTime.now());
        List<ReviewResponse> reviews = reviewService.getReviewsByStoreId(storeId, start, end, lastDate);

        return ResponseEntity.ok(reviews);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> update(@Auth AuthUser authUser,
                                                 @PathVariable long reviewId,
                                                 @RequestPart(value = "update", required = false) ReviewUpdateRequest reviewUpdateRequest,
                                                 @RequestPart(value = "images", required = false) List<MultipartFile> images){
        ReviewResponse reviewResponse = reviewService.updateReview(reviewId, authUser.getId(), reviewUpdateRequest, images);

        return ResponseEntity.ok(reviewResponse);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> delete(@Auth AuthUser authUser,
                                       @PathVariable long reviewId) {
        reviewService.deleteReview(authUser.getId(), reviewId);

        return ResponseEntity.ok().build();
    }

}
