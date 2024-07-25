package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 사용자 알람용 이벤트
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('디데이','문의답변','구독한 행사 이벤트','좋아하는 아티스트 행사 알림','대댓글')")
    private Category category; // 이벤트 카테고리

    @NonNull
    @Column(length = 255, nullable = false)
    private String content; // 내용

    @NonNull
    @Column(length = 255, nullable = false)
    private String redirectUrl; // 알람 이벤트 처리 링크

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isChecked; // 알람 확인 여부

    @NonNull
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt; // 이벤트 생성 시간

    public enum Category {
        디데이, 문의답변, 구독한_행사_이벤트, 좋아하는_아티스트_행사_알림, 대댓글}
}
