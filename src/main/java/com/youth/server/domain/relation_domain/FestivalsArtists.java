package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 축제와 아티스트의 관계 테이블
 */
@Entity
@Table(name = "FestivalsArtists")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class FestivalsArtists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(nullable = false)
    private int festivalId; // 축제 외래 키

    @NonNull
    @Column(nullable = false)
    private int artistId; // 아티스트 외래 키
}
