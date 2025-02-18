package com.TwinStar.TwinStar.post.repository;

import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 사용자의 게시글을 ID로 찾기
    Optional<Post> findByIdAndUserId(Long postId, Long userId);

    Optional<Post> findByIdAndPostDel(Long postId, String postDel); // String으로 변경
}
