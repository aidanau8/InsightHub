package com.internship.insighthub.service;

import com.internship.insighthub.dto.UserRegistrationDto;
import com.internship.insighthub.model.User;
import com.internship.insighthub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserRegistrationDto userData) {
        userRepository.findByUsername(userData.username())
                .ifPresent(u -> { throw new IllegalArgumentException("Username already exists"); });

        User user = new User();
        user.setUsername(userData.username());
        user.setPasswordHash(passwordEncoder.encode(userData.password()));
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedHash) {
        return passwordEncoder.matches(rawPassword, encodedHash);
    }
}

