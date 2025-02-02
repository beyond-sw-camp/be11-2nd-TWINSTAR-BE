package com.TwinStar.TwinStar.admin.dtos;

import com.TwinStar.TwinStar.commonDomain.PostStatus;
import com.TwinStar.TwinStar.commonDomain.PostVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AdminPostDetailDto {
    private Long id;
    private String nickName;
    private String content;
    private Long sharePostId;
    private PostVisibility postVisibility;
    private String postDel;
    private String hotIssueYn;
    private PostStatus postStatus;
}

