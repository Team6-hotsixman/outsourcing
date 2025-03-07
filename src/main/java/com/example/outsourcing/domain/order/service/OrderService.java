package com.example.outsourcing.domain.order.service;

import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.coupon.entity.Coupon;
import com.example.outsourcing.domain.coupon.entity.UserCoupon;
import com.example.outsourcing.domain.coupon.enums.DiscountType;
import com.example.outsourcing.domain.coupon.repository.UserCouponRepository;
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
import com.example.outsourcing.domain.statistics.dto.response.StatisticsCountResponseDto;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsPriceResponseDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemOptionRepository orderItemOptionRepository;
    private final UserCouponRepository userCouponRepository;

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

        // 포인트와 쿠폰 중복 사용 불가
        if (requestDto.getUsedPoint() != null && requestDto.getUserCouponId() != null) {
            throw new ApplicationException(ErrorCode.CANT_USE_BOTH_POINT_AND_COUPON);
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

        //포인트 사용 case
        if (requestDto.getUsedPoint() != null) {
            //포인트 확인 및 차감
            user.subtractPoint(requestDto.getUsedPoint());
            totalPriceAmount -= requestDto.getUsedPoint();
        }

        //쿠폰 사용 case
        if (requestDto.getUserCouponId() != null) {
            // 사용하려는 쿠폰 가져오기
            UserCoupon userCoupon = userCouponRepository.findById(requestDto.getUserCouponId()).orElseThrow(
                    () -> new ApplicationException(ErrorCode.NOT_FOUND_USER_COUPON)
            );

            // 이미 사용된 쿠폰인지 검증
            if (userCoupon.isUsed()) {
                throw new ApplicationException(ErrorCode.USED_COUPON);
            }
            Coupon coupon = userCoupon.getCoupon();
            if (totalPriceAmount < coupon.getMinOrderPrice()) {
                throw new ApplicationException(ErrorCode.NOT_ENOUGH_ORDER_PRICE);
            }

            //할인 금액 계산
            int discount = coupon.getDiscountType() == DiscountType.FIXED ? coupon.getDiscountValue() :
                    (totalPriceAmount * coupon.getDiscountValue()) / 100;

            //쿠폰 사용 처리
            userCoupon.useCoupon();
            userCouponRepository.save(userCoupon);
            totalPriceAmount -= discount;
        }

        //주문 생성 및 저장
        Orders order = OrderRequestDto.toEntity(
                totalPriceAmount,
                requestDto.getUsedPoint(),
                LocalDateTime.now(),
                OrderStatus.NEW,
                store,
                user,
                null
        );
        orderRepository.save(order);

        //주문 아이템 생성 및 저장
        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemOption> options = new ArrayList<>();
        for (OrderItemRequestDto itemRequestDto : requestDto.getOrderItems()) {
            Menu menu = menuRepository.findById(itemRequestDto.getMenuId()).orElseThrow(
                    () -> new ApplicationException(ErrorCode.NOT_FOUND_MENU)
            );
            OrderItem orderItem = OrderItemRequestDto.toEntity(itemRequestDto.getQuantity(), order, menu, options);
            orderItems.add(orderItem);
            orderItemRepository.save(orderItem);

            //주문 아이템 옵션 생성 및 저장
            for (OrderItemOptionRequestDto optionRequestDto : itemRequestDto.getOptions()) {
                MenuOption menuOption = menuOptionRepository.findById(optionRequestDto.getMenuOptionId()).orElseThrow(
                        () -> new ApplicationException(ErrorCode.NOT_FOUND_MENU_OPTION)
                );
                OrderItemOption orderItemOption = OrderItemOptionRequestDto.toEntity(optionRequestDto.getQuantity(), orderItem, menuOption);
                options.add(orderItemOption);
                orderItemOptionRepository.save(orderItemOption);
            }
        }
        // 값이 주입된 List<OrderItem> orderItems를 order 필드에 업데이트

        order.updateOrderItems(orderItems);
        return OrderSimpleResponseDto.of(order);
    }

    //주문 내역 조회
    @Transactional(readOnly = true)
    public List<OrderResponseDto> findAll(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );

        //response body 에 주문 메뉴, 메뉴 옵션을 반환하기 위한 로직
        //특정 사용자의 모든 주문 조회
        List<Orders> orders = orderRepository.findAllByUserId(user.getId());
        //Orders -> OrderResponseDto 변환
        return orders.stream().map(
                this::getOrderItems).toList();
    }

    //주문 단건 조회
    @Transactional(readOnly = true)
    public OrderResponseDto findOne(Long orderId, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );
        Orders order = orderRepository.findById(orderId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_ORDER)
        );

        //user.getId()와 order.getUser.getId() 비교
        if (!user.getId().equals(order.getUser().getId())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_ORDER_WITH_USER);
        }

        return getOrderItems(order);
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

        //해당 주문의 가게 사장님만 주문 상태 변경 가능
        if (!order.getStore().getUser().getId().equals(user.getId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_STORE_OWNER);
        }

        // 현재 상태와 같으면 예외처리
        if (order.getOrderStatus().equals(OrderStatus.valueOf(requestDto.getOrderStatus()))) {
            throw new ApplicationException(ErrorCode.ORDER_STATUS_ALREADY_SAME);
        }

        // 거절 및 완료된 주문은 상태를 변경할 수 없음
        if (order.getOrderStatus() == OrderStatus.REJECTED
                || order.getOrderStatus() == OrderStatus.COMPLETED
        ) {
            throw new ApplicationException(ErrorCode.CANT_UPDATE_ORDER_STATUS);
        }

        // OrderStatus : new -> cooking -> delivering -> completed 또는
        // new -> rejected 로만 변경할 수 있도록 예외처리
        if (
                order.getOrderStatus() == OrderStatus.NEW
                        && OrderStatus.valueOf(requestDto.getOrderStatus()) != OrderStatus.COOKING
                        && OrderStatus.valueOf(requestDto.getOrderStatus()) != OrderStatus.REJECTED
        ) {
            throw new ApplicationException(ErrorCode.MISMATCHED_ORDER_STATUS);
        }
        if (order.getOrderStatus() == OrderStatus.COOKING
                && OrderStatus.valueOf(requestDto.getOrderStatus()) != OrderStatus.DELIVERING
        ) {
            throw new ApplicationException(ErrorCode.MISMATCHED_ORDER_STATUS);
        }
        if (order.getOrderStatus() == OrderStatus.DELIVERING
                && OrderStatus.valueOf(requestDto.getOrderStatus()) != OrderStatus.COMPLETED) {
            throw new ApplicationException(ErrorCode.MISMATCHED_ORDER_STATUS);
        }

        //Status 변경
        order.updateOrderStatus(OrderStatus.valueOf(requestDto.getOrderStatus()));

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

    // store 통계 시작
    public StatisticsCountResponseDto getCountOrders(Long storeId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findCountOrders(storeId, startDate, endDate);
    }

    public StatisticsPriceResponseDto getTotalRevenue(Long storeId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findTotalRevenue(storeId, startDate, endDate);
    }
    // store 통계 끝

    // 관리자 통계 start
    @Transactional(readOnly = true)
    public List<StatisticsPriceResponseDto> getTotalPriceByStore(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return orderRepository.getTotalPriceByStore(startDateTime, endDateTime);
    }

    @Transactional(readOnly = true)
    public List<StatisticsCountResponseDto> getCountOrdersByStore(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return orderRepository.getCountOrdersByStore(startDateTime, endDateTime);
    }
    // 관리자 통계 end

    //order 내에 있는 menus 및 menuOptions 를 orderResponseDto 타입으로 반환
    private OrderResponseDto getOrderItems(Orders order) {
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
}
