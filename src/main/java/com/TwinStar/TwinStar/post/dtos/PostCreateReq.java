package com.TwinStar.TwinStar.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostCreateReq {
    private String content;
    private String file;
    private 


    public Post toEntity(User user) {
        return Post.builder()
                .content(content)
                .file(file)
                .user(user.getNickname())
                .build();
    }
    
}
