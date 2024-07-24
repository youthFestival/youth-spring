package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * 부스 정보
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Booth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('먹거리','체험','플리마켓','티켓','굿즈','기타')")
    private Category category; // 부스 종류

    @NonNull
    @Column(length = 255, nullable = false)
    private String name; // 제목

    public enum Category {
        먹거리, 체험, 플리마켓, 티켓, 굿즈, 기타
    }

    @OneToMany(mappedBy = "booth", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> items;

    @ManyToMany(mappedBy = "participatingBooths")
    private Set<Festival> festivals;
}
