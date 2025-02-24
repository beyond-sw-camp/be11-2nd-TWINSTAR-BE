package com.TwinStar.TwinStar.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfilePostResDto {
    private Long postId;
    private String imageUrl;
    private Long likeCount;
    private Long commentCount;
}
