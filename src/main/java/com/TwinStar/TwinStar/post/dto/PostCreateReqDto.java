package com.TwinStar.TwinStar.post.dto;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostCreateReqDto {
    private String content;
    private List<String> hashTag;
    private List<MultipartFile>imageFile;
    private Visibility visibility;

    public Post toEntity(User user){
        return Post.builder()
                .content(this.content)
                .visibility(this.visibility)
                .user(user)
                .score(0L)
                .build();
    }
}
