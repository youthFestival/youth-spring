package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자와 축제의 관계 테이블 (찜/좋아요 목록)
 */
@Entity
@Table(name = "UsersFestivals")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class UsersFestivals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(nullable = false)
    private int userId; // 사용자 외래 키

    @NonNull
    @Column(nullable = false)
    private int festivalId; // 축제 외래 키
}
