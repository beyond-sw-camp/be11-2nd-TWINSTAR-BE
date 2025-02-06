package com.TwinStar.TwinStar.post.dto;

import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.post.domain.PostVisibility;
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
    private PostVisibility visibility;


    public Post toEntity(){
        return Post.builder()
                .content(this.content)
                .postFileUrl(this.postFileUrl)
                .profileImgUrl(this.profileImgUrl)
                .visibility(this.visibility)
                .build();

    }
}
