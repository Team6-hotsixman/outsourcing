package com.example.outsourcing.domain.order.repository;

import com.example.outsourcing.domain.order.entity.Orders;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsCountResponseDto;
import com.example.outsourcing.domain.statistics.dto.response.StatisticsPriceResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByUserId(Long id);

    @Query("SELECT new com.example.outsourcing.domain.statistics.dto.response.StatisticsPriceResponseDto(o.store.storeName, SUM(o.totalPriceAmount)) " +
            "FROM Orders o " +
            "WHERE o.orderAt BETWEEN :startDate AND :endDate " +
            "AND o.orderStatus = 'COMPLETED' " +
            "GROUP BY o.store.storeName")
    List<StatisticsPriceResponseDto> getTotalPriceByStore(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("select new com.example.outsourcing.domain.statistics.dto.response.StatisticsCountResponseDto(o.store.storeName, count(o)) " +
            "from Orders o " +
            "WHERE o.orderAt BETWEEN :startDate AND :endDate " +
            "AND o.orderStatus = 'COMPLETED' " +
            "group by o.store.storeName")
    List<StatisticsCountResponseDto> getCountOrdersByStore(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.example.outsourcing.domain.statistics.dto.response.StatisticsCountResponseDto(o.store.storeName, count(o)) " +
            "FROM Orders o " +
            "WHERE o.orderAt BETWEEN :startDate AND :endDate " +
            "AND o.store.id = :storeId " +
            "AND o.orderStatus = 'COMPLETED' " +
            "GROUP BY o.store.id")
    StatisticsCountResponseDto findCountOrders(
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.example.outsourcing.domain.statistics.dto.response.StatisticsPriceResponseDto(o.store.storeName, sum(o.totalPriceAmount)) " +
            "FROM Orders o " +
            "WHERE o.orderAt BETWEEN :startDate AND :endDate " +
            "AND o.store.id = :storeId " +
            "AND o.orderStatus = 'COMPLETED' " +
            "GROUP BY o.store.id")
    StatisticsPriceResponseDto findTotalRevenue(
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}