package com.TwinStar.TwinStar.comment.domain;

import com.TwinStar.TwinStar.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

//@Entity
//@Table(name = "comment")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class Comment {
//}
//18번줄부터 삭제.이 주석도 같이 삭제.post import한거 삭제
@Entity
@Table(name = "comment") // 🔥 테이블 이름 명시적 지정
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne // 🔥 Post 엔티티와 관계 설정
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;  // 🔥 postId 대신 Post 객체 사용
}
