package com.TwinStar.TwinStar.post.dto;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostCreateReqDto {
    private String contents;
    private List<String > files; // 이미지 및 동영상 파일 URL 리스트
    private Visibility postVisibility;

    public void validate() {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("최소 1개의 파일을 업로드해야 합니다.");
        }
        if (files.size() > 10) {
            throw new IllegalArgumentException("최대 10개의 파일만 업로드 가능합니다.");
        }
    }

    public Post toEntity(User user) {
        return Post.builder()
                .content(this.contents)
                .user(user)
                .postVisibility(this.postVisibility != null ? this.postVisibility : Visibility.ALL) // default로 all 삽입
                .build();
    }
}