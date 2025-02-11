package com.TwinStar.TwinStar.post_file;

import com.TwinStar.TwinStar.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {

    List<PostFile> findByPost(Post post);

    // ✅ 숨겨지지 않은 파일 조회 (isHide = 'N')
    @Query("SELECT pf.fileUrl FROM PostFile pf WHERE pf.post.id = :postId AND pf.isHide = 'N'")
    List<String> findActiveUrlsByPostId(@Param("postId") Long postId);

    // ✅ 기존 파일을 숨김 처리 (isHide = 'Y')
    @Modifying
    @Query("UPDATE PostFile pf SET pf.isHide = 'Y' WHERE pf.fileUrl IN :fileUrls AND pf.post.id = :postId")
    void hideFilesByUrls(@Param("fileUrls") List<String> urls, @Param("postId") Long postId);

    // ✅ 기존 파일을 다시 보이도록 처리 (isHide = 'N')
    @Modifying
    @Query("UPDATE PostFile pf SET pf.isHide = 'N' WHERE pf.fileUrl IN :fileUrls AND pf.post.id = :postId")
    void restoreFilesByUrls(@Param("fileUrls") List<String> urls, @Param("postId") Long postId);
}
