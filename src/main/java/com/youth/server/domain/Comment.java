package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 댓글 정보
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt; // 댓글 작성 날짜

    @NonNull
    @Column(nullable = false)
    private String content; // 댓글 내용

    @NonNull
    @Column(nullable = false)
    private int userId; // 작성자 아이디

    @NonNull
    @Column(nullable = false)

    @ManyToMany(mappedBy = "likedComments")
    private List<User> likedByUsers; // 좋아요를 누른 사용자들

}
