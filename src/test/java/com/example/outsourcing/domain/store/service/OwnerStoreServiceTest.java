package com.example.outsourcing.domain.store.service;

import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.category.service.CategoryService;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.exception.*;
import com.example.outsourcing.domain.common.service.ImageService;
import com.example.outsourcing.domain.common.service.KaKaoMapApiService;
import com.example.outsourcing.domain.store.dto.request.StoreNoticeUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreStatusUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.response.StoreNoticeResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreSaveResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreStatusResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreUpdateResponseDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import com.example.outsourcing.domain.user.enums.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class OwnerStoreServiceTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private KaKaoMapApiService kaKaoMapApiService;

    @Mock
    private ImageService imageService;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private OwnerStoreService ownerStoreService;

    @Test
    public void store_등록_성공() {
        // given
        CategoryResponse categoryResponse = new CategoryResponse(1L, "PIZZA");
        Image image = new Image(1L, "image1","qwer1234");
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(37.5665, 126.9780));
        StoreSaveRequestDto request =  StoreSaveRequestDto.builder()
                .imageId(1L)
                .categoryId(1L)
                .storeName("중국집")
                .storeStatus(StoreStatus.OPEN)
                .storeNotice("맛집임")
                .address("서울시 도봉구")
                .minOrderPrice(10000)
                .openTime(LocalTime.of(12,0,0))
                .closeTime(LocalTime.of(23,0,0))
                .build();

        given(categoryService.getCategory(anyLong())).willReturn(categoryResponse);
        given(imageService.getImageById(anyLong())).willReturn(image);
        given(kaKaoMapApiService.getPoint(anyString())).willReturn(point);

        // when
        StoreSaveResponseDto result = ownerStoreService.saveStore(authUser, request);

        // then
        assertNotNull(result);
    }

    @Test
    public void 존재하지_않는_카테고리_ID일_경우_예외_발생() {
        // given
        Long categoryId = 1L;
        given(categoryService.getCategory(anyLong())).willThrow(new ApplicationException(ErrorCode.NOT_FOUND_CATEGORY));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            categoryService.getCategory(categoryId);
        });
        assertEquals(ErrorCode.NOT_FOUND_CATEGORY, exception.getErrorCode());
    }

    @Test
    public void 이미지_저장_실패로_인한_예외_발생() {

    }

    @Test
    public void 주소로_좌표_변환_실패_시_예외_발생 () {
        // given
        String address = "address";

        given(kaKaoMapApiService.getPoint(anyString())).willThrow(new ApplicationException(ErrorCode.INVALID_ADDRESS));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            kaKaoMapApiService.getPoint(address);
        });
        assertEquals(ErrorCode.INVALID_ADDRESS, exception.getErrorCode());
    }

    @Test
    public void 사용자가_가게_개수_제한_3개_초과_시_예외_발생() {
        // given
        CategoryResponse categoryResponse = new CategoryResponse(1L, "PIZZA");
        Image image = new Image(1L, "image1","qwer1234");
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(37.5665, 126.9780));
        StoreSaveRequestDto request = StoreSaveRequestDto.builder()
                .imageId(1L)
                .categoryId(1L)
                .storeName("중국집")
                .storeStatus(StoreStatus.OPEN)
                .storeNotice("맛집임")
                .address("서울시 도봉구")
                .minOrderPrice(10000)
                .openTime(LocalTime.of(12,0,0))
                .closeTime(LocalTime.of(23,0,0))
                .build();

        given(categoryService.getCategory(anyLong())).willReturn(categoryResponse);
        given(imageService.getImageById(anyLong())).willReturn(image);
        given(kaKaoMapApiService.getPoint(anyString())).willReturn(point);
        given(storeRepository.countStoresByUserId(anyLong())).willReturn(3L);

        // when & then
        ApplicationException exception = assertThrows(StoreLimitExceededException.class, () ->
                ownerStoreService.saveStore(authUser, request)  // 실제 예외 발생 코드 실행
        );

        assertEquals(ErrorCode.STORE_LIMIT_EXCEEDED, exception.getErrorCode());
    }

    @Test
    public void 가게_정보_업데이트_성공() {
        // given
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        Image image = new Image(1L, "image1","qwer1234");
        ReflectionTestUtils.setField(image, "id", 1L);
        Category category = Category.builder().categoryName("PIZZA").build();
        ReflectionTestUtils.setField(category, "id", 1L);
        CategoryResponse categoryResponse = CategoryResponse.of(category);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(37.5665, 126.9780));
        Store store = Store.builder()
                .user(user)
                .image(image)
                .category(category)
                .storeName("중국집")
                .storeStatus(StoreStatus.OPEN)
                .storeNotice("맛나요")
                .address("서울시")
                .minOrderPrice(10000)
                .openTime(LocalTime.of(12, 0, 0))
                .closeTime(LocalTime.of(23, 0, 0))
                .location(point)
                .build();

        StoreUpdateRequestDto request = StoreUpdateRequestDto.builder()
                .imageId(1L)
                .categoryId(1L)
                .storeName("중국집")
                .minOrderPrice(10000)
                .openTime(LocalTime.of(12, 0, 0))
                .closeTime(LocalTime.of(23, 0, 0))
                .build();

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(imageService.getImageById(anyLong())).willReturn(image);
        given(categoryService.getCategory(anyLong())).willReturn(categoryResponse);

        // when
        StoreUpdateResponseDto result = ownerStoreService.updateStore(storeId, authUser, request);

        // then
        assertNotNull(result);
        assertEquals(result.getImageId(), 1L);
        assertEquals(result.getCategoryName(), "PIZZA");
        assertEquals(result.getStoreName(), "중국집");
        assertEquals(result.getMinOrderPrice(), 10000);
        assertEquals(result.getOpenTime(), LocalTime.of(12, 0, 0));
        assertEquals(result.getCloseTime(), LocalTime.of(23, 0, 0));
    }

    @Test
    public void 전체_업데이트_중_존재하지_않는_가게_ID일_경우_예외_발생() {
        // given
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        StoreUpdateRequestDto request = StoreUpdateRequestDto.builder()
                .imageId(1L)
                .categoryId(1L)
                .storeName("중국집")
                .minOrderPrice(10000)
                .openTime(LocalTime.of(12, 0, 0))
                .closeTime(LocalTime.of(23, 0, 0))
                .build();

        given(storeRepository.findById(anyLong())).willReturn(Optional.empty());


        // when & then
        NotFoundStoreException exception = assertThrows(NotFoundStoreException.class, () ->
            ownerStoreService.updateStore(storeId, authUser, request));
        assertEquals(ErrorCode.NOT_FOUND_STORE, exception.getErrorCode());
    }

    @Test
    public void 전체_업데이트_중_현재_사용자가_가게_주인이_아닐_경우_예외_발생() {
        // given
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        Image image = new Image(1L, "image1","qwer1234");
        ReflectionTestUtils.setField(image, "id", 1L);
        Category category = Category.builder().categoryName("PIZZA").build();
        ReflectionTestUtils.setField(category, "id", 1L);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(37.5665, 126.9780));
        User newUser = User.builder()
                .email("QWER@1234")
                .password("password")
                .name("HyenHo")
                .point(5)
                .userRole(UserRole.OWNER)
                .userStatus(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(newUser, "id", 2L);
        Store store = Store.builder()
                .user(newUser)
                .image(image)
                .category(category)
                .storeName("중국집")
                .storeStatus(StoreStatus.OPEN)
                .storeNotice("맛나요")
                .address("서울시")
                .minOrderPrice(10000)
                .openTime(LocalTime.of(12, 0, 0))
                .closeTime(LocalTime.of(23, 0, 0))
                .location(point)
                .build();

        StoreUpdateRequestDto request = StoreUpdateRequestDto.builder()
                .imageId(1L)
                .categoryId(1L)
                .storeName("중국집")
                .minOrderPrice(10000)
                .openTime(LocalTime.of(12, 0, 0))
                .closeTime(LocalTime.of(23, 0, 0))
                .build();

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));


        // when & then
        UnauthorizedStoreOwnerException exception = assertThrows(UnauthorizedStoreOwnerException.class, () ->
                ownerStoreService.updateStore(storeId, authUser, request));
        assertEquals(ErrorCode.UNAUTHORIZED_STORE_OWNER, exception.getErrorCode());
    }

    @Test
    public void 상태_업데이트_중_존재하지_않는_가게_ID일_경우_예외_발생() {
        // given
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);

        StoreStatusUpdateRequestDto request = StoreStatusUpdateRequestDto.builder().storeStatus(StoreStatus.OPEN).build();

        given(storeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        NotFoundStoreException exception = assertThrows(NotFoundStoreException.class, () ->
                ownerStoreService.updateStoreStatus(authUser, storeId, request));
        assertEquals(ErrorCode.NOT_FOUND_STORE, exception.getErrorCode());
    }

    @Test
    public void 상태_업데이트_중_현재_사용자가_가게_주인이_아닐_경우_예외_발생() {
        // given
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        Image image = new Image(1L, "image1","qwer1234");
        ReflectionTestUtils.setField(image, "id", 1L);
        Category category = Category.builder().categoryName("PIZZA").build();
        ReflectionTestUtils.setField(category, "id", 1L);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(37.5665, 126.9780));
        User newUser = User.builder()
                .email("QWER@1234")
                .password("password")
                .name("HyenHo")
                .point(5)
                .userRole(UserRole.OWNER)
                .userStatus(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(newUser, "id", 2L);

        Store store = Store.builder()
                .user(newUser)
                .image(image)
                .category(category)
                .storeName("중국집")
                .storeStatus(StoreStatus.OPEN)
                .storeNotice("맛나요")
                .address("서울시")
                .minOrderPrice(10000)
                .openTime(LocalTime.of(12, 0, 0))
                .closeTime(LocalTime.of(23, 0, 0))
                .location(point)
                .build();

        StoreStatusUpdateRequestDto request = StoreStatusUpdateRequestDto.builder().storeStatus(StoreStatus.OPEN).build();

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

        // when & then
        UnauthorizedStoreOwnerException exception = assertThrows(UnauthorizedStoreOwnerException.class, () ->
                ownerStoreService.updateStoreStatus(authUser, storeId ,request));
        assertEquals(ErrorCode.UNAUTHORIZED_STORE_OWNER, exception.getErrorCode());
    }

    @Test
    public void 가게_상태가_이미_동일할_경우_예외_발생() {
        // given
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        Image image = new Image(1L, "image1","qwer1234");
        ReflectionTestUtils.setField(image, "id", 1L);
        Category category = Category.builder().categoryName("PIZZA").build();
        ReflectionTestUtils.setField(category, "id", 1L);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(37.5665, 126.9780));

        Store store = Store.builder()
                .user(user)
                .image(image)
                .category(category)
                .storeName("중국집")
                .storeStatus(StoreStatus.OPEN)
                .storeNotice("맛나요")
                .address("서울시")
                .minOrderPrice(10000)
                .openTime(LocalTime.of(12, 0, 0))
                .closeTime(LocalTime.of(23, 0, 0))
                .location(point)
                .build();

        StoreStatusUpdateRequestDto request = StoreStatusUpdateRequestDto.builder().storeStatus(StoreStatus.OPEN).build();

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

        // when & then
        StoreStatusAlreadySameException exception = assertThrows(StoreStatusAlreadySameException.class, () ->
                ownerStoreService.updateStoreStatus(authUser, storeId ,request));
        assertEquals(ErrorCode.STORE_STATUS_ALREADY_SAME, exception.getErrorCode());
    }

    @Test
    public void 요청된_상태가_OPEN이면_가게_오픈() {
        // given
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        Image image = new Image(1L, "image1","qwer1234");
        ReflectionTestUtils.setField(image, "id", 1L);
        Category category = Category.builder().categoryName("PIZZA").build();
        ReflectionTestUtils.setField(category, "id", 1L);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(37.5665, 126.9780));

        Store store = Store.builder()
                .user(user)
                .image(image)
                .category(category)
                .storeName("중국집")
                .storeStatus(StoreStatus.CLOSE)
                .storeNotice("맛나요")
                .address("서울시")
                .minOrderPrice(10000)
                .openTime(LocalTime.of(12, 0, 0))
                .closeTime(LocalTime.of(23, 0, 0))
                .location(point)
                .build();

        StoreStatusUpdateRequestDto request = StoreStatusUpdateRequestDto.builder().storeStatus(StoreStatus.OPEN).build();

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

        // when
        StoreStatusResponseDto result = ownerStoreService.updateStoreStatus(authUser, storeId ,request);

        // then
        assertNotNull(result);
        assertEquals(result.getStoreStatus(), request.getStoreStatus());
    }

    @Test
    public void 요청된_상태가_CLOSE이면_가게_마감() {
        // given
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        Image image = new Image(1L, "image1","qwer1234");
        ReflectionTestUtils.setField(image, "id", 1L);
        Category category = Category.builder().categoryName("PIZZA").build();
        ReflectionTestUtils.setField(category, "id", 1L);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(37.5665, 126.9780));

        Store store = Store.builder()
                .user(user)
                .image(image)
                .category(category)
                .storeName("중국집")
                .storeStatus(StoreStatus.OPEN)
                .storeNotice("맛나요")
                .address("서울시")
                .minOrderPrice(10000)
                .openTime(LocalTime.of(12, 0, 0))
                .closeTime(LocalTime.of(23, 0, 0))
                .location(point)
                .build();

        StoreStatusUpdateRequestDto request = StoreStatusUpdateRequestDto.builder().storeStatus(StoreStatus.CLOSE).build();

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

        // when
        StoreStatusResponseDto result = ownerStoreService.updateStoreStatus(authUser, storeId ,request);

        // then
        assertNotNull(result);
        assertEquals(result.getStoreStatus(), request.getStoreStatus());
    }

    @Test
    public void 공지_업데이트_성공() {
        // given
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        Image image = new Image(1L, "image1","qwer1234");
        ReflectionTestUtils.setField(image, "id", 1L);
        Category category = Category.builder().categoryName("PIZZA").build();
        ReflectionTestUtils.setField(category, "id", 1L);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(37.5665, 126.9780));

        Store store = Store.builder()
                .user(user)
                .image(image)
                .category(category)
                .storeName("중국집")
                .storeStatus(StoreStatus.OPEN)
                .storeNotice("맛나요")
                .address("서울시")
                .minOrderPrice(10000)
                .openTime(LocalTime.of(12, 0, 0))
                .closeTime(LocalTime.of(23, 0, 0))
                .location(point)
                .build();

        StoreNoticeUpdateRequestDto request = StoreNoticeUpdateRequestDto.builder().storeNotice("공지사항: 오늘부터 배달료 무료").build();

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

        // when
        StoreNoticeResponseDto result = ownerStoreService.updateStoreNotice(authUser, storeId, request);

        // then
        assertNotNull(result);
        assertEquals(result.getStoreNotice(), request.getStoreNotice());
    }

    @Test
    public void 공지_업데이트_중_존재하지_않는_가게_ID일_경우_예외_발생() {
        // given
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);

        StoreNoticeUpdateRequestDto request = StoreNoticeUpdateRequestDto.builder().storeNotice("공지사항: 오늘부터 배달료 무료").build();

        given(storeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        NotFoundStoreException exception = assertThrows(NotFoundStoreException.class, () ->
                ownerStoreService.updateStoreNotice(authUser, storeId, request));
        assertEquals(ErrorCode.NOT_FOUND_STORE, exception.getErrorCode());
    }

    @Test
    public void 공지_업데이트_중_현재_사용자가_가게_주인이_아닐_경우_예외_발생() {
        // given
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        Image image = new Image(1L, "image1","qwer1234");
        ReflectionTestUtils.setField(image, "id", 1L);
        Category category = Category.builder().categoryName("PIZZA").build();
        ReflectionTestUtils.setField(category, "id", 1L);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(37.5665, 126.9780));
        User newUser = User.builder()
                .email("QWER@1234")
                .password("password")
                .name("HyenHo")
                .point(5)
                .userRole(UserRole.OWNER)
                .userStatus(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(newUser, "id", 2L);

        Store store = Store.builder()
                .user(newUser)
                .image(image)
                .category(category)
                .storeName("중국집")
                .storeStatus(StoreStatus.OPEN)
                .storeNotice("맛나요")
                .address("서울시")
                .minOrderPrice(10000)
                .openTime(LocalTime.of(12, 0, 0))
                .closeTime(LocalTime.of(23, 0, 0))
                .location(point)
                .build();

        StoreNoticeUpdateRequestDto request = StoreNoticeUpdateRequestDto.builder().storeNotice("공지사항: 오늘부터 배달료 무료").build();

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

        // when & then
        UnauthorizedStoreOwnerException exception = assertThrows(UnauthorizedStoreOwnerException.class, () ->
                ownerStoreService.updateStoreNotice(authUser, storeId ,request));
        assertEquals(ErrorCode.UNAUTHORIZED_STORE_OWNER, exception.getErrorCode());
    }

    @Test
    public void 가게_폐업_성공() {
        // given
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        ReflectionTestUtils.setField(user, "password", "password");
        Image image = new Image(1L, "image1","qwer1234");
        ReflectionTestUtils.setField(image, "id", 1L);
        Category category = Category.builder().categoryName("PIZZA").build();
        ReflectionTestUtils.setField(category, "id", 1L);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(37.5665, 126.9780));

        Store store = Store.builder()
                .user(user)
                .image(image)
                .category(category)
                .storeName("중국집")
                .storeStatus(StoreStatus.OPEN)
                .storeNotice("맛나요")
                .address("서울시")
                .minOrderPrice(10000)
                .openTime(LocalTime.of(12, 0, 0))
                .closeTime(LocalTime.of(23, 0, 0))
                .location(point)
                .build();

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

        // when
       ownerStoreService.deleteStore(authUser, storeId);

        // then
        assertEquals(store.getStoreStatus(), StoreStatus.SHUTDOWN);
    }

    @Test
    public void 가게_폐업_중_존재하지_않는_가게_ID일_경우_예외_발생() {
        // given
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);

        given(storeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        NotFoundStoreException exception = assertThrows(NotFoundStoreException.class, () ->
                ownerStoreService.deleteStore(authUser, storeId));
        assertEquals(ErrorCode.NOT_FOUND_STORE, exception.getErrorCode());
    }

    @Test
    public void 가게_폐업_중_현재_사용자가_가게_주인이_아닐_경우_예외_발생() {
        // given
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        Image image = new Image(1L, "image1","qwer1234");
        ReflectionTestUtils.setField(image, "id", 1L);
        Category category = Category.builder().categoryName("PIZZA").build();
        ReflectionTestUtils.setField(category, "id", 1L);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(37.5665, 126.9780));
        User newUser = User.builder()
                .email("QWER@1234")
                .password("password")
                .name("HyenHo")
                .point(5)
                .userRole(UserRole.OWNER)
                .userStatus(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(newUser, "id", 2L);

        Store store = Store.builder()
                .user(newUser)
                .image(image)
                .category(category)
                .storeName("중국집")
                .storeStatus(StoreStatus.OPEN)
                .storeNotice("맛나요")
                .address("서울시")
                .minOrderPrice(10000)
                .openTime(LocalTime.of(12, 0, 0))
                .closeTime(LocalTime.of(23, 0, 0))
                .location(point)
                .build();


        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

        // when & then
        UnauthorizedStoreOwnerException exception = assertThrows(UnauthorizedStoreOwnerException.class, () ->
                ownerStoreService.deleteStore(authUser, storeId));
        assertEquals(ErrorCode.UNAUTHORIZED_STORE_OWNER, exception.getErrorCode());
    }
}