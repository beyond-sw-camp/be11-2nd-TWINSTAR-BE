package com.TwinStar.TwinStar.post.dto;

import com.TwinStar.TwinStar.comment.domain.Comment;
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
public class CommentListResDto {
    private Long id;
    private Long userId;
    private Long parentId;
    private String nickName;
    private String profileImage;
    private String content;
    private Long likeCount;
    private String isPinned;
    private String isUpdate;
    private String isDelete;
    private String isLike;
    private LocalDateTime createdTime;

    public static CommentListResDto fromEntity(Comment comment, Long likeCount, String isLike) {
        return CommentListResDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .nickName(comment.getUser().getNickName())
                .profileImage(comment.getUser().getProfileImg())
                .content(comment.getContent())
                .likeCount(likeCount)
                .isPinned(comment.getPinnedComment())
                .isUpdate(determineUpdateStatus(comment))
                .isDelete(comment.getCommentDel())
                .isLike(isLike)
                .createdTime((comment.getCreatedTime()))
                .build();
    }

    // 수정 여부 판단
    private static String determineUpdateStatus(Comment comment) {
        return (comment.getUpdatedTime() != null && !comment.getUpdatedTime().equals(comment.getCreatedTime())) ? "Y" : "N";
    }
}
