package com.TwinStar.TwinStar.post.controller;

import com.TwinStar.TwinStar.common.auth.JwtUtil;
import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.post.dto.PostCreateReqDto;
import com.TwinStar.TwinStar.post.dto.PostUpdateReqDto;
import com.TwinStar.TwinStar.post.service.PostWriteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("post")
public class PostWriteController {
    private final PostWriteService postWriteService;
    private final JwtUtil jwtUtil;

    public PostWriteController(PostWriteService postWriteService, JwtUtil jwtUtil) {
        this.postWriteService = postWriteService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("create")
    public ResponseEntity<?> postCreate(
            @RequestPart("contents") String contents,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart("hashTags") List<String> hashTags,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);

        PostCreateReqDto dto = new PostCreateReqDto(contents, files, null, hashTags); // 파일도 같이 처리
        String postId = postWriteService.postCreate(dto, userId);

        return ResponseEntity.ok("게시글이 성공적으로 생성되었습니다. postId: " + postId);
    }


    @PostMapping("/update")
    public ResponseEntity<?> postUpdate(
            @RequestPart("postId") Long postId,
            @RequestPart("content") String content,
            @RequestPart(value = "postVisibility", required = false) String postVisibility,
            @RequestPart(value = "postFileUrls", required = false) List<String> postFileUrls, // 유지할 파일 목록 추가
            @RequestPart(value = "newFiles", required = false) List<MultipartFile> newFiles,
            @RequestPart(value = "filesToHide", required = false) List<String> filesToHide, // 삭제할 파일 목록 추가
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);

        // 기존 업로드된 파일 URL 조회 (DB 또는 서비스에서 가져오기)
        List<String> existingFileUrls = postWriteService.getExistingFileUrls(postId, userId);
        if (existingFileUrls == null) {
            existingFileUrls = new ArrayList<>(); // Null 방지
        }

        // 유지할 파일 URL과 기존 파일 URL 병합
        if (postFileUrls != null) {
            existingFileUrls.addAll(postFileUrls);
        }

        // Enum 변환 (예외 발생 방지)
        Visibility visibilityEnum = Visibility.ALL; // 기본값
        if (postVisibility != null) {
            try {
                visibilityEnum = Visibility.valueOf(postVisibility.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("잘못된 postVisibility 값입니다: " + postVisibility);
            }
        }

        // DTO 생성 (유지할 파일 목록, 새 파일 목록, 삭제할 파일 목록 포함)
        PostUpdateReqDto dto = new PostUpdateReqDto(postId, content, visibilityEnum, existingFileUrls, newFiles, filesToHide);
        postWriteService.postUpdate(dto, userId);

        return ResponseEntity.ok("게시글이 성공적으로 수정되었습니다.");
    }


    @DeleteMapping("delete/{postId}")
    public ResponseEntity<?> postDelete(@PathVariable Long postId,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);

        postWriteService.postDelete(postId, userId);
        return ResponseEntity.ok().build();
    }
}