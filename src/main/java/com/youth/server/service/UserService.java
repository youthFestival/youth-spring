package com.youth.server.service;

import com.youth.server.domain.User;
import com.youth.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByUserId(String s) {
        return userRepository.findByUserId(s);
    }
}
