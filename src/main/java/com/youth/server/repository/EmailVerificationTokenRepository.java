package com.youth.server.repository;

import com.youth.server.domain.EmailVerificationToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Integer> {
    Optional<EmailVerificationToken> findByToken(String token);
    @Transactional
    void deleteAllByExpiresAtBefore(LocalDateTime currentTime);

    @Transactional
    void deleteAllByUserId(Integer userId);
}
