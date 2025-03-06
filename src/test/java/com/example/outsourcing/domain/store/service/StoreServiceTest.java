package com.example.outsourcing.domain.store.service;

import com.example.outsourcing.domain.category.dto.response.CategoryResponse;
import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.category.service.CategoryService;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.exception.*;
import com.example.outsourcing.domain.common.service.ImageService;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.common.service.KaKaoMapApiService;
import com.example.outsourcing.domain.common.service.SearchKeywordRankingService;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.service.MenuService;
import com.example.outsourcing.domain.store.dto.request.StoreNoticeUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreStatusUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.response.*;
import com.example.outsourcing.domain.order.enums.OrderStatus;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreResponseForNativeQuery;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.OrderBy;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.entity.UserAddress;
import com.example.outsourcing.domain.user.enums.AddressStatus;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

    @Test
    void searchStore_가게검색성공_검색어null일때(){
        //given
        long userId = 1L;
        Pageable page = PageRequest.of(1,10);
        OrderBy orderBy = OrderBy.DISTANCE;

        UserAddress address = UserAddress.builder()
                .user(null)
                .addressStatus(AddressStatus.DEFAULT)
                .address("address")
                .build();
        Point location = new GeometryFactory().createPoint(new Coordinate(1,2));

        StoreResponseDto storeResponseDto = mock(StoreResponseDto.class);
        given(storeResponseDto.getUserId()).willReturn(userId);
        given(storeResponseDto.getStoreStatus()).willReturn(StoreStatus.OPEN);

        given(userAddressRepository.findByUserIdAndAddressStatus(anyLong(), any(AddressStatus.class))).willReturn(Optional.of(address));
        given(kaKaoMapApiService.getPoint(anyString())).willReturn(location);
        given(storeRepository.findStoresByArea(any(Point.class), any(Pageable.class), any(OrderBy.class), any(OrderStatus.class), any(StoreStatus.class))).willReturn(List.of(storeResponseDto));
        //when
        List<StoreResponseDto> storeResponseDtos = storeService.searchStore(userId, null, page, orderBy);
        //then
        assertEquals(1, storeResponseDtos.size());
        assertEquals(storeResponseDto.getUserId(), storeResponseDtos.get(0).getUserId());
        assertEquals(storeResponseDto.getStoreStatus(), storeResponseDtos.get(0).getStoreStatus());
    }

    @Test
    void searchStore_가게검색성공_검색어없을때(){
        //given
        long userId = 1L;
        Pageable page = PageRequest.of(1,10);
        OrderBy orderBy = OrderBy.DISTANCE;

        UserAddress address = UserAddress.builder()
                .user(null)
                .addressStatus(AddressStatus.DEFAULT)
                .address("address")
                .build();
        Point location = new GeometryFactory().createPoint(new Coordinate(1,2));

        StoreResponseDto storeResponseDto = mock(StoreResponseDto.class);
        given(storeResponseDto.getUserId()).willReturn(userId);
        given(storeResponseDto.getStoreStatus()).willReturn(StoreStatus.OPEN);

        given(userAddressRepository.findByUserIdAndAddressStatus(anyLong(), any(AddressStatus.class))).willReturn(Optional.of(address));
        given(kaKaoMapApiService.getPoint(anyString())).willReturn(location);
        given(storeRepository.findStoresByArea(any(Point.class), any(Pageable.class), any(OrderBy.class), any(OrderStatus.class), any(StoreStatus.class))).willReturn(List.of(storeResponseDto));
        //when
        List<StoreResponseDto> storeResponseDtos = storeService.searchStore(userId, "", page, orderBy);
        //then
        assertEquals(1, storeResponseDtos.size());
        assertEquals(storeResponseDto.getUserId(), storeResponseDtos.get(0).getUserId());
        assertEquals(storeResponseDto.getStoreStatus(), storeResponseDtos.get(0).getStoreStatus());
    }

    @Test
    void searchStore_가게검색성공_검색어있을때_OrderBy_DISTANCE(){
        //given
        String search = "search";
        long userId = 1L;
        Pageable page = PageRequest.of(1,10);
        OrderBy orderBy = OrderBy.DISTANCE;

        UserAddress address = UserAddress.builder()
                .user(null)
                .addressStatus(AddressStatus.DEFAULT)
                .address("address")
                .build();
        Point location = new GeometryFactory().createPoint(new Coordinate(1,2));

        StoreResponseForNativeQuery storeResponseDto = mock(StoreResponseForNativeQuery.class);
        given(storeResponseDto.getUserId()).willReturn(userId);
        given(storeResponseDto.getStoreStatus()).willReturn(StoreStatus.OPEN);

        given(userAddressRepository.findByUserIdAndAddressStatus(anyLong(), any(AddressStatus.class))).willReturn(Optional.of(address));
        given(kaKaoMapApiService.getPoint(anyString())).willReturn(location);
        given(storeRepository.searchOrderByDistance(any(Point.class),anyString() , anyString(), anyString(), anyLong(), anyInt())).willReturn(List.of(storeResponseDto));
        doNothing().when(searchKeywordRankingService).increaseCount(anyString());
        //when
        List<StoreResponseDto> storeResponseDtos = storeService.searchStore(userId, search, page, orderBy);
        //then
        assertEquals(1, storeResponseDtos.size());
        assertEquals(storeResponseDto.getUserId(), storeResponseDtos.get(0).getUserId());
        assertEquals(storeResponseDto.getStoreStatus(), storeResponseDtos.get(0).getStoreStatus());
        verify(searchKeywordRankingService,times(1)).increaseCount(search);
    }
    @Test
    void searchStore_가게검색성공_검색어있을때_OrderBy_RATE(){
        //given
        String search = "search";
        long userId = 1L;
        Pageable page = PageRequest.of(1,10);
        OrderBy orderBy = OrderBy.RATE;

        UserAddress address = UserAddress.builder()
                .user(null)
                .addressStatus(AddressStatus.DEFAULT)
                .address("address")
                .build();
        Point location = new GeometryFactory().createPoint(new Coordinate(1,2));

        StoreResponseForNativeQuery storeResponseDto = mock(StoreResponseForNativeQuery.class);
        given(storeResponseDto.getUserId()).willReturn(userId);
        given(storeResponseDto.getStoreStatus()).willReturn(StoreStatus.OPEN);

        given(userAddressRepository.findByUserIdAndAddressStatus(anyLong(), any(AddressStatus.class))).willReturn(Optional.of(address));
        given(kaKaoMapApiService.getPoint(anyString())).willReturn(location);
        given(storeRepository.searchOrderByRate(any(Point.class),anyString() , anyString(), anyString(), anyLong(), anyInt())).willReturn(List.of(storeResponseDto));
        doNothing().when(searchKeywordRankingService).increaseCount(anyString());
        //when
        List<StoreResponseDto> storeResponseDtos = storeService.searchStore(userId, search, page, orderBy);
        //then
        assertEquals(1, storeResponseDtos.size());
        assertEquals(storeResponseDto.getUserId(), storeResponseDtos.get(0).getUserId());
        assertEquals(storeResponseDto.getStoreStatus(), storeResponseDtos.get(0).getStoreStatus());
        verify(searchKeywordRankingService,times(1)).increaseCount(search);
    }

    @Test
    void searchStore_가게검색성공_검색어가존재하지않는다면인기검색어갱신하지않는다(){
        //given
        String search = "search";
        long userId = 1L;
        Pageable page = PageRequest.of(1,10);
        OrderBy orderBy = OrderBy.RATE;

        UserAddress address = UserAddress.builder()
                .user(null)
                .addressStatus(AddressStatus.DEFAULT)
                .address("address")
                .build();
        Point location = new GeometryFactory().createPoint(new Coordinate(1,2));

        given(userAddressRepository.findByUserIdAndAddressStatus(anyLong(), any(AddressStatus.class))).willReturn(Optional.of(address));
        given(kaKaoMapApiService.getPoint(anyString())).willReturn(location);
        given(storeRepository.searchOrderByRate(any(Point.class),anyString() , anyString(), anyString(), anyLong(), anyInt())).willReturn(List.of());
        //when
        List<StoreResponseDto> storeResponseDtos = storeService.searchStore(userId, search, page, orderBy);
        //then
        assertEquals(0, storeResponseDtos.size());
        verify(searchKeywordRankingService,times(0)).increaseCount(search);
    }

    @Test
    void searchStoreByCategory_카테고리가게검색성공(){
        //given
        long userId = 1L;
        long categoryId = 1L;
        Pageable page = PageRequest.of(1,10);
        OrderBy orderBy = OrderBy.DISTANCE;
        StoreResponseDto storeResponseDto = mock(StoreResponseDto.class);
        UserAddress userAddress = UserAddress.builder()
                .address("address")
                .addressStatus(AddressStatus.DEFAULT)
                .build();
        Point point = mock(Point.class);

        given(storeResponseDto.getUserId()).willReturn(1L);
        given(storeResponseDto.getStoreStatus()).willReturn(StoreStatus.OPEN);
        given(storeResponseDto.getStoreName()).willReturn("store");
        given(userAddressRepository.findByUserIdAndAddressStatus(anyLong(), any(AddressStatus.class))).willReturn(Optional.of(userAddress));
        given(kaKaoMapApiService.getPoint(anyString())).willReturn(point);
        given(storeRepository.findStoresByCategory(any(Point.class), anyLong(), any(Pageable.class),any(OrderBy.class), any(OrderStatus.class), any(StoreStatus.class)))
                .willReturn(List.of(storeResponseDto));
        //when
        List<StoreResponseDto> storeResponseDtos = storeService.searchStoreByCategory(userId, categoryId, page, orderBy);
        //then
        assertEquals(1, storeResponseDtos.size());
        assertEquals(storeResponseDto.getUserId(), storeResponseDtos.get(0).getUserId());
        assertEquals(storeResponseDto.getStoreStatus(), storeResponseDtos.get(0).getStoreStatus());
        assertEquals(storeResponseDto.getStoreName(), storeResponseDtos.get(0).getStoreName());

    }

    @Test
    void searchStoreByCategory_카테고리가게검색실패_사용자의기본주소가없을때(){
        //given
        long userId = 1L;
        long categoryId = 1L;
        Pageable page = PageRequest.of(1,10);
        OrderBy orderBy = OrderBy.DISTANCE;

        given(userAddressRepository.findByUserIdAndAddressStatus(anyLong(), any(AddressStatus.class))).willReturn(Optional.empty());
        //when & then
        ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
            storeService.searchStoreByCategory(userId, categoryId, page, orderBy);
        });
        assertEquals(applicationException.getErrorCode() , ErrorCode.NOT_FOUND_DEFAULT_ADDRESS);

    }

    @Test
    void searchStoreByCategory_카테고리가게검색실패_사용자의기본주소가잘못됐을때(){
        //given
        long userId = 1L;
        long categoryId = 1L;
        Pageable page = PageRequest.of(1,10);
        OrderBy orderBy = OrderBy.DISTANCE;
        UserAddress userAddress = UserAddress.builder()
                .address("address")
                .addressStatus(AddressStatus.DEFAULT)
                .build();


        given(userAddressRepository.findByUserIdAndAddressStatus(anyLong(), any(AddressStatus.class))).willReturn(Optional.of(userAddress));
        given(kaKaoMapApiService.getPoint(anyString())).willThrow(new ApplicationException(ErrorCode.INVALID_ADDRESS));
        //when & then
        ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
            storeService.searchStoreByCategory(userId, categoryId, page, orderBy);
        });
        assertEquals(applicationException.getErrorCode() , ErrorCode.INVALID_ADDRESS);

    }

    @Test
    void findNearStore_가까운가게검색성공(){
        //given
        Point location = mock(Point.class);
        StoreResponseDto storeResponseDto = mock(StoreResponseDto.class);
        given(storeResponseDto.getUserId()).willReturn(1L);
        given(storeResponseDto.getStoreStatus()).willReturn(StoreStatus.OPEN);
        given(storeResponseDto.getStoreName()).willReturn("store");
        given(storeRepository.findTopNearStores(anyInt(), any(Point.class), any(OrderStatus.class), any(StoreStatus.class))).willReturn(List.of(storeResponseDto));
        //when
        List<StoreResponseDto> nearStore = storeService.findNearStore(1, location);
        //then

        assertEquals(1,nearStore.size());
        assertEquals(storeResponseDto.getUserId(),nearStore.get(0).getUserId());
        assertEquals(storeResponseDto.getStoreStatus(),nearStore.get(0).getStoreStatus());
        assertEquals(storeResponseDto.getStoreName(),nearStore.get(0).getStoreName());

    }

    @Test
    void findTopSellerStores_많이팔고있는가게검색성공(){
        //given
        Point location = mock(Point.class);
        StoreResponseDto storeResponseDto = mock(StoreResponseDto.class);
        given(storeResponseDto.getUserId()).willReturn(1L);
        given(storeResponseDto.getStoreStatus()).willReturn(StoreStatus.OPEN);
        given(storeResponseDto.getStoreName()).willReturn("store");
        given(storeRepository.findTopSellerStores(anyInt(), any(Point.class), any(OrderStatus.class), any(StoreStatus.class))).willReturn(List.of(storeResponseDto));
        //when
        List<StoreResponseDto> nearStore = storeService.findTopSellerStores(1, location);
        //then

        assertEquals(1,nearStore.size());
        assertEquals(storeResponseDto.getUserId(),nearStore.get(0).getUserId());
        assertEquals(storeResponseDto.getStoreStatus(),nearStore.get(0).getStoreStatus());
        assertEquals(storeResponseDto.getStoreName(),nearStore.get(0).getStoreName());

    }
}