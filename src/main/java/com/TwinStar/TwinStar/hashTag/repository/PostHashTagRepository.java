package com.TwinStar.TwinStar.hashTag.repository;


import com.TwinStar.TwinStar.hashTag.domain.PostHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostHashTagRepository extends JpaRepository<PostHashTag,Long> {
//    PostHashTag와 post를 조인하고 해시태그이름이 있는 것을 posthashtag타입으로 반환 
    @Query("SELECT pht FROM PostHashTag pht JOIN FETCH pht.post WHERE pht.hashTag.hashTagName = :hashTag")
    List<PostHashTag> findByHashTag(@Param("hashTag") String hashTag);
}
