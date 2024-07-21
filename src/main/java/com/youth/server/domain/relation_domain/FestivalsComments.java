package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 축제와 댓글의 관계 테이블
 */
@Entity
@Table(name = "FestivalsComments")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class FestivalsComments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(nullable = false)
    private int festivalId; // 축제 외래 키

    @NonNull
    @Column(nullable = false)
    private int commentId; // 댓글 외래 키

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('QnA', '기대평')")
    private Category category; // 카테고리

    public enum Category {
        QnA, 기대평
    }
}
