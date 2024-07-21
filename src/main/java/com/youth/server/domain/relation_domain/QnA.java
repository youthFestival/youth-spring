package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 고객 문의와 답변 관계 테이블
 */
@Entity
@Table(name = "QnA")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class QnA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(nullable = false)
    private int questionId; // 질문 외래 키

    @NonNull
    @Column(nullable = false)
    private int answerId; // 답변 외래 키
}
