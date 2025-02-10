package com.TwinStar.TwinStar.post_file;

import com.TwinStar.TwinStar.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String fileUrl; // 이미지 또는 동영상 URL

    @Column(nullable = false)
    private String fileType; // "image" 또는 "video"

}

