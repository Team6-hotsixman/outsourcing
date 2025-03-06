package com.example.outsourcing.domain.store.service;

import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.category.service.CategoryService;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.exception.*;
import com.example.outsourcing.domain.common.service.ImageService;
import com.example.outsourcing.domain.common.service.KaKaoMapApiService;
import com.example.outsourcing.domain.common.service.SearchKeywordRankingService;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.service.MenuService;
import com.example.outsourcing.domain.store.dto.request.StoreNoticeUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreStatusUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.response.*;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import com.example.outsourcing.domain.user.enums.UserStatus;
import com.example.outsourcing.domain.user.repository.UserAddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {
    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MenuService menuService;

    @Mock
    private KaKaoMapApiService kaKaoMapApiService;

    @Mock
    private UserAddressRepository userAddressRepository;

    @Mock
    private SearchKeywordRankingService searchKeywordRankingService;

    @InjectMocks
    private StoreService storeService;

    @Test
    void 단일_가게_조회_성공() {
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
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

        // when
        Store result = storeService.getStore(storeId);

        // then
        assertEquals(10000, result.getMinOrderPrice());
        assertEquals(store.getId(), result.getId());
        assertEquals(store.getUser().getId(), result.getUser().getId());
    }

    @Test
    void 단일_가게_조회_실패() {
        // given
        Long storeId = 1L;

        given(storeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            storeService.getStoreAndMenu(storeId);
        });

        // then
        assertEquals(ErrorCode.NOT_FOUND_STORE, exception.getErrorCode());
    }

    @Test
    void 단일_가게_정보와_해당_가게_메뉴_조회() {
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
        Menu menu1 = Menu.builder()
                .menuName("짜장면")
                .price(7000)
                .description("국수 요리")
                .isAvailable(true)
                .store(store)
                .image(image)
                .category(category)
                .build();
        Menu menu2 = Menu.builder()
                .menuName("짬뽕")
                .price(8000)
                .description("국수 요리")
                .isAvailable(true)
                .store(store)
                .image(image)
                .category(category)
                .build();
        List<Menu> list = List.of(menu1,menu2);

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(menuService.getMenus(anyLong())).willReturn(list);

        // when
        StoreResponseDto result = storeService.getStoreAndMenu(storeId);

        // then
        assertEquals(2, result.getMenus().size());
        assertEquals(store.getId(), result.getId());
        assertEquals(store.getUser().getId(), result.getUserId());
    }

    @Test
    void 가게와_메뉴_조회_중_가게_조회_실패() {
        // given
        Long storeId = 1L;

        given(storeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            storeService.getStoreAndMenu(storeId);
        });

        // then
        assertEquals(ErrorCode.NOT_FOUND_STORE, exception.getErrorCode());
    }
}