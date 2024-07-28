package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 이메일 인증 토큰
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user; // 유저 아이디

    @NonNull
    @Column(length = 255, nullable = false, unique = true)
    private String token; // 이메일 토큰

    @NonNull
    @CreationTimestamp
    private LocalDateTime createdAt; // 생성 시간

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        // 토큰 만료 시간을 현재 시간으로부터 1시간 뒤로 설정
        this.expiresAt = LocalDateTime.now().plusHours(1);
    }
}
