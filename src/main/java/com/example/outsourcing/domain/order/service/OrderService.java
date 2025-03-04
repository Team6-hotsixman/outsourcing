package com.example.outsourcing.domain.order.service;

import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.repository.MenuRepository;
import com.example.outsourcing.domain.order.dto.OrderItemRequestDto;
import com.example.outsourcing.domain.order.dto.OrderRequestDto;
import com.example.outsourcing.domain.order.dto.OrderResponseDto;
import com.example.outsourcing.domain.order.entity.OrderItem;
import com.example.outsourcing.domain.order.entity.Orders;
import com.example.outsourcing.domain.order.enums.OrderStatus;
import com.example.outsourcing.domain.order.repository.OrderItemRepository;
import com.example.outsourcing.domain.order.repository.OrderRepository;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    //todo 이후 UserService를 통해 접근할 수 있도록 수정 예정
    private final UserRepository userRepository;
    //todo 이후 StoreService를 통해 접근할 수 있도록 수정 예정
    private final StoreRepository storeRepository;
    //todo 이후 MenuService를 통해 접근할 수 있도록 수정 예정
    private final MenuRepository menuRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderResponseDto placeOrder(
            Long userId,
            Long storeId,
            OrderRequestDto requestDto
    ) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_STORE)
        );

        //가게 오픈시에만 주문 가능
        if (!store.getStoreStatus().equals(StoreStatus.OPEN)) {
            throw new ApplicationException(ErrorCode.NOT_OPENED_STORE);
        }

        //주문 금액 계산
        Integer totalPriceAmount = 0;
        for (OrderItemRequestDto orderItem : requestDto.getOrderItems()) {
            Menu menu = menuRepository.findById(orderItem.getMenuId()).orElseThrow(
                    () -> new ApplicationException(ErrorCode.NOT_FOUND_MENU)
            );
            totalPriceAmount += (menu.getPrice() * orderItem.getQuantity());
        }

        //최소 주문 금액 확인
        if (totalPriceAmount < store.getMinOrderPrice()) {
            throw new ApplicationException(ErrorCode.LESS_THAN_MIN_ORDER_PRICE);
        }

        //포인트 확인 및 차감
        if (requestDto.getUsedPoint() > user.getPoint()) {
            throw new ApplicationException(ErrorCode.NOT_ENOUGH_POINT);
        }
        user.subtractPoint(requestDto.getUsedPoint());
        totalPriceAmount -= requestDto.getUsedPoint();

        //주문 생성
        Orders order = OrderRequestDto.toEntity(
                totalPriceAmount,
                requestDto.getUsedPoint(),
                LocalDateTime.now(),
                OrderStatus.NEW,
                store,
                user
        );

        //주문 저장
        orderRepository.save(order);

        //주문 아이템 저장
        for (OrderItemRequestDto itemRequestDto : requestDto.getOrderItems()) {
            Menu menu = menuRepository.findById(itemRequestDto.getMenuId()).orElseThrow(
                    () -> new ApplicationException(ErrorCode.NOT_FOUND_MENU)
            );
            OrderItem orderItem = OrderItemRequestDto.toEntity(itemRequestDto.getQuantity(), order, menu);
            orderItemRepository.save(orderItem);
        }

        return OrderResponseDto.of(order);
    }
}
