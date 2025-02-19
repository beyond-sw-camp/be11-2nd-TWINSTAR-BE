package com.TwinStar.TwinStar.post.dto;

import com.TwinStar.TwinStar.comment.dto.CommentResponseDto;
import com.TwinStar.TwinStar.post.domain.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailResponseDto {
    private Long id;
    private String content;
    private String nickName;
    private String profileImg;
    private int likesCount;
    private int commentsCount;
    private LocalDateTime createdTime;
    private List<CommentResponseDto> comments;

    public static PostDetailResponseDto fromEntity(Post post) {
        return PostDetailResponseDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .nickName(post.getUser().getNickName())
                .profileImg(post.getUser().getProfileImg())
                .likesCount(post.getScore())
                .commentsCount(post.getComments().size())
                .createdTime(post.getCreatedTime())
                .comments(post.getComments().stream()
                        .map(CommentResponseDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
