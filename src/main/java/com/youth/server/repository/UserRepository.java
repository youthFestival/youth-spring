package com.youth.server.repository;

import com.youth.server.domain.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserIdAndPassword(String userId, String password);

    @Query("SELECT u FROM User u")
    List<User> findAll(PageRequest pageRequest);

    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    Optional<User> findByUserId(@Param("userId") String userId);

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndUserId(String email, String userId);

    @Transactional
    @Modifying
    void deleteUserByUserId(String userId);
    @Transactional
    Optional<User> deleteUserById(Integer id);
    Optional<User> findByUsernameAndEmail(String username, String email);
}
