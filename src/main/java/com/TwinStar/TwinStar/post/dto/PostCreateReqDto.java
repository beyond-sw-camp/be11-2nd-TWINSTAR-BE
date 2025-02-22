package com.TwinStar.TwinStar.post.dto;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.hashTag.domain.PostHashTag;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostCreateReqDto {
    private String contents;
    private List<MultipartFile> files; // 파일을 MultipartFile 리스트로 받음
    private Visibility postVisibility;
    private List<String> hashTags;

    public Post toEntity(User user, Set<PostHashTag> postHashTags) {
        return Post.builder()
                .content(this.contents)
                .user(user)
                .postVisibility(this.postVisibility != null ? this.postVisibility : Visibility.ALL) // default로 all 삽입
                .postHashtags(new HashSet<>(postHashTags))
                .build();
    }
}