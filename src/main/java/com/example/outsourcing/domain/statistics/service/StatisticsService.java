package com.example.outsourcing.domain.statistics.service;

import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.order.repository.OrderRepository;
import com.example.outsourcing.domain.order.service.OrderService;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsCountResponseDto;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsPriceResponseDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.service.StoreService;
import com.example.outsourcing.domain.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final OrderRepository orderRepository;

    private final OrderService orderService;

    private final StoreService storeService;

    // 관리자 통계 start
    @Transactional(readOnly = true)
    public List<StatisticsPriceResponseDto> getTotalPriceByStoreAndMonth(AuthUser user, int year, int month) {
        if (user.getUserRole().equals(UserRole.ADMIN)) {
            throw new ApplicationException(ErrorCode.Unauthorized_User);
        }
        return orderRepository.getTotalPriceByStoreAndMonth(year, month);
    }

    @Transactional(readOnly = true)
    public List<StatisticsPriceResponseDto> getTotalPriceByStoreAndDate(AuthUser user, LocalDate date) {
        if (user.getUserRole().equals(UserRole.ADMIN)) {
            throw new ApplicationException(ErrorCode.Unauthorized_User);
        }
        return orderRepository.getTotalPriceByStoreAndDate(date);
    }

    @Transactional(readOnly = true)
    public List<StatisticsCountResponseDto> getCountOrdersByStoreAndDate(AuthUser user, LocalDate date) {
        if (user.getUserRole().equals(UserRole.ADMIN)) {
            throw new ApplicationException(ErrorCode.Unauthorized_User);
        }
        return orderRepository.getCountOrdersByStoreAndDate(date);
    }

    @Transactional(readOnly = true)
    public List<StatisticsCountResponseDto> getCountOrdersByStoreAndMonth(AuthUser user, int year, int month) {
        if (user.getUserRole().equals(UserRole.ADMIN)) {
            throw new ApplicationException(ErrorCode.Unauthorized_User);
        }
        return orderRepository.getCountOrdersByStoreAndMonth(year, month);
    }

    // 관리자 통계 end
    // 가게 단일 통계 start

    @Transactional(readOnly = true)
    public StatisticsCountResponseDto getCountOrdersByStore(String date, Long storeId, AuthUser authUser) {
        Store store = storeService.getStore(storeId);
        if (!authUser.getId().equals(store.getUser().getId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_STORE_OWNER);
        }
        if (date.length() == 7) {
            YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();
            return orderService.getCountOrdersByMonth(storeId, startDate, endDate);
        }
        if (date.length() == 10) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            return orderService.getCountOrdersByDay(storeId, localDate);
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
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();
            return orderService.getTotalRevenueByMonth(storeId, startDate, endDate);
        }
        if (date.length() == 10) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            return orderService.getTotalRevenueByDay(storeId, localDate);
        }

        throw new ApplicationException(ErrorCode.INVALID_DATE_FORMAT);
    }
    // 가게 단일 통계 end
}
