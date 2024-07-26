package com.youth.server.service;

import com.youth.server.domain.User;
import com.youth.server.exception.NotFoundException;
import com.youth.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

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

}
