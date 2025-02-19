package com.TwinStar.TwinStar.hashTag.domain;


import com.TwinStar.TwinStar.post.domain.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class PostHashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private HashTag hashTag;

    public PostHashTag(Post post,HashTag hashTag){
        this.hashTag = hashTag;
        this.post = post;
    }

    public void updatePost(Post post) {
        this.post = post;
    }

    // ✅ HashTag를 받는 생성자 추가
    public PostHashTag(HashTag hashTag) {
        this.hashTag = hashTag;
    }

    // ✅ post를 설정하는 메서드 추가
    public void associatePost(Post post) {
        if (this.post == null) {  // 중복 설정 방지
            this.post = post;
            post.getPostHashTags().add(this); // Post 엔티티에도 추가 (양방향 관계 유지)
        }
    }

}
