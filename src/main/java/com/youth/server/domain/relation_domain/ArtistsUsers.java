package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 아티스트와 사용자 관계 테이블
 */
@Entity
@Table(name = "ArtistsUsers")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class ArtistsUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(nullable = false)
    private int artistId; // 아티스트 외래 키

    @NonNull
    @Column(nullable = false)
    private int userId; // 사용자 외래 키
}
