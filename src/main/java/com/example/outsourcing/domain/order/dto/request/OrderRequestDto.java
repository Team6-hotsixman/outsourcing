package com.example.outsourcing.domain.order.dto.request;

import com.example.outsourcing.domain.order.entity.Orders;
import com.example.outsourcing.domain.order.enums.OrderStatus;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    @NotNull
    private List<OrderItemRequestDto> orderItems;
    private Integer usedPoint;
    private Long userCouponId;

    public static Orders toEntity(
            Integer totalPriceAmount,
            Integer usedPoint,
            LocalDateTime orderAt,
            OrderStatus orderStatus,
            Store store,
            User user
            ) {
        return new Orders(
                totalPriceAmount,
                usedPoint,
                orderAt,
                orderStatus,
                store,
                user);
    }
}