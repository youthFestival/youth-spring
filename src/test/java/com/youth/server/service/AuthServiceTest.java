package com.youth.server.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthServiceTest {

    @Autowired AuthService authService;

    @Test
    void login() {
        System.out.println(authService.login("testUser", "password123"));
    }
}