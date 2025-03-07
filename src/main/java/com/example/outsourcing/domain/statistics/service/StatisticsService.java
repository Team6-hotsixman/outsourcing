package com.example.outsourcing.domain.statistics.service;

import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.order.service.OrderService;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsCountResponseDto;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsPriceResponseDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final OrderService orderService;

    private final StoreService storeService;

    @Transactional(readOnly = true)
    public List<StatisticsPriceResponseDto> getTotalPrice(String date) {
        if (date.length() == 7) {
            YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
            LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
            return orderService.getTotalPriceByStore(startDateTime, endDateTime);
        }
        if (date.length() == 10) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            LocalDateTime startDateTime = localDate.atStartOfDay();
            LocalDateTime endDateTime = localDate.atTime(LocalTime.MAX);
            return orderService.getTotalPriceByStore(startDateTime, endDateTime);
        }

        throw new ApplicationException(ErrorCode.INVALID_DATE_FORMAT);
    }

    @Transactional(readOnly = true)
    public List<StatisticsCountResponseDto> getCountOrders(String date) {
        if (date.length() == 7) {
            YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
            LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
            return orderService.getCountOrdersByStore(startDateTime, endDateTime);
        }
        if (date.length() == 10) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            LocalDateTime startDateTime = localDate.atStartOfDay();
            LocalDateTime endDateTime = localDate.atTime(LocalTime.MAX);
            return orderService.getCountOrdersByStore(startDateTime, endDateTime);
        }

        throw new ApplicationException(ErrorCode.INVALID_DATE_FORMAT);
    }


    @Transactional(readOnly = true)
    public StatisticsCountResponseDto getCountOrdersByStore(String date, Long storeId, AuthUser authUser) {
        Store store = storeService.getStore(storeId);
        if (!authUser.getId().equals(store.getUser().getId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_STORE_OWNER);
        }
        if (date.length() == 7) {
            YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
            LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
            return orderService.getCountOrders(storeId, startDateTime, endDateTime);
        }
        if (date.length() == 10) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            LocalDateTime startDateTime = localDate.atStartOfDay();
            LocalDateTime endDateTime = localDate.atTime(LocalTime.MAX);
            return orderService.getCountOrders(storeId, startDateTime, endDateTime);
        }

        throw new ApplicationException(ErrorCode.INVALID_DATE_FORMAT);
    }

    @Transactional(readOnly = true)
    public StatisticsPriceResponseDto getTotalRevenueByStore(String date, Long storeId, AuthUser authUser) {
        Store store = storeService.getStore(storeId);
        if (!authUser.getId().equals(store.getUser().getId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_STORE_OWNER);
        }

        if (date.length() == 7) {
            YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
            LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
            return orderService.getTotalRevenue(storeId, startDateTime, endDateTime);
        }
        if (date.length() == 10) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            LocalDateTime startDateTime = localDate.atStartOfDay();
            LocalDateTime endDateTime = localDate.atTime(LocalTime.MAX);
            return orderService.getTotalRevenue(storeId, startDateTime, endDateTime);
        }

        throw new ApplicationException(ErrorCode.INVALID_DATE_FORMAT);
    }
}
