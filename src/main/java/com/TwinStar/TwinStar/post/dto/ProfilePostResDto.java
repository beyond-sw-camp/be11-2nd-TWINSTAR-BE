package com.TwinStar.TwinStar.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProfilePostResDto {
    private Long id;
    private MultipartFile file;
    private int postLikeCount;
    private int commentCount;
    private int shareCount;
}
