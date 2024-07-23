package com.youth.server.controller;

import com.youth.server.domain.User;
import com.youth.server.dto.UserDTO;
import com.youth.server.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<?,?>> login(@RequestBody Map<String, String> credentials) {
        String userId = credentials.get("userId");
        String password = credentials.get("password");
        return authService.login(userId, password);
    }

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
}
