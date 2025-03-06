package com.example.outsourcing.domain.favorite.service;

import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.favorite.repository.FavoriteRepository;
import com.example.outsourcing.domain.store.dto.response.StoreListResponseDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.service.StoreService;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StoreService storeService;

    @InjectMocks
    private FavoriteService favoriteService;

    @Test
    void 즐겨찾기_추가() {
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
        ReflectionTestUtils.setField(store, "id", storeId);

        given(storeService.getStore(anyLong())).willReturn(store);
        doNothing().when(favoriteRepository).toggleFavorite(any(), any());

        // when
        favoriteService.toggleFavorite(storeId, user);

        // then
        verify(storeService, times(1)).getStore(storeId);
        verify(favoriteRepository, times(1)).toggleFavorite(store, user);
    }

    @Test
    void 즐겨찾기_중_가게_찾기_실패() {
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

        given(storeService.getStore(anyLong())).willThrow(new ApplicationException(ErrorCode.NOT_FOUND_STORE));

        // when
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            favoriteService.toggleFavorite(storeId, user);
        });

        // then
        assertEquals(ErrorCode.NOT_FOUND_STORE, exception.getErrorCode());
        verify(favoriteRepository, times(0)).toggleFavorite(store, user);
    }

    @Test
    void 즐겨찾기한_목록_조회() {
        // given
        Long storeId1 = 1L;
        Long storeId2 = 2L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        Image image = new Image(1L, "image1","qwer1234");
        ReflectionTestUtils.setField(image, "id", 1L);
        Category category = Category.builder().categoryName("PIZZA").build();
        ReflectionTestUtils.setField(category, "id", 1L);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(37.5665, 126.9780));
        Store store1 = Store.builder()
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
        Store store2 = Store.builder()
                .user(user)
                .image(image)
                .category(category)
                .storeName("한식당")
                .storeStatus(StoreStatus.OPEN)
                .storeNotice("맛없어요")
                .address("서울시")
                .minOrderPrice(13000)
                .openTime(LocalTime.of(10, 0, 0))
                .closeTime(LocalTime.of(21, 0, 0))
                .location(point)
                .build();
        ReflectionTestUtils.setField(store1, "id", storeId1);
        ReflectionTestUtils.setField(store2, "id", storeId2);


        List<Store> favoriteStores = List.of(store1, store2);

        given(favoriteRepository.findStoresByUserId(user.getId())).willReturn(favoriteStores);

        // when
        List<StoreListResponseDto> result = favoriteService.getFavorites(user);
        // then
        assertEquals(2, result.size());
        assertEquals(store1.getId(), result.get(0).getId());
        assertEquals(store1.getImage().getImagePath(), result.get(0).getImagePath());
        assertEquals(store1.getCategory().getCategoryName(), result.get(0).getCategoryName());
        assertEquals(store2.getStoreName(), result.get(1).getStoreName());
        assertEquals(store2.getStoreStatus(), result.get(1).getStoreStatus());
        assertEquals(store2.getMinOrderPrice(), result.get(1).getMinOrderPrice());
    }
}