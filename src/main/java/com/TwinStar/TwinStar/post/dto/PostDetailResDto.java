package com.TwinStar.TwinStar.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostDetailResDto {
    private Long userId;
    private String nickName;
    private String profileImage;
    private Long postId;
    private List<String> imageList;
    private String content;
    private Long postLikeCount;
    private List<CommentListResDto> commentList;
    private LocalDateTime createdTime;
    private String isUpdate;
}
