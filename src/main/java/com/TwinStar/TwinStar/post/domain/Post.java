package com.TwinStar.TwinStar.post.domain;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Entity
@Transactional
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 3000)
    private String contents;

    private String appointment;

    private LocalDateTime appointmentTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public PostListRes postListFromEntity(){
        return PostListRes.builder()
                .id(this.id)
                .title(this.title)
                .userNickname(this.user.getNickname())
                .build();
    }

    public PostDetailRes detailFromEntity(){
        return PostDetailRes.builder()
                .id(this.id)
                .title(this.title)
                .contents(this.contents)
                .userNickname(this.user.getNickname())
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }
}
