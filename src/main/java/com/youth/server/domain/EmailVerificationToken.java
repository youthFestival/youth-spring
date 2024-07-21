package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * 이메일 인증 토큰
 */
@Entity
@Table(name = "EmailVerificationTokens")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Column(nullable = false)
    private int userId; // 유저 아이디

    @NonNull
    @Column(length = 255, nullable = false, unique = true)
    private String token; // 이메일 토큰

    @NonNull
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt; // 생성 시간

    @NonNull
    @Column(nullable = false)
    private LocalDateTime expiresAt; // 유효 시간
}
