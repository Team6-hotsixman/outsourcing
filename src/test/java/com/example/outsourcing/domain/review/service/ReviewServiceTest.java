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
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import com.example.outsourcing.domain.user.repository.UserRepository;
import jakarta.persistence.SqlResultSetMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ImageService imageService;

    private User user;
    private Orders order;
    private Review review;
    private User owner;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("a@a.com")
                .password("password")
                .name("user")
                .point(100)
                .userRole(UserRole.USER)
                .userStatus(null)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        owner = User.builder()
                .email("a@a.com")
                .password("password")
                .name("owner")
                .point(100)
                .userRole(UserRole.USER)
                .userStatus(null)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(owner, "id", 99L);


        order = Orders.builder()
                .orderStatus(OrderStatus.COMPLETED)
                .store(null)
                .user(user)
                .build();
        ReflectionTestUtils.setField(order, "id", 1L);

        review = Review.builder()
                .order(order)
                .user(user)
                .content("content")
                .rate(2)
                .store(order.getStore())
                .build();
        ReflectionTestUtils.setField(review, "id", 1L);

        Image image = new Image(1L,"url","name");
        ReviewImage reviewImage = new ReviewImage(review, image);
        ReflectionTestUtils.setField(review, "images", new ArrayList(List.of(reviewImage)));
    }

    @Test
    void saveReview_리뷰저장성공() {
        // given
        long orderId = 1L;
        AuthUser authUser = new AuthUser(user.getId(), user.getEmail(), user.getUserRole());
        ReviewCreateRequest request = new ReviewCreateRequest(review.getContent(), review.getRate());
        List<MultipartFile> images = List.of(mock(MultipartFile.class));

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        given(reviewRepository.existsByUserIdAndOrderId(anyLong(), anyLong())).willReturn(false);
        given(imageService.uploadFile(any())).willReturn(new Image(1L, "test.jpg", "url"));
        given(reviewRepository.save(any(Review.class))).willReturn(review);

        // when
        ReviewResponse response = reviewService.saveReview(orderId, authUser, request, images);

        // then
        assertNotNull(response);
        assertEquals(request.getContent(), response.getContents());
        assertEquals(request.getRate(), response.getRate());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void saveReview_리뷰저장성공_가게사장도리뷰를달수있다() {
        // given
        long orderId = 1L;
        AuthUser authUser = new AuthUser(user.getId(), user.getEmail(), user.getUserRole());
        ReviewCreateRequest request = new ReviewCreateRequest(review.getContent(), review.getRate());
        List<MultipartFile> images = List.of(mock(MultipartFile.class));

        //주문데이터 세팅
        Store store = mock(Store.class);
        ReflectionTestUtils.setField(order, "store", store);
        given(store.getUser()).willReturn(owner);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(owner));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        given(reviewRepository.existsByUserIdAndOrderId(anyLong(), anyLong())).willReturn(false);
        given(imageService.uploadFile(any())).willReturn(new Image(1L, "test.jpg", "url"));
        given(reviewRepository.save(any(Review.class))).willReturn(review);

        // when
        ReviewResponse response = reviewService.saveReview(orderId, authUser, request, images);

        // then
        assertNotNull(response);
        assertEquals(request.getContent(), response.getContents());
        assertEquals(request.getRate(), response.getRate());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void saveReview_리뷰저장실패_주문없음() {
        // given
        long orderId = 1L;
        AuthUser authUser = new AuthUser(user.getId(), user.getEmail(), user.getUserRole());
        ReviewCreateRequest request = new ReviewCreateRequest(review.getContent(), review.getRate());
        List<MultipartFile> images = List.of(mock(MultipartFile.class));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(orderRepository.findById(orderId)).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> reviewService.saveReview(orderId, authUser, request, images));

        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.getErrorCode());
    }

    @Test
    void saveReview_리뷰저장실패_완료되지않은주문() {
        // given
        long orderId = 1L;
        order = Orders.builder()
                .orderStatus(OrderStatus.NEW) // 주문이 완료되지 않음
                .store(null)
                .user(user)
                .build();

        AuthUser authUser = new AuthUser(user.getId(), user.getEmail(), user.getUserRole());
        ReviewCreateRequest request = new ReviewCreateRequest(review.getContent(), review.getRate());
        List<MultipartFile> images = List.of(mock(MultipartFile.class));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> reviewService.saveReview(orderId, authUser, request, images));

        assertEquals(ErrorCode.REVIEW_ONLY_FOR_COMPLETED_ORDER, exception.getErrorCode());
    }

    @Test
    void saveReview_리뷰저장실패_주문당사자가아닐때() {
        // given
        long orderId = 1L;
        long anotherUserId = 34234L;
        User anotherUser = mock(User.class);
        ReflectionTestUtils.setField(order, "user", anotherUser);

        //주문데이터 세팅
        Store store = mock(Store.class);
        ReflectionTestUtils.setField(order, "store", store);
        given(store.getUser()).willReturn(owner);

        AuthUser authUser = new AuthUser(user.getId(), user.getEmail(), user.getUserRole());
        ReviewCreateRequest request = new ReviewCreateRequest(review.getContent(), review.getRate());
        List<MultipartFile> images = List.of(mock(MultipartFile.class));

        given(anotherUser.getId()).willReturn(anotherUserId);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when & then
        ApplicationException applicationException = assertThrows(ApplicationException.class,
                () -> reviewService.saveReview(orderId, authUser, request, images));
        assertEquals(ErrorCode.Unauthorized_User, applicationException.getErrorCode());
    }

    @Test
    void saveReview_리뷰저장실패_사용자가여러개의리뷰를달때() {
        // given
        long orderId = 1L;

        //주문데이터 세팅
        Store store = mock(Store.class);

        AuthUser authUser = new AuthUser(user.getId(), user.getEmail(), user.getUserRole());
        ReviewCreateRequest request = new ReviewCreateRequest(review.getContent(), review.getRate());
        List<MultipartFile> images = List.of(mock(MultipartFile.class));

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        given(reviewRepository.existsByUserIdAndOrderId(anyLong(), anyLong())).willReturn(true);


        // when & then
        ApplicationException applicationException = assertThrows(ApplicationException.class,
                () -> reviewService.saveReview(orderId, authUser, request, images));
        assertEquals(ErrorCode.DUPLICATE_REVIEW, applicationException.getErrorCode());
    }

    @Test
    void updateReview_리뷰수정성공() {
        // given
        long reviewId = 1L;
        long userId = user.getId();
        ReviewUpdateRequest request = new ReviewUpdateRequest( review.getRate()-1,"update content");
        List<MultipartFile> images = List.of(mock(MultipartFile.class));

        given(reviewRepository.findByIdWithUserAndOrder(reviewId)).willReturn(Optional.of(review));
        doNothing().when(imageService).deleteFile(anyString());
        given(imageService.uploadFile(any(MultipartFile.class))).willReturn(new Image(2L, "updated.jpg", "url"));

        // when
        ReviewResponse response = reviewService.updateReview(reviewId, userId, request, images);

        // then
        assertEquals(request.getContent(), response.getContents());
        assertEquals(request.getRate(), response.getRate());
    }

    @Test
    void updateReview_리뷰수정성공_content가null일때() {
        // given
        long reviewId = 1L;
        long userId = user.getId();
        ReviewUpdateRequest request = new ReviewUpdateRequest( review.getRate()-1,null);
        List<MultipartFile> images = List.of(mock(MultipartFile.class));

        given(reviewRepository.findByIdWithUserAndOrder(reviewId)).willReturn(Optional.of(review));
        doNothing().when(imageService).deleteFile(anyString());
        given(imageService.uploadFile(any(MultipartFile.class))).willReturn(new Image(2L, "updated.jpg", "url"));

        // when
        ReviewResponse response = reviewService.updateReview(reviewId, userId, request, images);

        // then
        assertEquals(review.getContent(), "content");
    }

    @Test
    void updateReview_리뷰수정성공_rate가null일때() {
        // given
        long reviewId = 1L;
        long userId = user.getId();
        ReviewUpdateRequest request = new ReviewUpdateRequest( null,"update content");
        List<MultipartFile> images = List.of(mock(MultipartFile.class));

        given(reviewRepository.findByIdWithUserAndOrder(reviewId)).willReturn(Optional.of(review));
        doNothing().when(imageService).deleteFile(anyString());
        given(imageService.uploadFile(any(MultipartFile.class))).willReturn(new Image(2L, "updated.jpg", "url"));

        // when
        ReviewResponse response = reviewService.updateReview(reviewId, userId, request, images);

        // then
        assertEquals(review.getRate(), 2);
    }

    @Test
    void updateReview_리뷰수정실패_권한없음() {
        // given
        long reviewId = 1L;
        long anotherUserId = 999L;
        ReviewUpdateRequest request = new ReviewUpdateRequest(4,"Updated review");
        List<MultipartFile> images = List.of(mock(MultipartFile.class));

        given(reviewRepository.findByIdWithUserAndOrder(reviewId)).willReturn(Optional.of(review));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> reviewService.updateReview(reviewId, anotherUserId, request, images));

        assertEquals(ErrorCode.Unauthorized_User, exception.getErrorCode());
    }

    @Test
    void updateReview_리뷰수정실패_s3이미지삭제오류() {
        // given
        long reviewId = 1L;
        ReviewUpdateRequest request = new ReviewUpdateRequest(4,"Updated review");
        List<MultipartFile> images = List.of(mock(MultipartFile.class));

        given(reviewRepository.findByIdWithUserAndOrder(anyLong())).willReturn(Optional.of(review));
        willThrow(new ApplicationException(ErrorCode.NOT_FOUND_IMAGE)).given(imageService).deleteFile(anyString());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> reviewService.updateReview(reviewId, user.getId(), request, images));

        assertEquals(ErrorCode.NOT_FOUND_IMAGE, exception.getErrorCode());
    }

    @Test
    void updateReview_리뷰삭제성공() {
        // given
        long reviewId = 1L;
        long userId = user.getId();

        given(reviewRepository.findByIdWithUserAndOrder(anyLong())).willReturn(Optional.of(review));
        doNothing().when(imageService).deleteFile(anyString());

        // when
        reviewService.deleteReview(userId, reviewId);

        // then
        then(reviewRepository).should(times(1)).delete(any(Review.class));
    }

    @Test
    void updateReview_리뷰삭제실패_권한없음() {
        // given
        long reviewId = 1L;
        long anotherUserId = 999L;

        given(reviewRepository.findByIdWithUserAndOrder(reviewId)).willReturn(Optional.of(review));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> reviewService.deleteReview(anotherUserId, reviewId));

        assertEquals(ErrorCode.Unauthorized_User, exception.getErrorCode());
    }

    @Test
    void getReviews_리뷰리스트조회성공(){
        //given
        long storeId = 1L;
        given(reviewRepository.findReviewsByStoreId(anyLong(), anyInt(), anyInt(), any(LocalDateTime.class))).willReturn(List.of(review));
        //when
        List<ReviewResponse> reviews = reviewService.getReviews(storeId, 1, 5, LocalDateTime.now());
        //then
        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals(review.getContent(), reviews.get(0).getContents());
    }
}