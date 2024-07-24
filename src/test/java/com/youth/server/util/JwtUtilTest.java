package com.youth.server.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class JwtUtilTest {

    @Autowired JwtUtil jwtUtil;

    @Test
    public void 토큰_만들기_테스트(){
        String token = jwtUtil.createAccessToken(Map.of("userId","youth","password","1234"));


        Assertions.assertEquals("youth",jwtUtil.getValueOf(token,"userId"));
        Assertions.assertFalse(jwtUtil.verifyToken(token, "userId", "비가오는거리에~~~"));

    }

}