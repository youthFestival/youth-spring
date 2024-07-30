package com.youth.server.controller;

import com.youth.server.domain.User;
import com.youth.server.dto.RestEntity;
import com.youth.server.dto.UserDTO;
import com.youth.server.exception.NotFoundException;
import com.youth.server.repository.EmailVerificationTokenRepository;
import com.youth.server.repository.UserRepository;
import com.youth.server.service.AuthService;
import com.youth.server.service.UserService;
import com.youth.server.util.Const;
import com.youth.server.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @PostMapping("/login")
    public RestEntity login(HttpServletResponse response, @RequestBody Map<String, String> credentials) {
        String userId = credentials.get("userId");
        String password = credentials.get("password");
        User loggedInUser = authService.login(userId, password);

        // 토큰 만들건데, userId랑 role을 넣어서 만들어야함
        Cookie jwtCookie = new Cookie(Const.AUTH_TOKEN_NAME, jwtUtil.createAccessToken(Map.of(
                "id",String.valueOf(loggedInUser.getId()),
                "userId", loggedInUser.getUserId(),
                "role", loggedInUser.getIsAdmin().toString()
        )) );

        jwtCookie.setMaxAge(30 * 60); // 30분
        jwtCookie.setPath("/");
        jwtCookie.setAttribute("sameSite", "Lax");
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
    @GetMapping ("/logout")
    public RestEntity logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Const.AUTH_TOKEN_NAME)) {
                    cookie.setValue("");
                    cookie.setPath("/"); // 쿠키 생성 시 설정한 path와 동일하게 설정
                    cookie.setMaxAge(0);
                    cookie.setAttribute("sameSite", "Lax"); // 쿠키 생성 시 설정한 속성과 동일하게 설정
                    response.addCookie(cookie);
                }
            }
        }
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
                .status(isDup ? HttpStatus.CONFLICT : HttpStatus.OK)
                .message(isDup ? "이미 사용중인 아이디입니다." : "사용 가능한 아이디입니다.")
                .put("duplicate", isDup)
                .build();
    }


    /**
     * 연동 회원가입
     */
    @PostMapping("/kakao-login") //
    public RestEntity snsLogin(HttpServletResponse response,@RequestBody Map<String, String> payload) {
        String userId = payload.get("id");
        String profileName = payload.get("profileName");

        Optional<User> user = userRepository.findByUserId(userId);

        // 유저가 없을경우 회원가입
        if(user.isEmpty()){
            user = Optional.of(authService.join(User.builder()
                    .userId(userId)
                    .username(profileName)
                    .password("새 비밀번호를 입력해주세요.")
                    .email("새 이메일 정보를 등록해주세요.")
                    .username(profileName)
                    .address("새 주소를 입력해주세요.")
                    .locality("서울")
                    .gender(User.Gender.남성)
                    .isAdmin(User.Role.user)
                    .build()));
        }

        // 유저가 있을경우
        user.ifPresent((loggedInUser)->{
            Cookie jwtCookie = new Cookie(Const.AUTH_TOKEN_NAME, jwtUtil.createAccessToken(Map.of(
                    "id",String.valueOf(loggedInUser.getId()),
                    "userId", loggedInUser.getUserId(),
                    "role", loggedInUser.getIsAdmin().toString())));

            jwtCookie.setMaxAge(30 * 60); // 30분
            jwtCookie.setPath("/");
            jwtCookie.setAttribute("sameSite", "Lax");

            response.addCookie(jwtCookie);
        });

            return RestEntity.builder()
                    .message("로그인되었습니다.")
                    .put("user", user.get())
                    .status(HttpStatus.OK)
                    .build();
        }

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

    /*
    * 이메일 인증
    * */
    @PostMapping("/reset-password-email-send")
    public RestEntity sendResetPasswordEmail(@RequestBody Map<String, String> payload) {
        emailVerificationTokenRepository.deleteAllByExpiresAtBefore(LocalDateTime.now()); // 만료된 토큰 삭제
        // 유저 아이디에 해당하는 이미 생성된 토큰 삭제


        String userId = payload.getOrDefault("userId", "");
        String email = payload.getOrDefault("email", "");

        if(userId.isEmpty() || email.isEmpty()){
            throw new IllegalArgumentException("성함 또는 이메일을 확인해주세요.");
        }

        Optional<User> user = authService.findByEmailAndUserId(email, userId);

        if(user.isEmpty()){
            throw new NotFoundException("일치하는 계정이 없습니다.");
        }

        emailVerificationTokenRepository.deleteAllByUserId(user.get().getId()); // 이미 생성된 토큰 삭제


        userService.sendCodeToEmail(email);

        return RestEntity.builder()
                .status(HttpStatus.OK)
                .message("인증번호를 발송했습니다")
                .build();
    }

    @GetMapping("/email-verification-request")
    public RestEntity sendEmailVerificationRequest(@RequestParam(name = "email") String email, @RequestParam(name = "code") String authCode) {
        boolean isVerified = userService.verifiedCode(email, authCode); // 인증번호 확인

        if (isVerified) {
            return RestEntity.builder()
                    .status(HttpStatus.OK)
                    .message("인증이 완료되었습니다")
                    .put("redirectUrl", "/change-password?email=" + email)
                    .build();
        } else {
            return RestEntity.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("인증에 실패하였습니다")
                    .build();
        }
    }

    /**
     * TODO
     * 인증번호 서버로 전송
     * 연동회원 가입
     *  인증 후, 비밀번호 변경
     */
    @GetMapping("/kakao")
    public RestEntity kakaoCallback(@RequestParam(name = "code") String code) {
        // TODO : Kakao API로 code 받아서 access token 받아서 user_id, email, nickname, profile_image_url 받아서 DB에 insert
        // user_id, email, nickname, profile_image_url, access_token, refresh_token, is_kakao, is_admin, is_allow_email, created_at, updated_at
        String access_token;
        try {
            access_token = authService.getKakaoAccessToken(code);
        } catch (Exception e) {
            return RestEntity.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Failed to retrieve access token from Kakao")
                    .put("error", e.getMessage())
                    .build();
        }
        UserDTO userDTO;
        try {
            userDTO = authService.kakaoLogin(access_token);
        } catch (Exception e) {
            return RestEntity.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Failed to log in with Kakao")
                    .put("error", e.getMessage())
                    .build();
        }
        return RestEntity.builder()
                .status(HttpStatus.OK)
                .message("Kakao login successful")
                .put("user", userDTO)
                .build();
    }


    @Transactional
    /**
     * 만료된 토큰 삭제
     */
    public void removeExpiredToken() {
        emailVerificationTokenRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
    }
}
