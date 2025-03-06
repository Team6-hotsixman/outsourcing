package com.example.outsourcing.domain.order.service;

import com.example.outsourcing.domain.common.annotation.Auth;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.coupon.repository.UserCouponRepository;
import com.example.outsourcing.domain.menu.menuoption.repository.MenuOptionRepository;
import com.example.outsourcing.domain.menu.repository.MenuRepository;
import com.example.outsourcing.domain.order.dto.request.OrderStatusRequestDto;
import com.example.outsourcing.domain.order.dto.response.OrderSimpleResponseDto;
import com.example.outsourcing.domain.order.entity.Orders;
import com.example.outsourcing.domain.order.enums.OrderStatus;
import com.example.outsourcing.domain.order.repository.OrderItemOptionRepository;
import com.example.outsourcing.domain.order.repository.OrderItemRepository;
import com.example.outsourcing.domain.order.repository.OrderRepository;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import com.example.outsourcing.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuOptionRepository menuOptionRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderItemOptionRepository orderItemOptionRepository;
    @Mock
    private UserCouponRepository userCouponRepository;
    @InjectMocks
    private OrderService orderService;

    @Test
    void 주문_생성_성공() {

    }

    @Test
    void findAll() {
    }

    @Test
    void findOne() {
    }

    @Test
    void 가게_사장님은_주문을_승인할_수_있다() {
        // NEW -> COOKING
        // given
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);
        Long userId = 2L;
        Long storeId = 1L;
        Long orderId = 1L;
        OrderStatusRequestDto requestDto = new OrderStatusRequestDto("COOKING");

        User owner = User.fromAuthUser(authUser);
        User customer = new User(userId);
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "user", owner);
        Orders order = new Orders(10000, 0, LocalDateTime.now(), OrderStatus.NEW, store, customer);
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        OrderSimpleResponseDto response = orderService.updateOrderStatus(authUser, storeId, orderId, requestDto);

        // then
        assertNotNull(response);
        assertEquals(OrderStatus.COOKING, order.getOrderStatus());
    }

    @Test
    void 가게_사장님은_주문을_거절할_수_있다() {
        // NEW -> REJECTED
        // given
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);
        Long userId = 2L;
        Long storeId = 1L;
        Long orderId = 1L;
        OrderStatusRequestDto requestDto = new OrderStatusRequestDto("REJECTED");

        User owner = User.fromAuthUser(authUser);
        User customer = new User(userId);
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "user", owner);
        Orders order = new Orders(10000, 0, LocalDateTime.now(), OrderStatus.NEW, store, customer);
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        OrderSimpleResponseDto response = orderService.updateOrderStatus(authUser, storeId, orderId, requestDto);

        // then
        assertNotNull(response);
        assertEquals(OrderStatus.REJECTED, order.getOrderStatus());
    }

    @Test
    void 가게_사장님은_주문의_상태를_배달중으로_수정할_수_있다() {
        // COOKING -> DELIVERING
        // given
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);
        Long userId = 2L;
        Long storeId = 1L;
        Long orderId = 1L;
        OrderStatusRequestDto requestDto = new OrderStatusRequestDto("DELIVERING");

        User owner = User.fromAuthUser(authUser);
        User customer = new User(userId);
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "user", owner);
        Orders order = new Orders(10000, 0, LocalDateTime.now(), OrderStatus.COOKING, store, customer);
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        OrderSimpleResponseDto response = orderService.updateOrderStatus(authUser, storeId, orderId, requestDto);

        // then
        assertNotNull(response);
        assertEquals(OrderStatus.DELIVERING, order.getOrderStatus());
    }

    @Test
    void 가게_사장님은_주문의_상태를_배달완료로_수정할_수_있다() {
        // DELIVERING -> COMPLETED
        // given
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);
        Long userId = 2L;
        Integer point = 0;
        Long storeId = 1L;
        Long orderId = 1L;
        Integer totalPriceAmount = 10000;
        OrderStatusRequestDto requestDto = new OrderStatusRequestDto("COMPLETED");

        User owner = User.fromAuthUser(authUser);
        User customer = new User(userId, point);
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "user", owner);
        Orders order = new Orders(totalPriceAmount, 0, LocalDateTime.now(), OrderStatus.DELIVERING, store, customer);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(customer));
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        OrderSimpleResponseDto response = orderService.updateOrderStatus(authUser, storeId, orderId, requestDto);

        // then
        assertNotNull(response);
        assertEquals(OrderStatus.COMPLETED, order.getOrderStatus());
        assertEquals(300, customer.getPoint()); // point + (totalPriceAmount * 0.03)
    }

    @Test
    void 가게가_존재하지_않을_때_주문상태_변경_메소드의_예외처리() {
        // given
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);
        Long userId = 2L;
        Long storeId = 1L;
        Long orderId = 1L;
        OrderStatusRequestDto requestDto = new OrderStatusRequestDto("COOKING");

        User owner = User.fromAuthUser(authUser);
        User customer = new User(userId, 0);
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "user", owner);
        Orders order = new Orders(10000, 0, LocalDateTime.now(), OrderStatus.NEW, store, customer);
        given(storeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> orderService.updateOrderStatus(authUser, storeId, orderId, requestDto)
        );
        Assertions.assertEquals(ErrorCode.NOT_FOUND_STORE, exception.getErrorCode());
        assertEquals(OrderStatus.NEW, order.getOrderStatus()); // 주문 상태가 변경되지 않아야 함.
    }

    @Test
    void 주문이_존재하지_않을_때_주문상태_변경_메소드의_예외처리() {
        // given
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);
        Long userId = 2L;
        Long storeId = 1L;
        Long orderId = 1L;
        OrderStatusRequestDto requestDto = new OrderStatusRequestDto("COOKING");

        User owner = User.fromAuthUser(authUser);
        User customer = new User(userId, 0);
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "user", owner);
        Orders order = new Orders(10000, 0, LocalDateTime.now(), OrderStatus.NEW, store, customer);
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> orderService.updateOrderStatus(authUser, storeId, orderId, requestDto)
        );
        Assertions.assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.getErrorCode());
        assertEquals(OrderStatus.NEW, order.getOrderStatus()); // 주문 상태가 변경되지 않아야 함.
    }

    @Test
    void 주문이_해당_가게에서_발생한_주문이_아닌_경우_주문상태_변경_메소드의_예외처리() {
        // given
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);
        Long userId = 2L;
        Long storeId1 = 1L;
        Long storeId2 = 2L;
        Long orderId = 1L;
        OrderStatusRequestDto requestDto = new OrderStatusRequestDto("COOKING");

        User owner = User.fromAuthUser(authUser);
        User customer = new User(userId, 0);
        Store store = new Store(); // 주문하지 않은 가게
        ReflectionTestUtils.setField(store, "id", storeId1);
        ReflectionTestUtils.setField(store, "user", owner);
        Store orderStore = new Store(); // 실제 주문이 이뤄진 가게
        ReflectionTestUtils.setField(store, "id", storeId2);
        ReflectionTestUtils.setField(store, "user", owner);
        Orders order = new Orders(10000, 0, LocalDateTime.now(), OrderStatus.NEW, orderStore, customer);
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> orderService.updateOrderStatus(authUser, storeId1, orderId, requestDto)
        );
        Assertions.assertEquals(ErrorCode.MISMATCHED_ORDER_WITH_STORE, exception.getErrorCode());
        assertEquals(OrderStatus.NEW, order.getOrderStatus()); // 주문 상태가 변경되지 않아야 함.
    }

    @Test
    void 해당_주문의_가게_사장님이_아닌_경우_주문상태_변경_메소드의_예외처리() {
        // given
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);
        AuthUser orderAuthUser = new AuthUser(2L, "email", UserRole.OWNER);
        Long userId = 2L;
        Long storeId = 1L;
        Long orderId = 1L;
        OrderStatusRequestDto requestDto = new OrderStatusRequestDto("COOKING");

        User orderOwner = User.fromAuthUser(orderAuthUser); //해당 주문의 가게 사장님
        User customer = new User(userId, 0);
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "user", orderOwner);
        Orders order = new Orders(10000, 0, LocalDateTime.now(), OrderStatus.NEW, store, customer);
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> orderService.updateOrderStatus(authUser, storeId, orderId, requestDto)
        );
        Assertions.assertEquals(ErrorCode.UNAUTHORIZED_STORE_OWNER, exception.getErrorCode());
        assertEquals(OrderStatus.NEW, order.getOrderStatus()); // 주문 상태가 변경되지 않아야 함.
    }

    @Test
    void 현재_주문의_상태가_변경하려는_상태와_경우_주문상태_변경_메소드의_예외처리() {
        // given
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);
        Long userId = 2L;
        Long storeId = 1L;
        Long orderId = 1L;
        OrderStatusRequestDto requestDto = new OrderStatusRequestDto("NEW");

        User orderOwner = User.fromAuthUser(authUser);
        User customer = new User(userId, 0);
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "user", orderOwner);
        Orders order = new Orders(10000, 0, LocalDateTime.now(), OrderStatus.NEW, store, customer);
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> orderService.updateOrderStatus(authUser, storeId, orderId, requestDto)
        );
        Assertions.assertEquals(ErrorCode.ORDER_STATUS_ALREADY_SAME, exception.getErrorCode());
        assertEquals(OrderStatus.NEW, order.getOrderStatus()); // 주문 상태가 변경되지 않아야 함.
    }

    @Test
    void 거절된_주문의_주문상태_변경_메소드의_예외처리() {
        // given
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);
        Long userId = 2L;
        Long storeId = 1L;
        Long orderId = 1L;
        OrderStatusRequestDto requestDto = new OrderStatusRequestDto("NEW");

        User orderOwner = User.fromAuthUser(authUser);
        User customer = new User(userId, 0);
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "user", orderOwner);
        Orders order = new Orders(10000, 0, LocalDateTime.now(), OrderStatus.REJECTED, store, customer);
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> orderService.updateOrderStatus(authUser, storeId, orderId, requestDto)
        );
        Assertions.assertEquals(ErrorCode.CANT_UPDATE_ORDER_STATUS, exception.getErrorCode());
        assertEquals(OrderStatus.REJECTED, order.getOrderStatus()); // 주문 상태가 변경되지 않아야 함.
    }

    @Test
    void 완료된_주문의_주문상태_변경_메소드의_예외처리() {
        // given
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);
        Long userId = 2L;
        Long storeId = 1L;
        Long orderId = 1L;
        OrderStatusRequestDto requestDto = new OrderStatusRequestDto("NEW");

        User orderOwner = User.fromAuthUser(authUser);
        User customer = new User(userId, 0);
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "user", orderOwner);
        Orders order = new Orders(10000, 0, LocalDateTime.now(), OrderStatus.COMPLETED, store, customer);
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> orderService.updateOrderStatus(authUser, storeId, orderId, requestDto)
        );
        Assertions.assertEquals(ErrorCode.CANT_UPDATE_ORDER_STATUS, exception.getErrorCode());
        assertEquals(OrderStatus.COMPLETED, order.getOrderStatus()); // 주문 상태가 변경되지 않아야 함.
    }

    @Test
    void 올바르지_않은_주문상태_변경_메소드의_예외처리() {
        // OrderStatus : NEW -> COOKING -> DELIVERING -> COMPLETED 또는
        // NEW -> REJECTED 로만 변경할 수 있도록 예외처리
        // given
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);
        Long userId = 2L;
        Long storeId = 1L;
        Long orderId = 1L;
        OrderStatusRequestDto requestDto = new OrderStatusRequestDto("NEW");

        User orderOwner = User.fromAuthUser(authUser);
        User customer = new User(userId, 0);
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "user", orderOwner);
        Orders order = new Orders(10000, 0, LocalDateTime.now(), OrderStatus.DELIVERING, store, customer);
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> orderService.updateOrderStatus(authUser, storeId, orderId, requestDto)
        );
        Assertions.assertEquals(ErrorCode.MISMATCHED_ORDER_STATUS, exception.getErrorCode());
        assertEquals(OrderStatus.DELIVERING, order.getOrderStatus()); // 주문 상태가 변경되지 않아야 함.
    }

    @Test
    void 주문상태_NEW인_주문을_취소할_수_있다() {
        // given
        Long userId = 1L;
        Long orderId = 1L;
        Long storeId = 1L;

        User user = new User(userId);
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        Orders order = new Orders(10000,0, LocalDateTime.now(), OrderStatus.NEW, store, user);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        doNothing().when(orderRepository).deleteById(anyLong());

        // when
        orderService.cancelOrder(userId, orderId, storeId);

        // then
        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void 유저가_존재하지_않을_때_주문취소_메소드의_예외처리() {
        // given
        Long userId = 1L;
        Long orderId = 1L;
        Long storeId = 1L;

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> orderService.cancelOrder(userId, orderId, storeId)
                );
        Assertions.assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
        verify(orderRepository, never()).deleteById(orderId);
    }

    @Test
    void 가게가_존재하지_않을_때_주문취소_메소드의_예외처리() {
        // given
        Long userId = 1L;
        Long orderId = 1L;
        Long storeId = 1L;

        User user = new User(userId);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(storeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> orderService.cancelOrder(userId, orderId, storeId)
        );
        Assertions.assertEquals(ErrorCode.NOT_FOUND_STORE, exception.getErrorCode());
        verify(orderRepository, never()).deleteById(orderId);
    }

    @Test
    void 주문이_존재하지_않을_때_주문취소_메소드의_예외처리() {
        // given
        Long userId = 1L;
        Long orderId = 1L;
        Long storeId = 1L;

        User user = new User(userId);
        Store store = new Store(); // 주문 취소 요청한 가게
        ReflectionTestUtils.setField(store, "id", storeId);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> orderService.cancelOrder(userId, orderId, storeId)
        );
        Assertions.assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.getErrorCode());
        verify(orderRepository, never()).deleteById(orderId);
    }

    @Test
    void 주문이_해당_가게에서_발생한_주문이_아닌_경우_주문취소_메소드의_예외처리() {
        // given
        Long userId = 1L;
        Long orderId = 1L;
        Long storeId1 = 1L;
        Long storeId2 = 2L;

        User user = new User(userId);
        Store store = new Store(); // 주문 취소 요청한 가게
        ReflectionTestUtils.setField(store, "id", storeId1);
        Store orderStore = new Store(); // 실제 주문한 가게
        ReflectionTestUtils.setField(store, "id", storeId2);
        Orders order = new Orders(10000,0, LocalDateTime.now(), OrderStatus.NEW, orderStore, user);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> orderService.cancelOrder(userId, orderId, storeId1)
        );
        Assertions.assertEquals(ErrorCode.MISMATCHED_ORDER_WITH_STORE, exception.getErrorCode());
        verify(orderRepository, never()).deleteById(orderId);
    }

    @Test
    void 주문을_요청하지않은_고객이_주문을_취소할_때_주문취소_메소드의_예외처리() {
        // given
        Long userId1 = 1L;
        Long userId2 = 2L;
        Long orderId = 1L;
        Long storeId = 1L;

        User user = new User(userId1); // 주문 취소 요청한 사용자
        User orderUser = new User(userId2); // 실제 주문한 사용자
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        Orders order = new Orders(10000,0, LocalDateTime.now(), OrderStatus.NEW, store, orderUser);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> orderService.cancelOrder(userId1, orderId, storeId)
        );
        Assertions.assertEquals(ErrorCode.MISMATCHED_ORDER_WITH_USER, exception.getErrorCode());
        verify(orderRepository, never()).deleteById(orderId);
    }

    @Test
    void 가게에서_주문을_수락한_이후_주문을_취소할_때_주문취소_메소드의_예외처리() {
        // given
        Long userId = 1L;
        Long orderId = 1L;
        Long storeId = 1L;

        User user = new User(userId);
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        Orders order = new Orders(10000,0, LocalDateTime.now(), OrderStatus.COOKING, store, user);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> orderService.cancelOrder(userId, orderId, storeId)
        );
        Assertions.assertEquals(ErrorCode.CANT_CANCEL_AFTER_COOKING, exception.getErrorCode());
        verify(orderRepository, never()).deleteById(orderId);
    }
}