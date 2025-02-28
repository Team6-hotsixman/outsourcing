package com.example.outsourcing.domain.common.repository;

import com.example.outsourcing.domain.common.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
