package com.TwinStar.TwinStar.comment.service;

import com.TwinStar.TwinStar.comment.domain.Comment;
import com.TwinStar.TwinStar.comment.dto.CommentCreateReqDto;
import com.TwinStar.TwinStar.comment.dto.CommentUpdateReqDto;
import com.TwinStar.TwinStar.comment.dto.ReplyCommentCreateReqDto;
import com.TwinStar.TwinStar.comment.repository.CommentRepository;
import com.TwinStar.TwinStar.post.domain.Post;
import com.TwinStar.TwinStar.post.repository.PostRepository;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Long create(CommentCreateReqDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()-> new EntityNotFoundException("user is not found."));
        Post post = postRepository.findById(dto.getPostId()).orElseThrow(()-> new EntityNotFoundException("post is not found."));
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(dto.getContent())
                .build();
        commentRepository.save(comment);
        return dto.getPostId();
    }

    public Long update(CommentUpdateReqDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()->new EntityNotFoundException("user not found"));
        Comment comment = commentRepository.findById(dto.getCommentId()).orElseThrow(()-> new EntityNotFoundException("comment is not found."));
        User commentWriteUser = comment.getUser();

        if (!loginUser.equals(commentWriteUser)){ return 0L; }
        comment.updateContent(dto.getContent());
        commentRepository.save(comment);

        return comment.getPost().getId();
    }

    public Long delete(Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()->new EntityNotFoundException("user not found"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new EntityNotFoundException("comment is not found."));
        User commentWriteUser = comment.getUser();

        if (!loginUser.equals(commentWriteUser)){ return 0L; }
        comment.delete();
        commentRepository.save(comment);

        return comment.getPost().getId();
    }

    public Long replyCreate(ReplyCommentCreateReqDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()->new EntityNotFoundException("user not found"));
        Comment parent = commentRepository.findById(dto.getParentId()).orElseThrow(()-> new EntityNotFoundException("comment is not found."));
        Post post = commentRepository.findPostByParentId(parent.getId());

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .parent(parent)
                .content(dto.getContent())
                .build();
        commentRepository.save(comment);

        parent.addChild(comment);
        return post.getId();
    }
}
