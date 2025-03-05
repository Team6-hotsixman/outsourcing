package com.example.outsourcing.domain.common.controller;

import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.common.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageUploadController {

    private final ImageService imageService;

    /**
     * 이미지 업로드 API
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = imageService.uploadFile(file);
        return ResponseEntity.ok(imageUrl);
    }

    /**
     * ID로 이미지 조회 API
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getImage(@PathVariable Long id) {
        return ResponseEntity.ok(imageService.getImageById(id));
    }

    //파일 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("fileName") String fileName) {
        imageService.deleteFile(fileName);
        return ResponseEntity.ok("Deleted: " + fileName);
    }
}
