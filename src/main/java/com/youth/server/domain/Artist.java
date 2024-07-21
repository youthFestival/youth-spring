package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import java.util.Set;

/**
 * 아티스트 정보
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(length = 255, nullable = false)
    private String name; // 가수 이름

    @Column(nullable = false)
    private int imageId; // 프로필 이미지 ID (외래 키)

    @ManyToMany(mappedBy = "favoriteArtists", fetch = FetchType.LAZY)
    private Set<User> subscribedUsers; // 구독한 유저 목록

    @ManyToMany(mappedBy = "participatingArtists", fetch = FetchType.LAZY)
    private Set<Festival> participatingFestivals; // 참가하는 축제 목록
}
