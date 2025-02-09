package com.TwinStar.TwinStar.comment.service;

import com.TwinStar.TwinStar.comment.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class CommentWriteService {
    private final CommentRepository commentRepository;

    public CommentWriteService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

}
