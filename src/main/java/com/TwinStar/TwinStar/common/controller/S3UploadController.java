package com.TwinStar.TwinStar.common.controller;

import com.TwinStar.TwinStar.common.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/s3")
public class S3UploadController {

    private final S3Service s3Service;

    public S3UploadController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename(); // 원본 파일명
            String fileUrl = s3Service.uploadFile(multipartFile, fileName);

            return ResponseEntity.ok("S3 업로드 성공: " + fileUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("업로드 실패: " + e.getMessage());
        }
    }
}
