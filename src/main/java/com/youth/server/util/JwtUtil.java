package com.youth.server.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Map;
import java.util.Optional;


@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil() {
        this.secretKey = Jwts.SIG.HS512.key().build();
    }

    /**
     * 주어진 맵을 이용하여 토큰을 생성합니다.
     * @param claims 토큰에 담을 정보
     * @return 생성된 토큰
     */


    public String createAccessToken(Map<String,String> claims) {;

        JwtBuilder.BuilderClaims builderClaims = Jwts.builder()
                .claims();

        for ( String key : claims.keySet()) {
            builderClaims.add(key,claims.get(key));
        }

        return builderClaims.and()
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public boolean verifyToken(String jwtToken, String key, String value) {
        return getValueOf(jwtToken,key).
                equals(value);
    }


    public String getValueOf(String jwtToken, String key){
        return Jwts.parser().
                verifyWith(secretKey).
                build().
                parseSignedClaims(jwtToken).
                getPayload().
                get(key).toString();
    }

    /**
     *JWT 쿠키(token) 로부터 유저 아이디를 가져옵니다.
     * @param request {HttpServletRequest} 요청 객체
        * @return {Optional<String>} 유저 아이디
     */
    public Optional<String> getUserId(HttpServletRequest request) {
        return CookieParserUtil.getFromServletRequest(Const.AUTH_TOKEN_NAME, request)
                .map(authCookie -> getValueOf(authCookie, "userId"));
    }

}

