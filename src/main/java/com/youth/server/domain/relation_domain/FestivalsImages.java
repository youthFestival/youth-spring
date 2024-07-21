package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 축제와 이미지의 관계 테이블
 */
@Entity
@Table(name = "FestivalsImages")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class FestivalsImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(nullable = false)
    private int imageId; // 이미지 외래 키

    @NonNull
    @Column(nullable = false)
    private int festivalId; // 축제 외래 키
}
