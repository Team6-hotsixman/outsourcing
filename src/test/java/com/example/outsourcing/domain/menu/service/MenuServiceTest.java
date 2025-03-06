package com.example.outsourcing.domain.menu.service;

import com.example.outsourcing.domain.category.entity.Category;
import com.example.outsourcing.domain.category.repository.CategoryRepository;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.repository.ImageRepository;
import com.example.outsourcing.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcing.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcing.domain.menu.dto.response.MenuResponseDto;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.repository.MenuRepository;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import com.example.outsourcing.domain.user.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private MenuService menuService;

    private Store store;
    private Category category;
    private Image image;
    private Menu menu;

    GeometryFactory geometryFactory = new GeometryFactory();
    Coordinate coordinate = new Coordinate(127.035, 37.492);

    @BeforeEach
    void setting() {
        User user = User.builder()
                .email("a1@naver.com")
                .point(100)
                .userRole(UserRole.OWNER)
                .userStatus(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .name("스파르타")
                .password("123456QW")
                .modifiedAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        category = Category.builder()
                .categoryName("Test Category")
                .build();
        ReflectionTestUtils.setField(category, "id", 1L);


        image = Image.builder()
                .filename("test.jpg")
                .imagePath("/images/test.jpg")
                .build();
        image.setId(1L);
        ReflectionTestUtils.setField(image, "id", 1L);


        store = Store.builder()
                .user(user)
                .category(category)
                .image(image)
                .storeName("Test Store")
                .storeStatus(StoreStatus.OPEN)
                .storeNotice("Welcome")
                .address("123 Test St")
                .minOrderPrice(1000)
                .openTime(LocalTime.of(9, 0))
                .closeTime(LocalTime.of(22, 0))
                .location(geometryFactory.createPoint(coordinate))
                .build();


        menu = Menu.builder()
                .menuName("Test Menu")
                .price(1000)
                .description("Test Description")
                .store(store)
                .category(category)
                .image(image)
                .isAvailable(true)
                .build();
        ReflectionTestUtils.setField(menu, "id", 1L);
    }

    @Test
    void 메뉴_저장_테스트() {
        // given
        MenuSaveRequestDto requestDto = new MenuSaveRequestDto("testMenu", 1000, menu.getDescription(), 1L, 1L, 1L);
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(imageRepository.findById(1L)).willReturn(Optional.of(image));
        given(menuRepository.save(any(Menu.class))).willReturn(menu);

        // when
        MenuResponseDto result = menuService.saveMenu(requestDto);

        // then
        assertThat(result.getMenuName()).isEqualTo("testMenu");
        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    void 메뉴_수정_테스트() {
        // given
        MenuUpdateRequestDto requestDto = new MenuUpdateRequestDto("updatedMenu", 2000, menu.getDescription(),1L,true, 1L, 1L);
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(imageRepository.findById(1L)).willReturn(Optional.of(image));

        // when
        MenuResponseDto result = menuService.updateMenu(1L, requestDto);

        // then
        assertThat(result.getMenuName()).isEqualTo("updatedMenu");
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void 메뉴_삭제_테스트() {
        // given
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));
    }

}