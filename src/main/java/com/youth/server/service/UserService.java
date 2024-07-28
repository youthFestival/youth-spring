package com.youth.server.service;

import com.youth.server.domain.EmailVerificationToken;
import com.youth.server.domain.User;
//import com.youth.server.dto.EmailVerificationResult;
import com.youth.server.exception.BusinessLogicException;
import com.youth.server.exception.NotFoundException;
import com.youth.server.repository.EmailVerificationTokenRepository;
import com.youth.server.repository.UserRepository;
//import com.youth.server.util.RedisUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
//    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final EmailService mailService;
//    private final RedisUtil redisService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
//    @Value("${spring.mail.auth-code-expiration-millis}")
//    private long authCodeExpirationMillis;
    private final UserRepository userRepository;

    public User findByUserId(String s) {
        Optional<User> user = userRepository.findByUserId(s);

        if(user.isEmpty()){
            throw new NotFoundException("해당 유저를 찾을 수 없습니다.");
        }

        return user.get();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User save(User user){
        return userRepository.save(user);
    }

    /**
     * 이메일 인증 코드 전송
     * 데이터베이스 조회 후 해당 이메일이 존재하지 않으면 예외 발생
     * @param toEmail
     */
    @Transactional
    public void sendCodeToEmail(String toEmail) {
//        this.checkDuplicatedEmail(toEmail);

        String title = "youth 이메일 인증 번호";
        String authCode = this.createCode();
        mailService.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
//        redisService.setData(AUTH_CODE_PREFIX + toEmail, authCode, this.authCodeExpirationMillis);

        // 토큰 생성후 저장
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();

        emailVerificationToken.setUser(userRepository
                .findByEmail(toEmail)
                .orElseThrow(()-> new NotFoundException("해당 이메일에 해당하는 유저가 존재하지 않습니다."))
        );

        emailVerificationToken.setToken(authCode);

        emailVerificationTokenRepository.save(emailVerificationToken);

    }

//    private void checkDuplicatedEmail(String email) {
//        Optional<User> user = userRepository.findByEmail(email);
//        if (!user.isPresent()) {
//            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", email);
////            throw new BusinessLogicException(BusinessLogicException.ExceptionCode.MEMBER_EXISTS);
//        }
//    }

    /**
     * 인증 코드 생성
     * @return 인증 코드
     */
    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new BusinessLogicException(BusinessLogicException.ExceptionCode.NO_SUCH_ALGORITHM);
        }
    }

    /**
     * 인증 코드 검증
     * @param email 이메일
     * @param authCode 인증 코드
     * @return 인증 성공 여부
     */
    @Transactional
    public boolean verifiedCode(String email, String authCode) {
//        this.checkDuplicatedEmail(email);
//        String redisAuthCode = redisService.getData(AUTH_CODE_PREFIX + email);
//        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);

        EmailVerificationToken token = emailVerificationTokenRepository.findByToken(authCode)
                .orElseThrow(()-> new NotFoundException("해당 인증 코드에 해당하는 토큰이 존재하지 않습니다."));




        // 현재 시간보다 토큰이 오래됐는지 판단
        if(token.getExpiresAt().isBefore(LocalDateTime.now())){
            emailVerificationTokenRepository.delete(token);
            throw new IllegalArgumentException("인증 코드가 만료되었습니다.");
        }
        // 인증 코드가 일치하는지 판단 + 만료되지 않았는지 판단
        else if(token.getToken().equals(authCode)){
            emailVerificationTokenRepository.delete(token);
            return true;
        }


        return false;
    }
}
