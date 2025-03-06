package com.example.outsourcing.domain.order.service;

import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.coupon.repository.UserCouponRepository;
import com.example.outsourcing.domain.menu.menuoption.repository.MenuOptionRepository;
import com.example.outsourcing.domain.menu.repository.MenuRepository;
import com.example.outsourcing.domain.order.entity.Orders;
import com.example.outsourcing.domain.order.enums.OrderStatus;
import com.example.outsourcing.domain.order.repository.OrderItemOptionRepository;
import com.example.outsourcing.domain.order.repository.OrderItemRepository;
import com.example.outsourcing.domain.order.repository.OrderRepository;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void updateOrderStatus() {
    }

    @Test
    void 주문상태_NEW_주문을_취소할_수_있다() {
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
    void 유저가_존재하지_않을_때_예외처리() {
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
    void 가게가_존재하지_않을_때_예외처리() {
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
    void 주문이_존재하지_않을_때_예외처리() {
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
    void 주문이_해당_가게에서_발생한_주문이_아닌_경우_예외처리() {
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
    void 주문을_요청하지않은_고객이_주문을_취소할_때_예외처리() {
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
    void 가게에서_주문을_수락한_이후_주문을_취소할_때_예외처리() {
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