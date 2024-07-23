package com.youth.server.service;

import com.youth.server.domain.User;
import com.youth.server.exception.WrongInputException;
import com.youth.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public User login(String userId, String password) {
        Optional<User> user = userRepository.findByUserId(userId);

//        @TODO NOT FOUND EXCEIPTIon 파일이 없어서 임시로 대체
        if (!user.isPresent()) {
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
}
