package com.example.admin.service;

import com.example.admin.entity.User;
import com.example.admin.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Refresh Token 저장 (DB에 저장)
    public void updateRefreshToken(String username, String refreshToken) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        user.getStatus(refreshToken);
        userRepository.save(user);
    }
}