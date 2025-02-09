package com.TwinStar.TwinStar.comment.service;

import com.TwinStar.TwinStar.comment.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CommentReadService {
    private final CommentRepository commentRepository;

    public CommentReadService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

}
