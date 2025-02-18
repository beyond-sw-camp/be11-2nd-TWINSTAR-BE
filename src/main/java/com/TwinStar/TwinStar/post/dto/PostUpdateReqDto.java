package com.TwinStar.TwinStar.post.dto;

import com.TwinStar.TwinStar.common.domain.Visibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostUpdateReqDto {
    private Long postId;
    private String content;
    private Visibility postVisibility;
    private List<String> postFileUrls;  // 기존 파일 URL
    private List<MultipartFile> newFiles; // 새 파일을 위한 MultipartFile 리스트
}
