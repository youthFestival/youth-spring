package com.youth.server.controller;

import com.youth.server.domain.User;
import com.youth.server.dto.RestEntity;
import com.youth.server.dto.UserDTO;
import com.youth.server.exception.NotFoundException;
import com.youth.server.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final String AUTH_TOKEN_NAME = "token";

    @Autowired private AuthService authService;

    @PostMapping("/login")
    public RestEntity login(HttpServletResponse response, @RequestBody Map<String, String> credentials) {
        String userId = credentials.get("userId");
        String password = credentials.get("password");
        User loggedInUser = authService.login(userId, password);

        // @TODO
        Cookie jwtCookie = new Cookie(AUTH_TOKEN_NAME," NANSUIIIIIIII");
        jwtCookie.setMaxAge(60*30);//30분
        jwtCookie.setSecure(false);//https 를 통해서만 쿠키를 주고받을 수 있도록 설정
        response.addCookie(jwtCookie);

        return RestEntity.builder().
                status(HttpStatus.OK).
                message("로그인되었습니다.").
                put("user", loggedInUser)
                .build();
    }

    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public RestEntity logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie(AUTH_TOKEN_NAME, null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setSecure(false);//https 를 통해서만 쿠키를 주고받을 수 있도록 설정

        response.addCookie(jwtCookie);

        return RestEntity.builder()
                .put("status", "success")
                .put("message", "로그아웃되었습니다.")
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/register")
    public RestEntity register(@RequestBody UserDTO userDTO) {
            User newUser = User.builder()
                    .userId(userDTO.getUserId())
                    .password(userDTO.getPassword())
                    .email(userDTO.getEmail())
                    .gender("남성".equals(userDTO.getGender()) ? User.Gender.남성 : User.Gender.여성)
                    .username(userDTO.getUsername())
                    .address(userDTO.getAddress())
                    .tel(userDTO.getTel())
                    .isAllowEmail(userDTO.getIsAllowEmail())
                    .isAdmin(User.Role.user)
                    .build();

            User user = authService.join(newUser);

            return RestEntity.builder()
                    .status(HttpStatus.CREATED)
                    .put("message", "회원가입이 완료되었습니다.")
                    .put("user", user)
                    .build();
    }

    @PostMapping("/duplication-username")
    public RestEntity checkUsernameDuplication(@RequestBody Map<String, String> payload) {
        String userId = payload.getOrDefault("userId", "");
        boolean isDup = authService.checkUserIdDuplication(userId);

        return RestEntity.builder()
                .status(isDup ? HttpStatus.OK : HttpStatus.CONFLICT)
                .message(isDup ? "이미 사용중인 아이디입니다." : "사용 가능한 아이디입니다.")
                .put("duplicate", isDup)
                .build();
    }


    /**
     * 연동 회원가입
     */
//    @PostMapping("/sns-login") // @TODO : 연동 로그인
//    public void snsLogin(@RequestBody Map<String, String> snsInfo) {
//        String provider = snsInfo.getOrDefault("provider", "");
//        String token = snsInfo.getOrDefault("token", "");
//
//    }

    /**
     *  계정 찾기
     */
    @PostMapping("/find-username-email")
    public RestEntity findUsernameEmail(@RequestBody Map<String, String> payload) {
        String username = payload.getOrDefault("username", "");
        String email = payload.getOrDefault("email", "");

        if(username.isEmpty() || email.isEmpty()){
            throw new IllegalArgumentException("성함 또는 이메일을 확인해주세요.");
        }

        Optional<User> user = authService.findUsernameEmail(username, email);

        if(user.isEmpty()){
            throw new NotFoundException("일치하는 계정이 없습니다.");
        }

        return RestEntity.builder()
                .status(HttpStatus.OK)
                .message("계정을 찾았습니다.")
                .put("userId", user.get().getUserId())
                .build();
    }

    /**
     * TODO
     * 인증번호 서버로 전송
     * 연동회원 가입
     *  인증 후, 비밀번호 변경
     */
}
