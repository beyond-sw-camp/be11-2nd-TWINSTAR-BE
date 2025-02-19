package com.TwinStar.TwinStar.post_file;

import com.TwinStar.TwinStar.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_file")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 보호
@AllArgsConstructor
@Builder
@ToString(exclude = "post") // 순환 참조 방지
public class PostFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false, length = 1)
    @Builder.Default
    private String isHide = "N"; // "Y" 또는 "N"


    // 파일 숨김 처리
    public void hideFile() {
        if (!"Y".equals(this.isHide)) {
            this.isHide = "Y"; // 기존 파일 숨김 처리
        }
    }

    // 숨겨진 파일 복구
    public void restoreFile() {
        if (!"N".equals(this.isHide)) {
            this.isHide = "N"; // 숨김된 파일 복구
        }
    }


    // 빌더를 통한 객체 생성 시 기본값 유지

    @Builder
    public PostFile(Post post, String fileUrl, String fileType, String isHide) {
        this.post = post;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.isHide = (isHide != null) ? isHide : "N"; // 기본값 설정
    }
}
