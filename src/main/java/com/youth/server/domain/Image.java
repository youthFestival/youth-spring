package com.youth.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * 사진 목록
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"festivals", "artists"})
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
    @Column(name="categories", nullable = false, columnDefinition = "enum('행사_사진','구조도','포스터','행사_정보','부스','기타','대학로고','아티스트') default '기타'")
    private Category category; // 이미지 타입

    public enum Category {
        행사_사진, 구조도, 포스터, 행사_정보, 부스, 기타, 대학로고, 아티스트, 썸네일
    }

    @ManyToMany(mappedBy = "images", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Festival> festivals;

    @OneToOne(mappedBy = "artistProfileImage", fetch = FetchType.LAZY)
    @JsonIgnore
    private Artist artists;


}
