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

    List<Orders> findAllByUserId(Long id);

    // 관리자 통계 시작
    @Query("select o.store.storeName , sum (o.totalPriceAmount) from Orders o " +
            "WHERE function('date_format', o.orderAt, '%Y-%m-%d') = :localDate " +
            "AND o.orderStatus = 'COMPLETED' " +
            "group by o.store.storeName")
    List<StatisticsPriceResponseDto> getTotalPriceByStoreAndDate(@Param("localDate") LocalDate localDate);

    @Query("select o.store.storeName, sum(o.totalPriceAmount) from Orders o " +
            "WHERE function('date_format', o.orderAt, '%Y-%m-%d') BETWEEN :startDate AND :endDate " +
            "AND o.orderStatus = 'COMPLETED' " +
            "group by o.store.storeName")
    List<StatisticsPriceResponseDto> getTotalPriceByStoreAndMonth(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("select count(o) from Orders o " +
            "WHERE function('date_format', o.orderAt, '%Y-%m-%d') BETWEEN :startDate AND :endDate " +
            "AND o.orderStatus = 'COMPLETED' " +
            "group by o.store.storeName")
    List<StatisticsCountResponseDto> getCountOrdersByStoreAndMonth(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("select new com.example.outsourcing.domain.statistics.dto.response.StatisticsCountResponseDto(o.store.storeName, count(o)) " +
            "from Orders o " +
            "WHERE function('date_format', o.orderAt, '%Y-%m-%d') = :localDate " +
            "AND o.orderStatus = 'COMPLETED' " +
            "group by o.store.storeName")
    List<StatisticsCountResponseDto> getCountOrdersByStoreAndDate(@Param("localDate") LocalDate localDate);

    // 관리자 통계 끝

    // 가게 통계 시작
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

    // 가게 통계 끝

}