package com.example.outsourcing.domain.order.service;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

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
        Store store = new Store(storeId);
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
}