package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 부스와 아이템의 관계 테이블
 */
@Entity
@Table(name = "BoothsItems")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class BoothsItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(nullable = false)
    private int boothId; // 부스 외래 키

    @NonNull
    @Column(nullable = false)
    private int itemId; // 아이템 외래 키
}
