package com.example.outsourcing.domain.review.repository;

import com.example.outsourcing.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
