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

    //파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileUrl = imageService.uploadFile(file);
        return ResponseEntity.ok(fileUrl);
    }

    //image id로 이미지 찾기
    @GetMapping("/{imageId}")
    public ResponseEntity<Image> getImageById(@PathVariable Long imageId) {
        Image image = imageService.getImageById(imageId);
        return ResponseEntity.ok(image);
    }

    //파일 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("fileName") String fileName) {
        imageService.deleteFile(fileName);
        return ResponseEntity.ok("Deleted: " + fileName);
    }
}
