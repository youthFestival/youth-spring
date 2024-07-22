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
@AllArgsConstructor
public class QnA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @OneToOne
    @JoinColumn(name = "questionId", referencedColumnName = "id")
    private Inquiry question; // 질문

    @OneToOne
    @JoinColumn(name = "answerId", referencedColumnName = "id")
    private Inquiry answer; // 답변
}
