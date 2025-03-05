package com.example.outsourcing.domain.order.repository;

import com.example.outsourcing.domain.order.entity.Orders;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsCountResponseDto;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsPriceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    Page<Orders> findAllByUserId(Long userId, Pageable pageable);

    @Query("select o.store.storeName , sum (o.totalPriceAmount) from Orders o " +
            "where Date(o.orderAt) = DATE(:date)" +
            "group by o.store.storeName")
    List<StatisticsPriceResponseDto> getTotalPriceByStoreAndDate(@Param("date") LocalDate date);

    @Query("select o.store.storeName, sum(o.totalPriceAmount) from Orders o " +
            "where function('YEAR', o.orderAt) = :year " +
            "and function('MONTH', o.orderAt) = :month " +
            "group by o.store.storeName")
    List<StatisticsPriceResponseDto> getTotalPriceByStoreAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("select count(o) from Orders o " +
            "where function('YEAR', o.orderAt) = :year " +
            "and function('MONTH', o.orderAt) = :month " +
            "group by o.store.storeName")
    List<StatisticsCountResponseDto> getCountOrdersByStoreAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("select count(o) from Orders o " +
            "where DATE(o.orderAt) = DATE(:date) " +
            "group by o.store.storeName")
    List<StatisticsCountResponseDto> getCountOrdersByStoreAndDate(@Param("date") LocalDate date);
}
