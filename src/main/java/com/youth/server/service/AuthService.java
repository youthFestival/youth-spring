package com.youth.server.service;

import com.youth.server.domain.User;
//import com.youth.server.exception.NotFoundException;
import com.youth.server.exception.WrongInputException;
import com.youth.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 비밀번호 암호화
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // 비밀번호 검증
    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 로그인
     * @param userId 아이디
     * @param password 비밀번호
     * @return 로그인 결과
     */
    public ResponseEntity<Map<?,?>> login(String userId, String password) {
        Optional<User> user = userRepository.findByUserId(userId);
        System.out.println("yas");

//        @TODO NOT FOUND EXCEIPTIon 파일이 없어서 임시로 대체
        if (!user.isPresent()) {
            throw new WrongInputException("존재하지 않는 사용자입니다.");
        }
        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new WrongInputException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        // 로그인 성공
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Login successful.");
        response.put("user", user.get());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 회원가입
     * @param user 회원 정보
     *
     */
    public ResponseEntity<Map<?,?>> join(User user) {
        // 아이디 중복 확인
        Optional<User> existingUser = userRepository.findByUserId(user.getUserId());
        if (existingUser.isPresent()) {
            throw new WrongInputException("이미 사용중인 아이디입니다.");
        }

        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 회원가입
        userRepository.save(user);

        // 회원가입 성공
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "회원가입되었습니다.");
        response.put("user", user);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
