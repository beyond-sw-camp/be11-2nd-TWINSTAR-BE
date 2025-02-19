package com.TwinStar.TwinStar.post.service;

import com.TwinStar.TwinStar.post.dto.*;
import com.TwinStar.TwinStar.post.repository.*;
import com.TwinStar.TwinStar.commentLike.CommentLikeRepository;
import com.TwinStar.TwinStar.postLike.PostLikeRepository;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostReadService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;

    // 홈 피드 조회: 사용자가 팔로우한 사람들의 최신 게시물을 반환
    public List<PostResponseDto> getHomeFeed(Long userId) {
        return postRepository.findHomeFeed(userId).stream()
                .map(PostResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 유저의 게시물 조회
    public List<PostResponseDto> getUserPosts(Long userId) {
        return postRepository.findByUserId(userId).stream()
                .map(PostResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 내 게시물 조회
    public List<PostResponseDto> getMyPosts(Long userId) {
        return postRepository.findByUserId(userId).stream()
                .map(PostResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 핫 게시물 조회: 좋아요 및 댓글이 많은 인기 게시물 반환
    public List<PostResponseDto> getHotPosts() {
        return postRepository.findHotPosts().stream()
                .map(PostResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 게시물 상세 조회
    public PostDetailResponseDto getPostDetail(Long postId) {
        return postRepository.findById(postId)
                .map(PostDetailResponseDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    // 게시물 검색: 키워드를 포함하는 게시물 목록 반환
    public List<PostResponseDto> searchPosts(String query) {
        return postRepository.searchPosts(query).stream()
                .map(PostResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
