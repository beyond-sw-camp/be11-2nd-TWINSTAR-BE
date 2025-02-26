package com.TwinStar.TwinStar.post.dto;

import com.TwinStar.TwinStar.post.domain.Post;
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
public class PostListResDto {
    private Long userId;
    private String nickName;
    private String profileImage;
    private Long postId;
    private List<String> imageList;
    private String content;
    private Long likeCount;
    private Long commentCount;
    private LocalDateTime createdTime;
    private String isUpdate;
    private List<String> hashTag;
    private String isLike;

    public PostListResDto fromEntity(Post post, Long likeCount, Long commentCount) {
        return PostListResDto.builder()
                .userId(post.getUser().getId())
                .nickName(post.getUser().getNickName())
                .profileImage(post.getUser().getProfileImg())
                .postId(post.getId())
                .imageList(post.getFileUrls())
                .content(post.getContent())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .createdTime(post.getCreatedTime())
                .isUpdate(determineUpdateStatus(post))
                .build();
    }

    // 업데이트 여부 로직 (예제: content가 수정되었는지 확인)
    private String determineUpdateStatus(Post post) {
        if (post.getUpdatedTime() != null && !post.getUpdatedTime().equals(post.getCreatedTime())) {
            return "Y"; // 수정됨
        }
        return "N"; // 수정되지 않음
    }
}
