package com.youth.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

/**
 * 고객 문의
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 기본 키, 자동 증가

    @Enumerated(EnumType.STRING)
    @NonNull
    @Column(nullable = false, columnDefinition = "enum('질문','답변')")
    private Category category; // 문의 카테고리

    @NonNull
    @Column(length = 255, nullable = false)
    private String title; // 제목

    @NonNull
    @Column(nullable = false)
    private String content; // 내용

    @JoinColumn(name="authorId")
    @ManyToOne(fetch = FetchType.EAGER)
    private User author; // 작성자 ID

    @Transient
    private String username;

    @Column
    @CurrentTimestamp
    private LocalDateTime updatedAt; // 작성 일시(답변 일시)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('접수중','접수완료','답변완료') default '접수중'")
    private Status status; // 접수 상태

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isSecret; // 비밀글 여부

    public enum Category {
        질문, 기타, 답변
    }

    public enum Status {
        접수중, 접수완료, 답변완료
    }


    // Assuming a direct reference to QnA for either question or answer
    @JoinColumn(name="replyId")
    @OneToOne(fetch = FetchType.EAGER)
    private Inquiry reply;

    @JoinColumn(name="festivalId", nullable = true)
    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Festival festival;

    @Transient
    private Integer festivalId;

    @Transient
    private String festivalName;

    @PostLoad
    private void setFestivalDetails() {
        if (festival != null) {
            this.festivalId = festival.getId();
            this.festivalName = festival.getName();
        }
        this.username = author.getUserId();
    }

}
