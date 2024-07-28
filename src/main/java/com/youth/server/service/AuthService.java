package com.youth.server.service;

import com.youth.server.domain.User;
import com.youth.server.dto.UserDTO;
import com.youth.server.exception.WrongInputException;
import com.youth.server.repository.UserRepository;
import com.youth.server.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    /**
     * 로그인
     * @param userId 아이디
     * @param password 비밀번호
     * @return 로그인 결과
     */
    public User login(String userId, String password) {
        Optional<User> user = userRepository.findByUserId(userId);

        if (user.isEmpty()) {
            throw new WrongInputException("존재하지 않는 사용자입니다.");
        }
        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new WrongInputException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 로그인 성공
        return user.get();
    }

    /**
     * 회원가입
     * @param user 회원 정보
     *
     */
    public User join(User user) {
        // 아이디 중복 확인
        Optional<User> existingUser = userRepository.findByUserId(user.getUserId());
        if (existingUser.isPresent()) {
            throw new WrongInputException("이미 사용중인 아이디입니다.");
        }
        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 회원가입 (실패시, jpa에서 에러를 던지고, 글로벌 핸들러로 감)
        userRepository.save(user);

        return user;
    }

    public boolean checkUserIdDuplication(String userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

    public Optional<User> findUsernameEmail(String username, String email) {
        return userRepository.findByUsernameAndEmail(username, email);
    }

    public Optional<User> getUserFromToken(String jwt) {
        String userId = jwtUtil.getValueOf(jwt, "userId").toString();
        // 토큰 검증
        return userRepository.findByUserId(userId);
    }

    public String getKakaoAccessToken(String code) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .build();
        Map<String, String> response = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", "28cf3160c48b1795c740c62a4df6831b")
                        .queryParam("redirect_uri", "http://localhost:3000/oauth/kakao")
                        .queryParam("code", code)
                        .build())
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();
        if (response == null || response.get("access_token") == null) {
            throw new RuntimeException("Failed to retrieve access token from Kakao");
        }
        return response.get("access_token");
    }

    public UserDTO kakaoLogin(String accessToken) {
        Map<String, Object> userInfo = getUserKakaoInfo(accessToken);

        String email = (String) ((Map<String, Object>) userInfo.get("kakao_account")).get("email");
        String nickname = (String) ((Map<String, Object>) userInfo.get("kakao_account")).get("nickname");
        String gender = (String) ((Map<String, Object>) userInfo.get("kakao_account")).get("gender");

        Optional<User> existingUser = userRepository.findByUserId(email);

        User user;
        if (existingUser.isEmpty()) {
            user = User.builder()
                    .userId(email)
                    .email(email)
                    .username(nickname)
                    .isAllowEmail(true)
                    .isAdmin(User.Role.user)
                    .password(passwordEncoder.encode("default_password"))
                    .createdAt(LocalDateTime.now())
                    .gender(User.Gender.valueOf(gender))
                    .build();
            userRepository.save(user);
        } else {
            user = existingUser.get();
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());

        return userDTO;
    }


    private Map<String, Object> getUserKakaoInfo(String accessToken) {
        WebClient webClient = WebClient.builder()
               .baseUrl("https://kapi.kakao.com")
               .build();
        return webClient.get()
               .uri(uriBuilder -> uriBuilder.path("/v2/user/me")
                       .queryParam("access_token", accessToken)
                       .build())
               .header("Authorization", "Bearer " + accessToken)
               .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
               .retrieve()
               .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
               .block();
    }

    public Optional<User> findByEmailAndUserId(String email, String userId) {
        return userRepository.findByEmailAndUserId(email, userId);
    }
}
