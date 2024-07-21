package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * 고객 문의
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('페스티벌','계정')")
    private Category category; // 문의 카테고리

    @NonNull
    @Column(length = 255, nullable = false)
    private String title; // 제목

    @NonNull
    @Column(nullable = false)
    private String content; // 내용

    @NonNull
    @Column(nullable = false)
    private int authorId; // 작성자 ID

    @NonNull
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt; // 작성 일시(답변 일시)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('접수중','접수완료','답변완료') default '접수중'")
    private Status status; // 접수 상태

    @NonNull
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isSecret; // 비밀글 여부

    public enum Category {
        페스티벌, 계정
    }

    public enum Status {
        접수중, 접수완료, 답변완료
    }
}
