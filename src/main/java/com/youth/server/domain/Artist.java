package com.youth.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * 아티스트 정보
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"subscribedUsers", "participatingFestivals"})
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(length = 255, nullable = false)
    private String name; // 가수 이름

    @JoinColumn(name = "imageId", nullable = true)
    @OneToOne(fetch = FetchType.EAGER)
    private Image image;

    @ManyToMany(mappedBy = "favoriteArtists", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> subscribedUsers; // 구독한 유저 목록

    @ManyToMany(mappedBy = "participatingArtists", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Festival> participatingFestivals; // 참가하는 축제 목록

//    @Transient
//    private String imgUrl;
//
//    @PostLoad
//    public void setImgUrl(){
//        this.imgUrl = artistProfileImage.getImgUrl();
//    }
}
