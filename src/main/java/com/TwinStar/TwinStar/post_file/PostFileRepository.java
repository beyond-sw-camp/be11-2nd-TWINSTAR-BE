package com.TwinStar.TwinStar.post_file;

import com.TwinStar.TwinStar.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {

    List<PostFile> findByPostId(Long postId);

    @Modifying
    @Query("UPDATE PostFile pf SET pf.isHide = :isHideValue WHERE pf.fileUrl IN :fileUrls AND pf.post.id = :postId")
    void hideFilesByUrls(@Param("fileUrls") List<String> urls, @Param("postId") Long postId, @Param("isHideValue") String isHideValue);

    @Modifying
    @Query("UPDATE PostFile pf SET pf.isHide = :isHideValue WHERE pf.fileUrl IN :fileUrls AND pf.post.id = :postId")
    void restoreFilesByUrls(@Param("fileUrls") List<String> urls, @Param("postId") Long postId, @Param("isHideValue") String isHideValue);
}