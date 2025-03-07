package com.example.outsourcing.domain.buket.dto.response;

import com.example.outsourcing.domain.order.dto.request.OrderItemOptionRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {
    private long menuId;
    private String menuName;
    private List<OrderItemOptionRequestDto> options;
    private int quantity;

    public static CartResponseDto fromEntry(Object key, Object value) {
        int quantity = (value instanceof Integer) ? (int) value : Integer.parseInt(value.toString());

        String[] parts = key.toString().split(":");
        long menuId = Long.parseLong(parts[0]);
        String menuName = parts[1];

        List<OrderItemOptionRequestDto> options = List.of();
        if (parts.length > 2) {
            String optionString = parts[2].replaceAll("[\\[\\]]", "");
            if (!optionString.isEmpty()) {
                options = List.of(optionString.split(",")).stream()
                        .map(v -> {
                            String[] split = v.split("/");
                            Long optionId = Long.parseLong(split[0]);
                            int q = Integer.parseInt(split[1]);
                            return new OrderItemOptionRequestDto(optionId, q);
                        })
                        .toList();
            }
        }

        return new CartResponseDto(menuId, menuName, options, quantity);
    }
}

