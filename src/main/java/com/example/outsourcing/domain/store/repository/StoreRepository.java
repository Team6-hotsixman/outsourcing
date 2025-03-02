package com.example.outsourcing.domain.store.repository;

import com.example.outsourcing.domain.store.entity.Store;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT COUNT(s) " +
            "FROM Store s " +
            "WHERE s.user.id = :userId")
    long countStoresByUserId(Long userId);

    @Query(value = "SELECT s " +
            "         FROM Store s " +
            "        WHERE ST_CONTAINS(ST_BUFFER( :area, :distance ), s.location) ")
    Page<Store> findStoresByArea(@Param("area") Point area,
                                 @Param("distance") double distance,
                                 Pageable pageable);

    @Query(value = "SELECT s " +
            "         FROM Menu m " +
            "         LEFT JOIN Store s " +
            "                ON m.store = s " +
            "         LEFT JOIN Category mc " +
            "                ON m.category = mc " +
            "         LEFT JOIN Category sc " +
            "                ON s.category = sc " +
            "        WHERE s.id IN ( SELECT s.id " +
                            "         FROM Store s " +
                            "        WHERE ST_CONTAINS(ST_BUFFER( :area, :distance ), s.location) " +
            "                      )" +
            "          AND ( m.menuName LIKE %:search% " +
            "                OR sc.categoryName LIKE %:search% " +
            "                OR mc.categoryName LIKE %:search% " +
            "                OR sc.categoryName LIKE %:search% " +
            "              ) " +
            "        GROUP BY s",
            countQuery = "SELECT COUNT(DISTINCT s.id) " +
                    "       FROM Menu m " +
                    "       LEFT JOIN Store s ON m.store = s " +
                    "       LEFT JOIN Category mc ON m.category = mc " +
                    "       LEFT JOIN Category sc ON s.category = sc " +
                    "      WHERE s.id IN ( SELECT s.id " +
                    "                        FROM Store s " +
                    "                       WHERE ST_CONTAINS(ST_BUFFER(:area, :distance), s.location) " +
                    "                    ) " +
                    "        AND ( m.menuName LIKE %:search% " +
                    "             OR sc.categoryName LIKE %:search% " +
                    "             OR mc.categoryName LIKE %:search% " +
                    "             OR sc.categoryName LIKE %:search%" +
                    "            ) ",
            countProjection = "COUNT(DISTINCT s.id)")
    Page<Store> findStoresByAreaAndSearch(@Param("area") Point area,
                                          @Param("distance") double distance,
                                          @Param("search")String keyword,
                                          Pageable pageable);


}
