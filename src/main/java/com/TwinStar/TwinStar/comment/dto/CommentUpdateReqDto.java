package com.TwinStar.TwinStar.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentUpdateReqDto {
    private Long commentId;
    private String content;
}
