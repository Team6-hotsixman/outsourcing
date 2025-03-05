package com.example.outsourcing.domain.statistics.service;

import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.order.repository.OrderRepository;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsCountResponseDto;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsPriceResponseDto;
import com.example.outsourcing.domain.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final OrderRepository orderRepository;

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

}
