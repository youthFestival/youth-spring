package com.youth.server.controller;

import com.youth.server.domain.User;
import com.youth.server.dto.RestEntity;
import com.youth.server.exception.NotFoundException;
import com.youth.server.exception.PermissionDeniedException;
import com.youth.server.repository.UserRepository;
import com.youth.server.util.Const;
import com.youth.server.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    /**
     * 관리자 페이지 유저 전체 가져오기
     * Permission admin
     *
     * @return
     */

    @GetMapping
    public RestEntity getAllUsers(HttpServletRequest request, @RequestParam(name="page",required = false, defaultValue = "0") Integer page) {
//        User.Role role = jwtUtil.getRole(request).
//                orElseThrow(() -> new PermissionDeniedException("로그인 후 이용해주세요."));
//
//        if(role != User.Role.admin){
//            throw new PermissionDeniedException("관리자만 이용 가능합니다.");
//        }
        PageRequest pageRequest = PageRequest.of(page,Const.DEAFULT_LIMIT);

        return RestEntity.builder()
                .status(HttpStatus.CREATED)
                .message("조회되었습니다.")
                .put("users", userRepository.findAll(pageRequest))
                .build();

    }

    @GetMapping("{userId}")
    public RestEntity getUserDetail(@PathVariable Integer userId) {
        return RestEntity.builder()
                .put("user", userRepository.findById(userId))
                .status(HttpStatus.OK)
                .message("조회되었습니다.")
                .build();
    }

    @PutMapping("{uid}")
    public RestEntity updateUser(@RequestBody Map<String,String> payload,
                                 @PathVariable("uid") Integer uid) {

        User user = userRepository.findById(uid).orElseThrow(()-> new NotFoundException("존재하지 않는 유저입니다."));

        String userId, username, email, gender, address, tel, locality, password, isAllowEmail;

        userId = payload.get("userId");
        username = payload.get("username");
        email = payload.get("email");
        gender = payload.get("gender");
        address = payload.get("address");
        tel = payload.get("tel");
        locality = payload.get("locality");
        password = payload.get("password");
//        isAllowEmail = payload.get("isAllowEmail");

        if(userId != null) user.setUserId(userId);
        if(username != null) user.setUsername(username);
        if(email != null) user.setEmail(email);
        if(gender != null) user.setGender(gender.equals("남성") ? User.Gender.남성 : User.Gender.여성 );
        if(address != null) user.setAddress(address);
        if(tel != null) user.setTel(tel);
        if(locality != null) user.setLocality(locality);
        if(password != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
//        if(isAllowEmail != null) user.setAllowEmail(Boolean.parseBoolean(isAllowEmail));



        userRepository.save(user);

        return RestEntity.builder()
                .status(HttpStatus.OK)
                .message("수정되었습니다.")
                .build();
    }

    @PutMapping("/password/{email}")
    public RestEntity changePassword(@PathVariable("email") String email,
                                     @RequestBody Map<String,String> payload) {

        User user =  userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("해당 이메일에 존재하지 않는 유저입니다."));

        String newPassword = payload.get("password");

        if(newPassword == null) throw new IllegalArgumentException("비밀번호를 입력해주세요.");

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        return RestEntity.builder()
                .status(HttpStatus.OK)
                .message("비밀번호가 변경되었습니다.")
                .build();
    }
}