package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 위치 정보
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Geolocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키

    @Column(nullable = true)
    private Float latitude; // 위도

    @Column(nullable = true)
    private Float longitude; // 경도

    @Column(length = 255, nullable = true)
    private String detail; // 주소 디테일
}
