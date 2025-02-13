package com.TwinStar.TwinStar.post.dto;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.post_file.PostFile;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateReqDto {
    private Long postId;  // 수정할 게시글 ID
    private String content;
    private List<String> postFileUrls;  // ✅ 파일을 여러 개 저장할 수 있도록 변경
    private Visibility postVisibility;
}