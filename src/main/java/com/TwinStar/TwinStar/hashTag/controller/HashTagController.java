package com.TwinStar.TwinStar.hashTag.controller;


import com.TwinStar.TwinStar.common.dto.CommonDto;
import com.TwinStar.TwinStar.hashTag.domain.HashTag;
import com.TwinStar.TwinStar.hashTag.service.HashTagService;
import com.TwinStar.TwinStar.post.domain.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hashtags")
public class HashTagController {
    private final HashTagService hashTagService;


    public HashTagController(HashTagService hashTagService) {
        this.hashTagService = hashTagService;
    }

//   1. 해시태그 검색- 게시물과 같이 조회됨
    @GetMapping("/{hashtag}")
    public ResponseEntity<?> getPostByHashTag(@PathVariable String hashtag){
        HashTag hashTagEntity = hashTagService.findByName(hashtag);
        if (hashTagEntity == null) {
            return new ResponseEntity<>(new CommonDto(HttpStatus.NOT_FOUND.value(), "해시태그를 찾을 수 없습니다.", null), HttpStatus.NOT_FOUND);
        }
        List<Post> searchHashTag = hashTagService.findPostsByHashTag(hashTagEntity);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "해시태그 검색을 찾았습니다.",searchHashTag),HttpStatus.OK);
    }

//    특정 게시물의 해시태그 조회
    public ResponseEntity<?> getHashTagsByPost(@PathVariable Long postId){
        Post post = hashTagService.findPostById(postId);
        if (post == null) {
            return new ResponseEntity<>(new CommonDto(HttpStatus.NOT_FOUND.value(), "게시물을 찾을 수 없습니다.", null), HttpStatus.NOT_FOUND);
        }
        List<String> hashTags = hashTagService.getHashTagsByPost(post);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "게시물의 해시태그 목록입니다.",hashTags),HttpStatus.OK);
    }

}
