package com.TwinStar.TwinStar.post.dto;

import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.post_file.PostFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostResponseDto {
    private Long id;
    private String content;
    private Visibility postVisibility;
    private List<String> postFileUrls;

    public static PostResponseDto from(Post post){
        return PostResponseDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .postVisibility(post.getPostVisibility())
                .postFileUrls(post.getPostFiles().stream()
                        .map(PostFile::getFileUrl) // ✅ PostFile에서 fileUrl 가져오기
                        .collect(Collectors.toList()))
                .build();
    }

}
