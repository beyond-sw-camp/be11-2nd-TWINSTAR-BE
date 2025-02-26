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
    private List<String> hashTag;
    private String isLike;
    private String isFollow;

    public static PostDetailResDto fromEntity(Post post, Long postLikeCount, List<CommentListResDto> commentList, List<String> hashTags, String isLike, String isFollow) {
        return PostDetailResDto.builder()
                .userId(post.getUser().getId())
                .nickName(post.getUser().getNickName())
                .profileImage(post.getUser().getProfileImg())
                .postId(post.getId())
                .imageList(post.getFileUrls())
                .content(post.getContent())
                .postLikeCount(postLikeCount)
                .commentList(commentList)
                .createdTime(post.getCreatedTime())
                .isUpdate(determineUpdateStatus(post))
                .hashTag(hashTags)
                .isLike(isLike)
                .isFollow(isFollow)
                .build();
    }

    // 수정 여부 판단 (createdTime과 updatedTime 비교)
    private static String determineUpdateStatus(Post post) {
        return (post.getUpdatedTime() != null && !post.getUpdatedTime().equals(post.getCreatedTime())) ? "Y" : "N";
    }
}
