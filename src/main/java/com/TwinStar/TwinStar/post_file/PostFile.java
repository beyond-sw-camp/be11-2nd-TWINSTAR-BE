package com.TwinStar.TwinStar.post_file;

import com.TwinStar.TwinStar.common.domain.YN;
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

    @Column(nullable = false)
    private YN isHide = YN.N; // 노출 시킬 파일 여부

    public void prePersist() {
        if (this.isHide == null) {
            this.isHide = YN.N; // 기본값 설정
        }
    }

    // 파일 숨김 처리
    public void hideFile(){
        this.isHide = YN.Y;
    }

    // 파일 공개 처리
    public void restoreFile(){
        this.isHide = YN.N;
    }


}

