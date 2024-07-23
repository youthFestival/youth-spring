package com.youth.server.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;


@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil() {
        this.secretKey = Jwts.SIG.HS256.key().build();
    }

    public String createAccessToken(String value) {

        return Jwts.builder()
                .claim("email", String.class)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public boolean verifyToken(String jwtToken, String key, String value) {
        return Jwts.parser().
                verifyWith(secretKey).
                build().
                parseSignedClaims(jwtToken).
                getPayload().
                get(key).
                equals(value);
    }
}
