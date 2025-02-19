package com.TwinStar.TwinStar.comment.dto;

import com.TwinStar.TwinStar.comment.domain.Comment;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Long id;
    private String content;
    private String nickName;
    private String profileImg;
    private String createdTime;
    private String updatedTime;

    public static CommentResponseDto fromEntity(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getDelYn().equals("Y") ? "삭제된 댓글입니다." : comment.getContent()) // 삭제 된 댓글일 경우 내용 변경
                .nickName(comment.getUser().getNickName())
                .profileImg(comment.getUser().getProfileImg())
                .createdTime(comment.getCreatedTime().toString())
                .updatedTime(comment.getUpdatedTime() != null ? comment.getUpdatedTime().toString() : null)
                .build();
    }

}