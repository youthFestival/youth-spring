package com.youth.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "userId"),
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@ToString(exclude = {"favoriteArtists", "likedComments", "favoriteFestivals"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(nullable = false, length = 50)
    private String userId;

    @NonNull
    @Column(nullable = false, length = 255)
    @JsonIgnore // 비밀번호 노출 방지
    private String password;

    @NonNull
    @Column(nullable = false, length = 50)
    private String email;

    @NonNull
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @NonNull
    @Column(nullable = false, length = 50)
    private String username;

    @Column(length = 50)
    private String tel;

    @Column(length = 255)
    private String address;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role isAdmin;

    @Column(nullable = false)
    private boolean isAllowEmail;


    @Column(nullable = true)
    private String locality;

    public enum Gender {
        남성, 여성
    }

    public enum Role {
        admin, user
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ArtistUser",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "artistId")
    )
    @JsonIgnore
    private List<Artist> favoriteArtists; // 좋아요 누른 아티스트


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "CommentLike",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "commentId")
    )
    @JsonIgnore
    private Set<Comment> likedComments; // 좋아요 누른 댓글


    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "favoriteUsers")
    @JsonIgnore
    private Set<Festival> favoriteFestivals; // 좋아요 누른 축제

    // 해당 유저가 달은 댓글 목록
    @OneToMany(mappedBy ="author", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Comment> comments;

    // 유저 문의 글 정보
    @OneToMany(mappedBy ="author")
    @JsonIgnore
    private Set<Inquiry> inquiries;
}
