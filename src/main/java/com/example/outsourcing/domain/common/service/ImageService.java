package com.example.outsourcing.domain.common.service;

import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public Image getImageById(Long imageId) {
        // 예외처리 추가 필요
        return imageRepository.findById(imageId).orElseThrow();
    }
}
