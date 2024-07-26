package com.youth.server.repository;

import com.youth.server.domain.Comment;
import com.youth.server.dto.CommentDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    @Query("SELECT new com.youth.server.dto.CommentDTO(c.id, c.content, c.author.userId, c.updatedAt) FROM Comment c WHERE c.festival.id = :festivalId")
    Set<CommentDTO> findAllByFestivalId(int festivalId, PageRequest of);

    Set<Comment> findAllByAuthorId(int userId);
}
