package com.youth.server.service;

import com.youth.server.domain.Comment;
import com.youth.server.dto.CommentDTO;
import com.youth.server.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Set<CommentDTO> getCommentsByFestivalId(int festivalId, PageRequest of) {
        return commentRepository.findAllByFestivalId(festivalId, of);
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }


    public Set<Comment> getCommentsByUserId(int userId) {
        return commentRepository.findAllByAuthorId(userId);
    }

    public Comment join(Comment comment) {
        return commentRepository.save(comment);
    }
}
