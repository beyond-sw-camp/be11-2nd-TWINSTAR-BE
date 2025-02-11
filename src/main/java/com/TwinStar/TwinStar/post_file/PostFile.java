package com.TwinStar.TwinStar.post_file;

import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.report.domain.Type;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_file")
@Data
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

    @Column(nullable = false)
    @Builder.Default
    private String isHide = "N"; // ✅ String 타입으로 변경

    // ✅ 파일 숨김 처리
    public void hideFile() {
        this.isHide = "Y";
    }

    // ✅ 파일 공개 처리
    public void restoreFile() {
        this.isHide = "N";
    }
}
