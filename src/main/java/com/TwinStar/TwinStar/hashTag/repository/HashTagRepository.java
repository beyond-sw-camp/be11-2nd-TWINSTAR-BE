package com.TwinStar.TwinStar.hashTag.repository;

import com.TwinStar.TwinStar.hashTag.domain.HashTag;
import com.TwinStar.TwinStar.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {
//    해시태그 조회
    Optional<HashTag> findByHashTagName(String hasTagName);
    

}
