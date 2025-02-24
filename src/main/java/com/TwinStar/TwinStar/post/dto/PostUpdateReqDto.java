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
    private List<String> hashTag;
    private Visibility visibility;
}
