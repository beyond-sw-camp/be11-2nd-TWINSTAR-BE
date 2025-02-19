package com.TwinStar.TwinStar.comment.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDto {
    private String content;
    private Long userId;
}