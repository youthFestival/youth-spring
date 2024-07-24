package com.youth.server.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;


public class CookieParserUtil {

    /**
     * HttpServletRequest로부터 쿠키를 가져옵니다.
     * @param cookieName {String} 가져올 쿠키의 이름
     * @param request {HttpServletRequest} 요청 객체
     * @return {Optional<String>>} 쿠키의 값
     */
    public static Optional<String> getFromServletRequest(String cookieName, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return Optional.of(cookie.getValue());
            }
        }

        return Optional.empty();
    }




}

