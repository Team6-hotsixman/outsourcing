package com.example.outsourcing.domain.common.service;

import com.example.outsourcing.domain.common.repository.ImageRepository;
import com.example.outsourcing.domain.common.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;


    public Image save(MultipartFile image){

        return new Image("good");
    }

    public Image getImageById(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow();
    }

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    //S3에 파일 업로드
    public String uploadFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); // 고유한 파일명 생성

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType(file.getContentType())  // MIME 타입 지정
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );

            return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;  // 저장된 파일 URL 반환
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }

    }

    //S3에서 파일 조회 URL 생성
    public String getFileUrl(String fileName) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
    }

    //S3에서 파일 삭제
    public void deleteFile(String fileName) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build());
    }
}
