package com.example.outsourcing.domain.statistics.service;

import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.order.service.OrderService;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsCountResponseDto;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsPriceResponseDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.service.StoreService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {
    @Mock
    private OrderService orderService;

    @Mock
    private StoreService storeService;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void 월별_주문_횟수_통계_조회() {
        // given
        String date = "2025-03";
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
        ReflectionTestUtils.setField(store, "id", storeId);

        StatisticsCountResponseDto response = new StatisticsCountResponseDto(store.getStoreName(), 3L);

        given(storeService.getStore(anyLong())).willReturn(store);
        given(orderService.getCountOrdersByMonth(anyLong(),any(), any())).willReturn(response);

        // when
        StatisticsCountResponseDto result = statisticsService.getCountOrdersByStore(date, storeId, authUser);

        // then
        assertNotNull(result);
        assertEquals(response.getOrderCount(), 3);
        verify(orderService, times(1)).getCountOrdersByMonth(anyLong(), any(), any());
    }

    @Test
    void 일별_주문_횟수_통계_조회() {
        // given
        String date = "2025-03-06";
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
        ReflectionTestUtils.setField(store, "id", storeId);

        StatisticsCountResponseDto response = new StatisticsCountResponseDto(store.getStoreName(), 3L);

        given(storeService.getStore(anyLong())).willReturn(store);
        given(orderService.getCountOrdersByDay(anyLong(),any())).willReturn(response);

        // when
        StatisticsCountResponseDto result = statisticsService.getCountOrdersByStore(date, storeId, authUser);

        // then
        assertNotNull(result);
        assertEquals(response.getOrderCount(), 3);
        verify(orderService, times(1)).getCountOrdersByDay(anyLong(), any());
    }

    @Test
    void  주문_횟수_통계_조회_중_가게_찾기_실패() {
        // given
        String date = "2025-03-06";
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);

        given(storeService.getStore(anyLong())).willThrow(new ApplicationException(ErrorCode.NOT_FOUND_STORE));

        // when
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
                    statisticsService.getCountOrdersByStore(date, storeId, authUser);
                });

        // then
        assertEquals(ErrorCode.NOT_FOUND_STORE, exception.getErrorCode());
        verify(orderService, times(0)).getCountOrdersByDay(anyLong(), any());
    }

    @Test
    void 주문_횟수_통계_조회_중_가게_주인_아님() {
        // given
        String date = "2025-03-06";
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        User user = User.fromAuthUser(authUser);
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
        ReflectionTestUtils.setField(store, "id", storeId);

        given(storeService.getStore(anyLong())).willReturn(store);

        // when
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            statisticsService.getCountOrdersByStore(date, storeId, authUser);
        });
        // then
        assertEquals(ErrorCode.UNAUTHORIZED_STORE_OWNER, exception.getErrorCode());
        verify(orderService, times(0)).getCountOrdersByDay(anyLong(), any());
    }

    @Test
    void 주문_횟수_통계_조회_중_날짜_포맷_미스매치() {
        // given
        String date = "2025-03-06-23:00";
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
        ReflectionTestUtils.setField(store, "id", storeId);

        given(storeService.getStore(anyLong())).willReturn(store);

        // when
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            statisticsService.getCountOrdersByStore(date, storeId, authUser);
        });
        // then
        assertEquals(ErrorCode.INVALID_DATE_FORMAT, exception.getErrorCode());
        verify(orderService, times(0)).getCountOrdersByDay(anyLong(), any());
    }


    @Test
    void 월별_매출_통계_조회() {
        // given
        String date = "2025-03";
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
        ReflectionTestUtils.setField(store, "id", storeId);

        StatisticsPriceResponseDto response = new StatisticsPriceResponseDto(store.getStoreName(), 200000L);

        given(storeService.getStore(anyLong())).willReturn(store);
        given(orderService.getTotalRevenueByMonth(anyLong(),any(), any())).willReturn(response);

        // when
        StatisticsPriceResponseDto result = statisticsService.getTotalRevenueByStore(date, storeId, authUser);

        // then
        assertNotNull(result);
        assertEquals(response.getTotalPrice(), 200000);
        verify(orderService, times(1)).getTotalRevenueByMonth(anyLong(), any(), any());
    }

    @Test
    void 일별_매출_통계_조회() {
        // given
        String date = "2025-03-10";
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
        ReflectionTestUtils.setField(store, "id", storeId);

        StatisticsPriceResponseDto response = new StatisticsPriceResponseDto(store.getStoreName(), 200000L);

        given(storeService.getStore(anyLong())).willReturn(store);
        given(orderService.getTotalRevenueByDay(anyLong(),any())).willReturn(response);

        // when
        StatisticsPriceResponseDto result = statisticsService.getTotalRevenueByStore(date, storeId, authUser);

        // then
        assertNotNull(result);
        assertEquals(response.getTotalPrice(), 200000);
        verify(orderService, times(1)).getTotalRevenueByDay(anyLong(), any());
    }

    @Test
    void  매출_통계_조회_중_가게_찾기_실패() {
        // given
        String date = "2025-03-06";
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);

        given(storeService.getStore(anyLong())).willThrow(new ApplicationException(ErrorCode.NOT_FOUND_STORE));

        // when
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            statisticsService.getTotalRevenueByStore(date, storeId, authUser);
        });

        // then
        assertEquals(ErrorCode.NOT_FOUND_STORE, exception.getErrorCode());
        verify(orderService, times(0)).getTotalRevenueByDay(anyLong(), any());
    }

    @Test
    void 매출_통계_조회_중_가게_주인_아님() {
        // given
        String date = "2025-03-06";
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        User user = User.fromAuthUser(authUser);
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
        ReflectionTestUtils.setField(store, "id", storeId);

        given(storeService.getStore(anyLong())).willReturn(store);

        // when
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            statisticsService.getTotalRevenueByStore(date, storeId, authUser);
        });
        // then
        assertEquals(ErrorCode.UNAUTHORIZED_STORE_OWNER, exception.getErrorCode());
        verify(orderService, times(0)).getTotalRevenueByDay(anyLong(), any());
    }

    @Test
    void 매출_통계_조회_중_날짜_포맷_미스매치() {
        // given
        String date = "2025-03-06-23:00";
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
        ReflectionTestUtils.setField(store, "id", storeId);

        given(storeService.getStore(anyLong())).willReturn(store);

        // when
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            statisticsService.getTotalRevenueByStore(date, storeId, authUser);
        });
        // then
        assertEquals(ErrorCode.INVALID_DATE_FORMAT, exception.getErrorCode());
        verify(orderService, times(0)).getTotalRevenueByDay(anyLong(), any());
    }



}