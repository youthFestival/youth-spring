package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 아이템 정보
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(length = 255, nullable = false)
    private String name; // 제목

    @Column(nullable = true)
    private Integer price; // 물품 가격

    @Column(length = 255, nullable = true)
    private String keyword; // 키워드(음식, 주류 등)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boothId", nullable = false)
    private Booth booth;
}
