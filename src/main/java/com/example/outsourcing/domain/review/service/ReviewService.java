package com.example.outsourcing.domain.review.service;

import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.common.service.ImageService;
import com.example.outsourcing.domain.order.entity.Orders;
import com.example.outsourcing.domain.order.enums.OrderStatus;
import com.example.outsourcing.domain.order.repository.OrderRepository;
import com.example.outsourcing.domain.review.dto.request.ReviewCreateRequest;
import com.example.outsourcing.domain.review.dto.request.ReviewUpdateRequest;
import com.example.outsourcing.domain.review.dto.response.ReviewResponse;
import com.example.outsourcing.domain.review.entity.Review;
import com.example.outsourcing.domain.review.entity.ReviewImage;
import com.example.outsourcing.domain.review.repository.ReviewRepository;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;
    private final ImageService imageService;

    @Transactional
    public ReviewResponse saveReview(long orderId, AuthUser authUser, ReviewCreateRequest request, List<MultipartFile> images) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(()->new ApplicationException(ErrorCode.NOT_FOUND_USER));
        Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_ORDER));
        if(orders.getOrderStatus() != OrderStatus.COMPLETED){
            //수정필요 : 메시지 합의
            new ApplicationException(ErrorCode.METHOD_ARGUMENT_NOT_VALID);
        }
        Review review = Review.builder()
                .order(orders)
                .user(user)
                .content(request.getContent())
                .rate(request.getRate())
                .store(orders.getStore())
                .build();

        for (MultipartFile image : images) {
            //수정필요
            //Image save = imageService.save(image);
            review.getImages().add(new ReviewImage(review, new Image()));
        }

        Review save = reviewRepository.save(review);

        return ReviewResponse.of(save);
    }

    public List<ReviewResponse> getReviews(long storeId, int start, int end, LocalDateTime last) {
        List<Review> reviewsByStoreId = reviewRepository.findReviewsByStoreId(storeId, start, end, last);
        return reviewsByStoreId.stream().map(ReviewResponse::of).toList();
    }

    @Transactional
    public ReviewResponse updateReview(long reviewId, long userId, ReviewUpdateRequest request, List<MultipartFile> images) {
        Review review = reviewRepository.findByIdWithUserAndOrder(reviewId).orElseThrow(()->new ApplicationException(ErrorCode.NOT_FOUND_REVIEW));
        if(review.getUser().getId() != userId){
            throw new ApplicationException(ErrorCode.Unauthorized_User);
        }

        //수정할 이미지가 존재한다면
        if(images!= null && !images.isEmpty()){
            //리뷰의 이미지를 비우고 새로운 이미지로 채운다
            review.getImages().clear();
            for(MultipartFile image : images){
                //수정필요
                //Image save = imageService.save(image);
                long newId = 2L;
                review.getImages().add(new ReviewImage(review, new Image()));
            }
        }

        if(request.getContent() != null){
            review.updateContent(request.getContent());
        }
        if(request.getRate() != null){
            review.updateRate(request.getRate());
        }
        reviewRepository.save(review);

        return ReviewResponse.of(review);
    }


    public void deleteReview(Long userId, long reviewId) {
        Review review = reviewRepository.findByIdWithUserAndOrder(reviewId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_REVIEW));
        if(review.getUser().getId() != userId){
            throw new ApplicationException(ErrorCode.Unauthorized_User);
        }
        reviewRepository.delete(review);
    }
}
