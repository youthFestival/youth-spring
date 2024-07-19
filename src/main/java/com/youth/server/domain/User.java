package com.youth.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "Users")
@Getter @Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "userId", nullable = false, length = 50)
    private String userId;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "createdAt", nullable = false)
    private Timestamp createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "tel", length = 50)
    private String tel;

    @Column(name = "address", length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "isAdmin", nullable = false)
    private Role isAdmin;

    @Column(name = "isAllowEmail", nullable = false)
    private boolean isAllowEmail;

    public User() {
    }

    public enum Gender {
        남성, 여성
    }

    public enum Role {
        admin, user
    }
}
