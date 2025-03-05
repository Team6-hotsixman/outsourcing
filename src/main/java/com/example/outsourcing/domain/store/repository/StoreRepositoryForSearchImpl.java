package com.example.outsourcing.domain.store.repository;

import com.example.outsourcing.domain.order.enums.OrderStatus;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreResponseForNativeQuery;
import com.example.outsourcing.domain.store.enums.OrderBy;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryForSearchImpl implements StoreRepositoryForSearch{
    private final EntityManager entityManager;

    @Override
    public List<StoreResponseDto> findStoresByArea(Point location,
                                                   Pageable page,
                                                   OrderBy orderBy,
                                                   OrderStatus orderStatus,
                                                   StoreStatus storeStatus) {
        String base = "SELECT new com.example.outsourcing.domain.store.dto.response.StoreResponseDto("
                    + "s "
                    + ", CAST( ST_Distance_Sphere(s.location, :location) AS double) "
                    + ", CAST( COALESCE(AVG(r.rate), 0) AS double)) "
                    + "FROM Store s "
                    + "JOIN FETCH s.category "
                    + "LEFT JOIN Orders o ON o.store = s "
                    + "LEFT JOIN Review r ON r.order = o "
                    + "WHERE 1 = 1 ";
        base += "AND o.orderStatus = :orderStatus ";
        base += "AND s.storeStatus = :storeStatus ";
        if(location != null) {
            base += "AND ST_CONTAINS(ST_BUFFER(:location, 4000), s.location) ";
        }

        base += "GROUP BY s";
        if(orderBy == OrderBy.DISTANCE) {
            base += " ORDER BY ST_Distance_Sphere(s.location, :location) ";
        }
        if(orderBy == OrderBy.RATE) {
            base += " ORDER BY AVG(r.rate) DESC ";
        }

        TypedQuery<StoreResponseDto> query = entityManager.createQuery(base, StoreResponseDto.class);
        query.setParameter("location", location);
        query.setParameter("orderStatus", OrderStatus.COMPLETED);
        query.setParameter("storeStatus", StoreStatus.OPEN);
        query.setFirstResult(((int) page.getOffset()));
        query.setMaxResults(page.getPageSize());

        return query.getResultList();
    }

    @Override
    public List<StoreResponseDto> findStoresByCategory(Point location,
                                                       Long categoryId,
                                                       Pageable page,
                                                       OrderBy orderBy,
                                                       OrderStatus orderStatus,
                                                       StoreStatus storeStatus) {
        String base = "SELECT new com.example.outsourcing.domain.store.dto.response.StoreResponseDto("
                + "s "
                + ", CAST( ST_Distance_Sphere(s.location, :location) AS double) "
                + ", CAST( COALESCE(AVG(r.rate), 0) AS double)) "
                + "FROM Store s "
                + "JOIN FETCH s.category "
                + "LEFT JOIN Orders o ON o.store = s "
                + "LEFT JOIN Review r ON r.order = o "
                + "WHERE 1 = 1 ";
        base += "AND o.orderStatus = :orderStatus ";
        base += "AND s.storeStatus = :storeStatus ";
        if(location != null) {
            base += "AND ST_CONTAINS(ST_BUFFER(:location, 4000), s.location) ";
        }
        if(categoryId != null) {
            base += "AND s.category.id = :category ";
        }
        base += "GROUP BY s.id ";
        if(orderBy == OrderBy.DISTANCE) {
            base += " ORDER BY ST_Distance_Sphere(s.location, :location) ";
        }
        if(orderBy == OrderBy.RATE) {
            base += " ORDER BY AVG(r.rate) DESC ";
        }

        TypedQuery<StoreResponseDto> query = entityManager.createQuery(base, StoreResponseDto.class);

        query.setParameter("location", location);
        query.setParameter("category", categoryId);
        query.setParameter("orderStatus", orderStatus);
        query.setParameter("storeStatus", storeStatus);
        query.setFirstResult(((int) page.getOffset()));
        query.setMaxResults(page.getPageSize());


        return query.getResultList();
    }

    @Override
    public List<StoreResponseDto> findTopSellerStores(int top,
                                                      Point location,
                                                      OrderStatus orderStatus,
                                                      StoreStatus storeStatus) {
        String base = "SELECT new com.example.outsourcing.domain.store.dto.response.StoreResponseDto("
                + "s "
                + ", CAST( ST_Distance_Sphere(s.location, :location) AS double) "
                + ", CAST( COALESCE(AVG(r.rate), 0) AS double)) "
                + "FROM Store s "
                + "JOIN FETCH s.category "
                + "LEFT JOIN Orders o ON o.store = s "
                + "LEFT JOIN Review r ON r.order = o "
                + "LEFT JOIN Menu m ON m.store = s "
                + "WHERE 1 = 1 ";
        base += "AND o.orderStatus = :orderStatus ";
        base += "AND s.storeStatus = :storeStatus ";
        base += "AND o.orderAt BETWEEN :start AND :end ";
        if(location != null) {
            base += "AND ST_CONTAINS(ST_BUFFER(:location, 4000), s.location) ";
        }
        base += "GROUP BY s ";
        base += "ORDER BY count(o) DESC ";

        TypedQuery<StoreResponseDto> query = entityManager.createQuery(base, StoreResponseDto.class);

        query.setMaxResults(top);
        query.setParameter("location", location);

        LocalDateTime start = LocalDateTime.now().minusHours(5);
        LocalDateTime end = LocalDateTime.now();
        query.setParameter("end", end);
        query.setParameter("start", start);
        query.setParameter("orderStatus", orderStatus);
        query.setParameter("storeStatus", storeStatus);

        return query.getResultList();
    }

    @Override
    public List<StoreResponseDto> findTopNearStores(int top,
                                                    Point location,
                                                    OrderStatus orderStatus,
                                                    StoreStatus storeStatus) {
        String base = "SELECT new com.example.outsourcing.domain.store.dto.response.StoreResponseDto("
                + "s "
                + ", CAST( ST_Distance_Sphere(s.location, :location) AS double) "
                + ", CAST( COALESCE(AVG(r.rate), 0) AS double)) "
                + "FROM Store s "
                + "JOIN FETCH s.category "
                + "LEFT JOIN Orders o ON o.store = s "
                + "      AND o.orderStatus = :orderStatus "
                + "LEFT JOIN Review r ON r.order = o "
                + "WHERE 1 = 1 ";
        base += "AND o.orderStatus = :orderStatus ";
        base += "AND s.storeStatus = :storeStatus ";

        if(location != null) {
            base += "AND ST_CONTAINS(ST_BUFFER(:location, 4000), s.location) ";
        }
        base += "GROUP BY s ";
        base += "ORDER BY ST_Distance_Sphere(s.location, :location) ASC ";

        TypedQuery<StoreResponseDto> query = entityManager.createQuery(base, StoreResponseDto.class);

        query.setMaxResults(top);
        query.setParameter("location", location);
        query.setParameter("orderStatus", orderStatus);
        query.setParameter("storeStatus", storeStatus);

        return query.getResultList();
    }
}
