package com.TwinStar.TwinStar.post.controller;

import com.TwinStar.TwinStar.common.dto.CommonDto;
import com.TwinStar.TwinStar.post.dto.PostCreateReqDto;
import com.TwinStar.TwinStar.post.dto.PostUpdateReqDto;
import com.TwinStar.TwinStar.post.dto.PostUpdateResDto;
import com.TwinStar.TwinStar.post.service.LikeService;
import com.TwinStar.TwinStar.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RequestMapping("/post")
@RestController
public class PostController {

    private final PostService postService;
    private final LikeService likeService;

    public PostController(PostService postService, LikeService likeService) {
        this.postService = postService;
        this.likeService = likeService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@ModelAttribute PostCreateReqDto dto){
        Long postId = postService.save(dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "게시물 작성 완료",postId),HttpStatus.OK);
    }

    @PostMapping("delete/{postId}")
    public ResponseEntity<?> delete(@PathVariable Long postId){
        postService.delete(postId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "게시물 삭제 완료", postId), HttpStatus.OK);
    }

    @GetMapping("/update/{postId}")
    public ResponseEntity<?> getUpdate(@PathVariable Long postId){
        PostUpdateResDto dto = postService.getUpdateDataRes(postId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "게시물 데이터 반환",dto),HttpStatus.OK);
    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<?> patchUpdate(@PathVariable Long postId, @RequestBody PostUpdateReqDto dto) {
        postService.Update(postId, dto); // postId가 있으면 기존 게시물 수정
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "게시물 수정 완료", postId), HttpStatus.OK);
    }


    @PostMapping("/like/{postId}")
    public ResponseEntity<?> postLike(@PathVariable Long postId){
        Map<String, Object> likeInfo = likeService.toggleLike(postId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"기능 성공",likeInfo),HttpStatus.OK);
    }


}
