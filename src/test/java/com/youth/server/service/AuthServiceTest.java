package com.youth.server.service;

import com.youth.server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired AuthService authService;

    @Test
    void login() {
        System.out.println(authService.login("testUser", "password123"));
    }
}