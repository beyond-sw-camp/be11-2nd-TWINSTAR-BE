package com.TwinStar.TwinStar.post.dto;

import com.TwinStar.TwinStar.comment.domain.Comment;
import com.TwinStar.TwinStar.comment.domain.CommentLike;
import com.TwinStar.TwinStar.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProfilePostResDto {
    private Long postId;
    private String imageUrl;
    private Long likeCount;
    private Long commentCount;

    public static ProfilePostResDto fromEntity(Long postId, String url, Long likeCount, Long commentCount){
        return ProfilePostResDto.builder()
                .postId(postId)
                .imageUrl(url)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();
    }
}
