package com.example.outsourcing.domain.common.repository;

import com.example.outsourcing.domain.common.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByFilename(String filename); // 파일명으로 이미지 찾기
}
