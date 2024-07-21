package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 대학과 축제의 관계 테이블
 */
@Entity
@Table(name = "UniversitiesFestival")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class UniversitiesFestival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(nullable = false)
    private int univId; // 대학 외래 키

    @NonNull
    @Column(nullable = false)
    private int festivalId; // 축제 외래 키
}
