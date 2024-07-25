package com.youth.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 행사 정보
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"participatingArtists", "participatingBooths", "favoriteUsers", "university", "comments", "images"})
public class Festival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(length = 255, nullable = false)
    private String name; // 행사 이름

    @Column(nullable = true)
    private LocalDateTime startDate; // 축제 시작 날짜

    @Column(nullable = true)
    private LocalDateTime endDate; // 축제 종료 날짜

    @JoinColumn(name = "geoLocationId")
    @OneToOne(fetch = FetchType.EAGER)
    private Geolocation geoLocationId; // 위치 (외래 키)

    @Column(nullable = true)
    private String description; // 설명

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name= "category",nullable = false, columnDefinition = "enum('대학축제', '페스티벌')")
    private Category category; // 행사 유형

    @Column(length = 255, nullable = true)
    private String organizer; // 행사 주관하는 곳

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int viewCount; // 방문자 수

    @Column(length = 255, nullable = true)
    private String locality; // 행사 지역 (전라도, 경기도 등)

    @Column(length = 50, nullable = true)
    private String tel; // 행사 주관 연락처

    @Column(nullable = true)
    private Integer minAge; // 관람 연령 제한

    @Column(nullable = true)
    private Integer ticketPrice; // 티켓 가격

    @Column(length = 255, nullable = true)
    private String ticketUrl; // 티켓 예매 링크

    @Column(nullable = true)
    private LocalDateTime ticketOpen; // 티켓 판매 시작 시간

    @Column(nullable = true)
    private LocalDateTime ticketClose; // 티켓 판매 종료 시간

    @Column(nullable = true)
    private java.time.LocalTime stageOpen; // 공연 시작 시간

    @Column(nullable = true)
    private java.time.LocalTime stageClose; // 공연 마무리 시간

    @Column(length = 255, nullable = true)
    private String organizerUrl; // 행사 홈페이지

    public enum Category {
        대학축제, 페스티벌
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "FestivalArtist",
        joinColumns = @JoinColumn(name = "festivalId"),
        inverseJoinColumns = @JoinColumn(name = "artistId")
    )
    @JsonIgnore
    private Set<Artist> participatingArtists; // 축제에 참여하는 아티스트들


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "FestivalBooth",
            joinColumns = @JoinColumn(name = "festivalId"),
            inverseJoinColumns = @JoinColumn(name = "boothId")
    )
    @JsonIgnore
    private Set<Booth> participatingBooths; // 축제에 참여하는 부스들

    @ManyToMany(fetch = FetchType.LAZY , mappedBy = "favoriteFestivals")
    @JsonIgnore
    private Set<User> favoriteUsers; // 좋아요 누른 사용자들


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "univercityId")
    @JsonIgnore
    private University university;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "festival")
    @JsonIgnore
    private Set<Comment> comments; // 댓글 목록

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "FestivalImage",
            joinColumns = @JoinColumn(name = "festivalId"),
            inverseJoinColumns = @JoinColumn(name = "imageId")
    )
    @JsonIgnore
    private Set<Image> images; // 페스티벌 이미지들



}
