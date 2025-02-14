package com.youth.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 댓글 정보
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가


    @Column
    @CurrentTimestamp
    private LocalDateTime updatedAt; // 댓글 작성 날짜

    @NonNull
    @Column(nullable = false)
    private String content; // 댓글 내용


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnore
    private User author; // 작성자 아이디

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt; // 댓글 작성 날짜

    @ManyToMany(mappedBy = "likedComments" ,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> likedByUsers; // 좋아요를 누른 사용자들

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festivalId", nullable = false)
    @JsonIgnore
    private Festival festival; // 댓글이 달린 게시글

}
