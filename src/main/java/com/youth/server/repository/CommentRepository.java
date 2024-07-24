package com.youth.server.repository;

import com.youth.server.domain.Comment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    Set<Comment> findAllByFestivalId(int festivalId, PageRequest of);

    Set<Comment> findAllByAuthorId(int userId);
}
