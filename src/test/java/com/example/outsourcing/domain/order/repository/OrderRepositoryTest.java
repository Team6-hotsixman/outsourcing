package com.example.outsourcing.domain.order.repository;

import com.example.outsourcing.domain.order.entity.Orders;
import com.example.outsourcing.domain.order.enums.OrderStatus;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void 유저_아이디로_주문_리스트를_조회할_수_있다() {
        // given
        Long userId = 1L;
        User user = new User(userId);
        userRepository.save(user);

        Store store = new Store();
        storeRepository.save(store);

        Orders order1 = new Orders(1000, 100, LocalDateTime.now(), OrderStatus.NEW, store, user);
        Orders order2 = new Orders(2000, 100, LocalDateTime.now(), OrderStatus.NEW, store, user);
        orderRepository.save(order1);
        orderRepository.save(order2);
        // when
        List<Orders> result = orderRepository.findAllByUserId(user.getId());
        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getUser().getId()).isEqualTo(userId);
        assertThat(result.get(1).getUser().getId()).isEqualTo(userId);
    }
}