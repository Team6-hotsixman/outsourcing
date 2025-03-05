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

    @Query("SELECT new com.example.outsourcing.domain.statistics.dto.response.StatisticsCountResponseDto(o.store.storeName, count(o)) " +
            "FROM Orders o " +
            "WHERE function('date_format', o.orderAt, '%Y-%m-%d') BETWEEN :startDate AND :endDate " +
            "AND o.store.id = :storeId " +
            "AND o.orderStatus = 'COMPLETED' " +
            "GROUP BY o.store.id")
    StatisticsCountResponseDto findCountOrdersByMonth(
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT new com.example.outsourcing.domain.statistics.dto.response.StatisticsCountResponseDto(o.store.storeName, count(o)) " +
            "FROM Orders o " +
            "WHERE function('date_format', o.orderAt, '%Y-%m-%d') = :localDate " +
            "AND o.store.id = :storeId " +
            "AND o.orderStatus = 'COMPLETED' " +
            "GROUP BY o.store.id")
    StatisticsCountResponseDto findCountOrdersByDay(@Param("storeId") Long storeId, @Param("localDate") LocalDate localDate);


    @Query("SELECT new com.example.outsourcing.domain.statistics.dto.response.StatisticsPriceResponseDto(o.store.storeName, sum(o.totalPriceAmount)) " +
            "FROM Orders o " +
            "WHERE function('date_format', o.orderAt, '%Y-%m-%d') BETWEEN :startDate AND :endDate " +
            "AND o.store.id = :storeId " +
            "AND o.orderStatus = 'COMPLETED' " +
            "GROUP BY o.store.id")
    StatisticsPriceResponseDto findTotalRevenueByMonth(
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT new com.example.outsourcing.domain.statistics.dto.response.StatisticsPriceResponseDto(o.store.storeName, sum(o.totalPriceAmount)) " +
            "FROM Orders o " +
            "WHERE function('date_format', o.orderAt, '%Y-%m-%d') = :localDate " +
            "AND o.store.id = :storeId " +
            "AND o.orderStatus = 'COMPLETED' " +
            "GROUP BY o.store.id")
    StatisticsPriceResponseDto findTotalRevenueByDay(@Param("storeId") Long storeId, @Param("localDate") LocalDate localDate);
}

