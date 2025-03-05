package com.example.outsourcing.domain.order.service;

import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.menuoption.entity.MenuOption;
import com.example.outsourcing.domain.menu.menuoption.repository.MenuOptionRepository;
import com.example.outsourcing.domain.menu.repository.MenuRepository;
import com.example.outsourcing.domain.order.dto.request.OrderItemOptionRequestDto;
import com.example.outsourcing.domain.order.dto.request.OrderItemRequestDto;
import com.example.outsourcing.domain.order.dto.request.OrderRequestDto;
import com.example.outsourcing.domain.order.dto.response.OrderItemOptionResponseDto;
import com.example.outsourcing.domain.order.dto.response.OrderItemResponseDto;
import com.example.outsourcing.domain.order.dto.response.OrderSimpleResponseDto;
import com.example.outsourcing.domain.order.dto.response.OrderResponseDto;
import com.example.outsourcing.domain.order.dto.request.OrderStatusRequestDto;
import com.example.outsourcing.domain.order.entity.OrderItem;
import com.example.outsourcing.domain.order.entity.OrderItemOption;
import com.example.outsourcing.domain.order.entity.Orders;
import com.example.outsourcing.domain.order.enums.OrderStatus;
import com.example.outsourcing.domain.order.repository.OrderItemOptionRepository;
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
import java.util.List;

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
    //todo 이후 MenuOptionService를 통해 접근할 수 있도록 수정 예정
    private final MenuOptionRepository menuOptionRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemOptionRepository orderItemOptionRepository;

    //주문 생성
    @Transactional
    public OrderSimpleResponseDto placeOrder(
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
        //for 반복문을 통해 menu 조회
        for (OrderItemRequestDto itemRequestDto : requestDto.getOrderItems()) {
            Menu menu = menuRepository.findById(itemRequestDto.getMenuId()).orElseThrow(
                    () -> new ApplicationException(ErrorCode.NOT_FOUND_MENU)
            );
            //for 반복문을 통해 menuOption 조회
            for (OrderItemOptionRequestDto optionRequestDto : itemRequestDto.getOptions()) {
                MenuOption menuOption = menuOptionRepository.findById(optionRequestDto.getMenuOptionId()).orElseThrow(
                        () -> new ApplicationException(ErrorCode.NOT_FOUND_MENU_OPTION)
                );
                //menuOption 총 금액
                totalPriceAmount += (menuOption.getPrice() * optionRequestDto.getQuantity());
            }
            //menu + menuOption 총 금액
            totalPriceAmount += (menu.getPrice() * itemRequestDto.getQuantity());
        }

        //최소 주문 금액 확인
        if (totalPriceAmount < store.getMinOrderPrice()) {
            throw new ApplicationException(ErrorCode.LESS_THAN_MIN_ORDER_PRICE);
        }

        //포인트 확인 및 차감
        user.subtractPoint(requestDto.getUsedPoint());
        totalPriceAmount -= requestDto.getUsedPoint();

        //주문 생성 및 저장
        Orders order = OrderRequestDto.toEntity(
                totalPriceAmount,
                requestDto.getUsedPoint(),
                LocalDateTime.now(),
                OrderStatus.NEW,
                store,
                user
        );
        orderRepository.save(order);

        //주문 아이템 생성 및 저장
        for (OrderItemRequestDto itemRequestDto : requestDto.getOrderItems()) {
            Menu menu = menuRepository.findById(itemRequestDto.getMenuId()).orElseThrow(
                    () -> new ApplicationException(ErrorCode.NOT_FOUND_MENU)
            );
            OrderItem orderItem = OrderItemRequestDto.toEntity(itemRequestDto.getQuantity(), order, menu);
            orderItemRepository.save(orderItem);

            //주문 아이템 옵션 생성 및 저장
            for (OrderItemOptionRequestDto optionRequestDto : itemRequestDto.getOptions()) {
                MenuOption menuOption = menuOptionRepository.findById(optionRequestDto.getMenuOptionId()).orElseThrow(
                        () -> new ApplicationException(ErrorCode.NOT_FOUND_MENU_OPTION)
                );
                OrderItemOption orderItemOption = OrderItemOptionRequestDto.toEntity(optionRequestDto.getQuantity(), orderItem, menuOption);
                orderItemOptionRepository.save(orderItemOption);
            }
        }

        return OrderSimpleResponseDto.of(order);
    }

    //주문 내역 조회
    @Transactional(readOnly = true)
    public List<OrderResponseDto> findAll(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );

        //response body에 주문 메뉴, 메뉴 옵션을 반환하기 위한 로직
        //특정 사용자의 모든 주문 조회
        List<Orders> orders = orderRepository.findAllByUserId(user.getId());
        //Orders -> OrderResponeDto 변환
        List<OrderResponseDto> orderDtos = orders.stream().map(
                order -> {
                    //주문 아이템 조회
                    List<OrderItem> menus = orderItemRepository.findAllByOrderId(order.getId());
                    // 주문 아이템 DTO 리스트 생성
                    List<OrderItemResponseDto> orderItemDtos = menus.stream().map(
                            item -> {
                                //주문 아이템 옵션 조회
                                List<OrderItemOption> options = orderItemOptionRepository.findAllByOrderItemId(item.getId());

                                // 주문 아이템 옵션 DTO 생성
                                List<OrderItemOptionResponseDto> optionDtos = options.stream().map(
                                        option -> {
                                            MenuOption menuOption = menuOptionRepository.findById(option.getMenuOption().getId()).orElseThrow(
                                                    () -> new ApplicationException(ErrorCode.NOT_FOUND_MENU_OPTION)
                                            );
                                            return new OrderItemOptionResponseDto(
                                                    menuOption.getId(),
                                                    menuOption.getOptionName(),
                                                    option.getQuantity()
                                            );
                                        }
                                ).toList();

                                // 주문 아이템 DTO 생성
                                Menu menu = menuRepository.findById(item.getMenu().getId()).orElseThrow(
                                        () -> new ApplicationException(ErrorCode.NOT_FOUND_MENU)
                                );
                                return new OrderItemResponseDto(
                                        menu.getId(),
                                        menu.getMenuName(),
                                        item.getQuantity(),
                                        optionDtos
                                );
                            }
                    ).toList();

                    return OrderResponseDto.of(order, orderItemDtos);
                }
        ).toList();

        return orderDtos;
    }

    //주문 수락/거절/배달중/배달완료 상태 변경
    @Transactional
    public OrderSimpleResponseDto updateOrderStatus(
            AuthUser authUser,
            Long storeId,
            Long orderId,
            OrderStatusRequestDto requestDto
    ) {
        User user = User.fromAuthUser(authUser);
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_STORE)
        );
        Orders order = orderRepository.findById(orderId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_ORDER)
        );

        //조회한 주문이 해당 가게에서 발생한 주문이 아닐 경우
        if (!store.getId().equals(order.getStore().getId())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_ORDER_WITH_STORE);
        }

        //가게 사장님만 주문 수락/거절 가능
        if (!order.getStore().getUser().getId().equals(user.getId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_STORE_OWNER);
        }

        // 현재 상태와 같으면 예외처리
        if (order.getOrderStatus().equals(requestDto.getOrderStatus())) {
            throw new ApplicationException(ErrorCode.ORDER_STATUS_ALREADY_SAME);
        }

        //Status 변경
        order.updateOrderStatus(requestDto.getOrderStatus());

        //주문 배달 완료 시 주문 고객 포인트 적립
        if (order.getOrderStatus().equals(OrderStatus.COMPLETED)) {
            User customer = userRepository.findById(order.getUser().getId()).orElseThrow(
                    () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
            );
            Integer pointToEarn = (int) (order.getTotalPriceAmount() * 0.03);
            customer.earnPoint(pointToEarn);
        }

        return OrderSimpleResponseDto.of(order);
    }

    //주문 취소
    @Transactional
    public void cancelOrder(Long userId, Long orderId, Long storeId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_STORE)
        );
        Orders order = orderRepository.findById(orderId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_ORDER)
        );

        //조회한 주문이 해당 가게에서 발생한 주문이 아닐 경우
        if (!store.getId().equals(order.getStore().getId())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_ORDER_WITH_STORE);
        }

        //주문을 요청한 고객만 주문 취소 가능
        if (!user.getId().equals(order.getUser().getId())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_ORDER_WITH_USER);
        }

        //가게에서 주문을 수락 전에만 취소 가능
        if (!order.getOrderStatus().equals(OrderStatus.NEW)) {
            throw new ApplicationException(ErrorCode.CANT_CANCEL_AFTER_COOKING);
        }

        orderRepository.deleteById(orderId);
    }
}
