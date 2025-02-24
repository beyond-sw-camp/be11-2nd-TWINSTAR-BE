package com.TwinStar.TwinStar.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepository extends JpaRepository<Post,Long> {

}
