package com.TwinStar.TwinStar.post.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostDetailRes {
    private Long id;
    private String contents;
    private String user;
    private Long groupId;
    private Long sharePostId;
    private Enum postVisibility;
    private int postDel;
    private Long score;
    private int hotIssueYn;
}
