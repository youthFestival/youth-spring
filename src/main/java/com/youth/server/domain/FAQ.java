package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * 자주 묻는 질문
 */
@Entity
@Table(name = "FAQ")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class FAQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(length = 255, nullable = false)
    private String title; // 제목

    @NonNull
    @Column(nullable = false)
    private String content; // 내용

    @NonNull
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt; // 작성 일시(답변 일시)
}
