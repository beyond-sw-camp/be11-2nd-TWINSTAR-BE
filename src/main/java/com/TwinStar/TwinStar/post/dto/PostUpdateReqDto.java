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
    private List<String> postFileUrls; // 유지할 파일 목록
    private List<MultipartFile> newFiles; // 새로 추가할 파일 목록
    private List<String> filesToHide; // 사용자가 삭제 요청한 파일 목록
}
