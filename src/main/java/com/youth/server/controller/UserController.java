package com.youth.server.controller;

import com.youth.server.domain.User;
import com.youth.server.dto.RestEntity;
import com.youth.server.exception.PermissionDeniedException;
import com.youth.server.repository.UserRepository;
import com.youth.server.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    /**
     * 관리자 페이지 유저 전체 가져오기
     * Permission admin
     * @return
     */

    @GetMapping
    public List<User> getAllUsers(HttpServletRequest request){
        User.Role role = jwtUtil.getRole(request).
                orElseThrow(() -> new PermissionDeniedException("로그인 후 이용해주세요."));

        if(role != User.Role.admin){
            throw new PermissionDeniedException("관리자만 이용 가능합니다.");
        }

        return userRepository.findAll();
    }

    @GetMapping("{userId}")
    public RestEntity getUserDetail(@PathVariable Integer userId){
        return RestEntity.builder()
                .put("user", userRepository.findById(userId))
                .status(HttpStatus.OK)
                .message("조회되었습니다.")
                .build();
    }





}