package kr.or.ddit.util.file.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class S3Uploader {
	private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public String upload(MultipartFile multipartFile, String changeFileName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(uploadFile, changeFileName);
    }

    private String upload(File uploadFile, String changeFileName) {
        String uploadImageUrl = putS3(uploadFile, changeFileName);
        removeNewFile(uploadFile); // 로컬에 생성된 File 삭제
        return uploadImageUrl;
    }

    // 실질적인 s3 업로드 부분
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("임시 파일 삭제 성공: {}", targetFile.getAbsolutePath());
        } else {
            log.warn("임시 파일 삭제 실패: {}", targetFile.getAbsolutePath());
        }
    }

    // 개선된 MultipartFile -> File 변환 방식
    private Optional<File> convert(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String ext = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        File tempFile = File.createTempFile("upload_", ext);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }

        return Optional.of(tempFile);
    }

    // 파일 삭제
    public void fileDelete(String s3Key) {
        amazonS3Client.deleteObject(bucket, s3Key);
        log.info("S3 삭제 완료: {}", s3Key);
    }

    public S3Object getObject(String s3Key) {
        return amazonS3Client.getObject(bucket, s3Key);
    }

    public String generatePresignedUrl(String key, int expirationInMinutes) {
        Date expiration = new Date(System.currentTimeMillis() + expirationInMinutes * 60 * 1000L); // 현재 시간 + N분
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        URL url = amazonS3Client.generatePresignedUrl(request);
        return url.toString();
    }
}
