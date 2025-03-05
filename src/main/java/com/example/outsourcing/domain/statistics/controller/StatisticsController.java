package com.example.outsourcing.domain.statistics.controller;

import com.example.outsourcing.domain.common.annotation.Admin;
import com.example.outsourcing.domain.common.annotation.Auth;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsCountResponseDto;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsPriceResponseDto;
import com.example.outsourcing.domain.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Admin
public class StatisticsController {

    private final StatisticsService statisticsService;

    // 관리자 통계 start
    @GetMapping("/admin/statistics/price/month")
    public ResponseEntity<List<StatisticsPriceResponseDto>> getTotalPriceByStoreAndMonth(
            @Auth AuthUser user,
            @RequestParam int year,
            @RequestParam int month
            ) {
        return ResponseEntity.ok(statisticsService.getTotalPriceByStoreAndMonth(user, year, month));
    }

    @GetMapping("/admin/statistics/price/date")
    public ResponseEntity<List<StatisticsPriceResponseDto>> getTotalPriceByStoreAndDate(
            @Auth AuthUser user,
            @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(statisticsService.getTotalPriceByStoreAndDate(user, date));
    }

    @GetMapping("/admin/statistics/orders/date")
    public ResponseEntity<List<StatisticsCountResponseDto>> getCountOrdersByStoreAndDate(
            @Auth AuthUser user,
            @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(statisticsService.getCountOrdersByStoreAndDate(user, date));
    }

    @GetMapping("/admin/statistics/orders/month")
    public ResponseEntity<List<StatisticsCountResponseDto>> getCountOrdersByStoreAndMonth(
            @Auth AuthUser user,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(statisticsService.getCountOrdersByStoreAndMonth(user, year, month));
    }
    // 관리자 통계 end


}
