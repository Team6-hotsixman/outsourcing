package com.example.outsourcing.domain.order.repository;

import com.example.outsourcing.domain.order.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("select o.store.storeName , sum (o.totalPriceAmount) from Orders o " +
            "where Date(o.orderAt) = DATE(:date)" +
            "group by o.store.storeName")
    Page<Orders> findTotalPriceByStoreAndDay(@Param("date") LocalDate date, Pageable pageable);

    @Query("select o.store.storeName, sum(o.totalPriceAmount) from Orders o " +
            "where function('YEAR', o.orderAt) = :year " +
            "and function('MONTH', o.orderAt) = :month " +
            "group by o.store.storeName")
    Page<Orders> findTotalPriceByStoreAndMonth(@Param("year") int year, @Param("month") int month, Pageable pageable);

    @Query("select count(o) from Orders o " +
            "where function('YEAR', o.orderAt) = :year " +
            "and function('MONTH', o.orderAt) = :month " +
            "group by o.store.storeName")
    Page<Orders> countOrdersByStoreAndMonth(@Param("year") int year, @Param("month") int month, Pageable pageable);

    @Query("select count(o) from Orders o " +
            "where DATE(o.orderAt) = DATE(:date) " +
            "group by o.store.storeName")
    Page<Orders> countOrdersByStoreAndDay(@Param("date") LocalDate date, Pageable pageable);


}
