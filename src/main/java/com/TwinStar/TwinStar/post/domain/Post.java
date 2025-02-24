package com.TwinStar.TwinStar.post.domain;

import com.TwinStar.TwinStar.chat.domain.ReadStatus;
import com.TwinStar.TwinStar.comment.domain.Comment;
import com.TwinStar.TwinStar.common.domain.BaseTimeEntity;
import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.hashTag.domain.PostHashTag;
import com.TwinStar.TwinStar.post.dto.PostUpdateResDto;
import com.TwinStar.TwinStar.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 500)
    private String content;

    @Builder.Default
    private String postDel = "N";

    private Visibility visibility;

    private Long score;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PostLike> PostLike = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PostFile> postFile = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PostHashTag> hashTag = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comment = new ArrayList<>();

    public void updateContent(String content){
        this.content = content;
    }

    public PostUpdateResDto formEntity(List<String> hashTag, List<String> imageFile){
        return PostUpdateResDto.builder()
                .content(this.content)
                .visibility(this.visibility)
                .hashTag(hashTag)
                .imageFile(imageFile)
                .build();
    }

    public List<String> getFileUrls() {
        return postFile.stream()
                .map(PostFile::getFileUrl)
                .collect(Collectors.toList());
    }
}
