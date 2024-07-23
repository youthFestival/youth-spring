package com.youth.server.controller;

import com.youth.server.domain.User;
import com.youth.server.dto.UserDTO;
import com.youth.server.exception.NotFoundException;
import com.youth.server.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<?,?>> login(HttpServletResponse response, @RequestBody Map<String, String> credentials) {
        String userId = credentials.get("userId");
        String password = credentials.get("password");
        Optional<User> user = authService.login(userId, password);

        Map<String, Object> data = new HashMap<>();
        data.put("status", "success");
        data.put("message", "로그인되었습니다.");
        data.put("user", user.get());

        // @TODO
        Cookie jwtCookie = new Cookie("token","NANSUIIIIIIII");
        jwtCookie.setMaxAge(60*60*3);//3시간
        jwtCookie.setSecure(false);//https를 통해서만 쿠키를 주고받을 수 있도록 설정
        response.addCookie(jwtCookie);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    // @TODO 로그아웃
    

    @PostMapping("/register")
    public ResponseEntity<Map<?,?>> register(@RequestBody UserDTO userDTO) {
        try {
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

            return authService.join(newUser);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/duplication-username")
    public ResponseEntity<Map<String,Object>> checkUsernameDuplication(@RequestBody Map<String, String> payload) {
        String userId = payload.getOrDefault("userId", "");
        boolean isDup = authService.checkUserIdDuplication(userId);
        String message = isDup ? "중복된 아이디입니다." : "사용 가능한 아이디입니다.";

        if(isDup){
            return new ResponseEntity<>(Map.of("message", message,
                    "duplication", Boolean.toString(isDup)) , HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(Map.of("message", message, "duplication" , Boolean.toString(isDup)), HttpStatus.OK);
    }


    /**
     * 연동 회원가입
     */
    @PostMapping("/sns-login") // @TODO : 연동 로그인
    public void snsLogin(@RequestBody Map<String, String> snsInfo) {
        String provider = snsInfo.getOrDefault("provider", "");
        String token = snsInfo.getOrDefault("token", "");

    }

    /**
     *  계정 찾기
     */
    @PostMapping("/find-username-email")
    public ResponseEntity<Map<?,?>> findUsernameEmail(@RequestBody Map<String, String> payload) {
        String username = payload.getOrDefault("username", "");
        String email = payload.getOrDefault("email", "");

        if(username.equals("") || email.equals("")){
            throw new IllegalArgumentException("성함 또는 이메일을 확인해주세요.");
        }

        Optional<User> user = authService.findUsernameEmail(username, email);

        if(!user.isPresent()){
            throw new NotFoundException("일치하는 계정이 없습니다.");
        }

        return new ResponseEntity<>(Map.of("message", "계정을 찾았습니다.", "userId", user.get().getUserId()), HttpStatus.OK);
    }

}
