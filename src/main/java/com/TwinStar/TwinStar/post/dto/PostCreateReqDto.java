package com.TwinStar.TwinStar.post.dto;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostCreateReqDto {
    private String contents;
    private String file;
    private String profileImgUrl;
    private String postFileUrl;
    private Visibility visibility;
    public Post toEntity(User user) {
        return Post.builder()
                .content(this.contents)
                .user(user)
                .profileImgUrl(this.profileImgUrl)
                .postFileUrl(this.postFileUrl)
                .visibility(this.visibility)
                .build();
    }
}
