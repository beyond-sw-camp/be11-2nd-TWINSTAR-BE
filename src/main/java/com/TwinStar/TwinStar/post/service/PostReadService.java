package com.TwinStar.TwinStar.post.service;

import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.post.dto.HomePostResDto;
import com.TwinStar.TwinStar.post.repository.PostRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostReadService {

    private final PostRepository postRepository;

    public PostReadService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    //    home화면에서 post 리스트 보여주는 메서드
    public Page<HomePostResDto> getHomePost(Long userId) {
        return null;
    }
}
