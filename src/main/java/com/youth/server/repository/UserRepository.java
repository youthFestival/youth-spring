package com.youth.server.repository;

import com.youth.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserIdAndPassword(String userId, String password);
    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    Optional<User> findByUserId(String userId);

    Optional<User> findByUsernameAndEmail(String username, String email);
}
