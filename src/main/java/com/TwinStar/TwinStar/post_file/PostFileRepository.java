package com.TwinStar.TwinStar.post_file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {
    List<PostFile> findByPostId(Long postId);
    List<PostFile> deleteByPostId(Long postId);
}

