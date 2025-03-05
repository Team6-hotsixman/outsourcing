package com.example.outsourcing.domain.statistics.controller;

import com.example.outsourcing.domain.common.annotation.Admin;
import com.example.outsourcing.domain.common.annotation.Auth;
import com.example.outsourcing.domain.common.annotation.Owner;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsCountResponseDto;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsPriceResponseDto;
import com.example.outsourcing.domain.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Admin
    @GetMapping("/statistics/orders")
    public ResponseEntity<List<StatisticsCountResponseDto>> getCountOrders(
            @RequestParam String date) {
        return ResponseEntity.ok(statisticsService.getCountOrders(date));
    }

    @Admin
    @GetMapping("/statistics/price")
    public ResponseEntity<List<StatisticsPriceResponseDto>> getTotalPrice(
            @RequestParam String date) {
        return ResponseEntity.ok(statisticsService.getTotalPrice(date));
    }

    @Owner
    @GetMapping("/statistics/orders/{storeId}")
    public ResponseEntity<StatisticsCountResponseDto> getCountOrdersByStore(
            @Auth AuthUser user,
            @RequestParam String date,
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(statisticsService.getCountOrdersByStore(date, storeId, user));
    }

    @Owner
    @GetMapping("/statistics/price/{storeId}")
    public ResponseEntity<StatisticsPriceResponseDto> getTotalRevenueByStore(
            @Auth AuthUser user,
            @RequestParam String date,
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(statisticsService.getTotalRevenueByStore(date, storeId, user));
    }
}
