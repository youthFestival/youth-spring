package com.youth.server.util;

import com.youth.server.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class JwtUtilTest {

    @Autowired JwtUtil jwtUtil;
    @Autowired
    AuthService authService;

    @Test
    public void 토큰_만들기_테스트(){
        String token = jwtUtil.createAccessToken(Map.of("userId","youth","password","1234"));


        Assertions.assertEquals("youth",jwtUtil.getValueOf(token,"userId"));
        Assertions.assertFalse(jwtUtil.verifyToken(token, "userId", "비가오는거리에~~~"));

    }

    @Test
    public void 토큰에서_유저권한_가져오기_테스트(){
        System.out.println("1 :"+ jwtUtil.getRole(null).get());
    }

}