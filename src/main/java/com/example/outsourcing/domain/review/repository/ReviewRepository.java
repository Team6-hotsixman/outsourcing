package com.example.outsourcing.domain.review.repository;

import com.example.outsourcing.domain.review.entity.Review;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r " +
            " from Review r " +
            " join fetch r.user " +
            " join fetch r.order " +
            "where r.createdAt < :last " +
            "  and r.order.store.id = :storeId " +
            "  and r.rate >= :start " +
            "  and r.rate <= :end " +
            "order by r.id desc " +
            "limit 10 ")
    List<Review> findReviewsByStoreId(Long storeId, int start, int end, LocalDateTime last);

    @Query("select r from Review r join fetch r.user join fetch r.order where r.id = :reviewId ")
    Optional<Review> findByIdWithUserAndOrder(long reviewId);
}
