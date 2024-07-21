package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 사진 목록
 */
@Entity
@Table(name = "Images")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(length = 255, nullable = false)
    private String detail; // 설명

    @NonNull
    @Column(length = 255, nullable = false)
    private String imgUrl; // 이미지 주소

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('행사_사진','구조도','포스터','행사_정보','부스','기타','대학로고','아티스트') default '기타'")
    private Category category; // 이미지 타입

    public enum Category {
        행사_사진, 구조도, 포스터, 행사_정보, 부스, 기타, 대학로고, 아티스트
    }
}
