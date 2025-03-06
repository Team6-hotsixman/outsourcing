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

    @PostMapping("/upload")
    public ResponseEntity<Long> uploadImage(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(imageService.uploadFile(file));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImage(@PathVariable Long id) {
        return ResponseEntity.ok(imageService.getImageById(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("fileName") String fileName) {
        imageService.deleteFile(fileName);
        return ResponseEntity.ok("Deleted: " + fileName);
    }
}
