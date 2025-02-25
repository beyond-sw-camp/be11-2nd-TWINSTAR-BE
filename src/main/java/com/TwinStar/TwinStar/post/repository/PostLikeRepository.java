package com.TwinStar.TwinStar.post.repository;

import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.post.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    Long countByPost(Post post);
//    유저가 좋아요를 취소하면 RDB에서도 삭제

}
