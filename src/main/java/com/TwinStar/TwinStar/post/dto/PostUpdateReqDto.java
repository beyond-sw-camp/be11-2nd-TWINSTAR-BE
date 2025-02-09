package com.TwinStar.TwinStar.post.dto;

import com.TwinStar.TwinStar.common.domain.Visibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateReqDto {

    private String content;
    private String postFileUrl;
    private String profileImgUrl;
    private Visibility visibility;

}
