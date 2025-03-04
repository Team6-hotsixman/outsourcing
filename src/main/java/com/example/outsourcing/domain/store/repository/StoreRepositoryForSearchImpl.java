package com.example.outsourcing.domain.store.repository;

import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.enums.OrderBy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryForSearchImpl implements StoreRepositoryForSearch{
    private final EntityManager entityManager;

    @Override
    public List<StoreResponseDto> findStoresByArea(Point area, Pageable page, OrderBy orderBy) {
        String base = "SELECT new com.example.outsourcing.domain.store.dto.response.StoreResponseDto("
                    + "s "
                    + ", CAST( ST_Distance_Sphere(s.location, :area) AS double) "
                    + ", CAST( COALESCE(AVG(r.rate), 0) AS double)) "
                    + "FROM Store s "
                    + "LEFT JOIN Orders o ON o.store = s "
                    + "LEFT JOIN Review r ON r.order = o "
                    + "WHERE 1 = 1 ";
        if(area != null) {
            base += "AND ST_CONTAINS(ST_BUFFER(:area, 3000), s.location) ";
        }

        base += "GROUP BY s";
        if(orderBy == OrderBy.DISTANCE) {
            base += " ORDER BY ST_Distance_Sphere(s.location, :area) ";
        }
        if(orderBy == OrderBy.RATE) {
            base += " ORDER BY AVG(r.rate) DESC ";
        }

        TypedQuery<StoreResponseDto> query = entityManager.createQuery(base, StoreResponseDto.class);
        query.setParameter("area", area);
        query.setFirstResult(((int) page.getOffset()));
        query.setMaxResults(page.getPageSize());

        return query.getResultList();
    }

    @Override
    public List<StoreResponseDto> findStoresByCategory(Point area, Long categoryId, Pageable page, OrderBy orderBy) {
        String base = "SELECT new com.example.outsourcing.domain.store.dto.response.StoreResponseDto("
                + "s "
                + ", CAST( ST_Distance_Sphere(s.location, :area) AS double) "
                + ", CAST( COALESCE(AVG(r.rate), 0) AS double)) "
                + "FROM Store s "
                + "LEFT JOIN Category c ON s.category = c "
                + "LEFT JOIN Orders o ON o.store = s "
                + "LEFT JOIN Review r ON r.order = o "
                + "WHERE 1 = 1 ";
        if(area != null) {
            base += "AND ST_CONTAINS(ST_BUFFER(:area, 3000), s.location) ";
        }
        if(categoryId != null) {
            base += "AND c.id = :category ";
        }
        base += "GROUP BY s.id ";
        if(orderBy == OrderBy.DISTANCE) {
            base += " ORDER BY ST_Distance_Sphere(s.location, :area) ";
        }
        if(orderBy == OrderBy.RATE) {
            base += " ORDER BY AVG(r.rate) DESC ";
        }
        TypedQuery<StoreResponseDto> query = entityManager.createQuery(base, StoreResponseDto.class);
        query.setParameter("area", area);
        query.setParameter("category", categoryId);
        query.setFirstResult(((int) page.getOffset()));
        query.setMaxResults(page.getPageSize());

        return query.getResultList();
    }

    @Override
    public List<StoreResponseDto> findStoresBySearch(Point area, String search, Pageable page, OrderBy orderBy) {
        String base = "SELECT new com.example.outsourcing.domain.store.dto.response.StoreResponseDto("
                + "s "
                + ", CAST( ST_Distance_Sphere(s.location, :area) AS double) "
                + ", CAST( COALESCE(AVG(r.rate), 0) AS double)) "
                + "FROM Store s "
                + "LEFT JOIN Orders o ON o.store = s "
                + "LEFT JOIN Review r ON r.order = o "
                + "LEFT JOIN Menu m ON m.store = s "
                + "WHERE 1 = 1 ";

        //next 정보 넣기
        if(area != null) {
            base += "AND ST_CONTAINS(ST_BUFFER(:area, 3000), s.location) ";
        }
        if (search != null && !search.trim().isEmpty()) {
            base += "AND (s.storeName LIKE CONCAT('%', :search, '%') OR  m.menuName LIKE CONCAT('%', :search, '%') ) ";
        }

        base += "GROUP BY s ";
        if(orderBy == OrderBy.DISTANCE) {
            base += " ORDER BY ST_Distance_Sphere(s.location, :area) ";
        }
        if(orderBy == OrderBy.RATE) {
            base += " ORDER BY AVG(r.rate) DESC ";
        }

        TypedQuery<StoreResponseDto> query = entityManager.createQuery(base, StoreResponseDto.class);
        query.setParameter("area", area);
        query.setParameter("search", search);
        query.setFirstResult(((int) page.getOffset()));
        query.setMaxResults(page.getPageSize());

        return query.getResultList();
    }
}
