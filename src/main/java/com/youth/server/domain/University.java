package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

/**
 * 대학 정보
 */
@Entity
@Table(name = "Universities")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(length = 255, nullable = false)
    private String name; // 대학 이름

    @Column(length = 50, nullable = true)
    private String campus; // 대학 캠퍼스

    @NonNull
    @Column(nullable = false)
    private int geoLocationId; // 위치 (외래 키)

    @NonNull
    @Column(nullable = false)
    private int logoId; // 대학교 로고 (외래 키)
}
