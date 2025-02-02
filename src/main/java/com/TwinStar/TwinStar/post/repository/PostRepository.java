package com.TwinStar.TwinStar.post.repository;


import com.TwinStar.TwinStar.post.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository {

    // 제목으로 게시글 검색
    List<Post> findByTitleContaining(String title);

    // 작성자로 게시글 검색
    List<Post> findByAuthor(String author);

    // 카테고리별 게시글 검색
    List<Post> findByCategory(String category);

    // 최신 게시글 n개 조회
    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    List<Post> findLatestPosts(Pageable pageable);

    // 조회수 기준 인기 게시글
    @Query("SELECT p FROM Post p ORDER BY p.viewCount DESC")
    List<Post> findPopularPosts(Pageable pageable);

    // 특정 기간 내의 게시글 검색
    List<Post> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}

