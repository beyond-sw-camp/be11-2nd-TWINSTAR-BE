package com.TwinStar.TwinStar.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostCreateReqDto {
    private String content;
//    태그, 이미지파일, 공개범위
}
