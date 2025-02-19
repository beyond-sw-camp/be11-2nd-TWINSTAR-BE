package com.TwinStar.TwinStar.post.repository;

import com.TwinStar.TwinStar.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 사용자의 게시글을 ID로 찾기
    Optional<Post> findByIdAndUserId(Long postId, Long userId);

    // 사용자가 팔로우한 사람들의 최신 게시물 조회
    @Query("SELECT p FROM Post p WHERE p.user IN (SELECT f.receiveUserId FROM Follow f WHERE f.userId = :userId) ORDER BY p.createdTime DESC")
    List<Post> findHomeFeed(@Param("userId") Long userId);


    @Query("SELECT p FROM Post p WHERE p.user.id = :userId ORDER BY p.createdTime DESC")
    List<Post> findByUserId(@Param("userId") Long userId);

    // 핫 게시물 조회 (hotIssueYn이 'Y'인 게시물, 좋아요 및 댓글 수 기준 정렬)
    @Query("SELECT p FROM Post p WHERE p.hotIssueYn = 'Y' ORDER BY p.score DESC, p.createdTime DESC")
    List<Post> findHotPosts();

    // 게시물 검색 (내용에 키워드 포함, 무작위 정렬)
    @Query("SELECT p FROM Post p WHERE p.content LIKE %?1% ORDER BY FUNCTION('RAND')")
    List<Post> searchPosts(String query);
}
