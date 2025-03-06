package com.example.outsourcing.domain.common.service;

import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
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




    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    //S3에 파일 업로드
    public Image uploadFile(MultipartFile file) {

        Image image = new Image();
        image = imageRepository.save(image);

        String fileName = image.getId() + "_" + file.getOriginalFilename(); // 고유한 파일명 생성

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType(file.getContentType())  // MIME 타입 지정
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );
            String imagePath = getFileUrl(fileName);
            image.setFilename(fileName);
            image.setImagePath(imagePath);
            imageRepository.save(image);

            return image;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }

    }

    public Image getImageById(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow();
    }

    /**
     * S3 파일 URL 생성
     */
    public String getFileUrl(String fileName) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
    }

    //S3에서 파일 삭제
    public void deleteFile(String fileName) {
        Image image = imageRepository.findByFilename(fileName)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_IMAGE));

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build());

        imageRepository.delete(image);

    }
}
