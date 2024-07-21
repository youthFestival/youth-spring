package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 댓글 좋아요 관계 테이블
 */
@Entity
@Table(name = "CommentsLikes")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class CommentsLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(nullable = false)
    private int userId; // 사용자 외래 키

    @NonNull
    @Column(nullable = false)
    private int commentId; // 댓글 외래 키
}
