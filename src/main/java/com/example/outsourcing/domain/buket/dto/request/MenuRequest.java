package com.example.outsourcing.domain.buket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MenuRequest {
    private long menuId;
    private String menuName;
    private List<Long> options;
    private int quantity;
}
