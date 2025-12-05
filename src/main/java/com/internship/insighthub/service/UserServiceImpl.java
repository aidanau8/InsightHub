package com.internship.insighthub.service;

import com.internship.insighthub.dto.LoginRequestDto;
import com.internship.insighthub.dto.UserDto;
import com.internship.insighthub.dto.UserRegistrationDto;
import com.internship.insighthub.entity.User;
import com.internship.insighthub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserRegistrationDto userData) {
        if (userData == null
                || isBlank(userData.getUsername())
                || isBlank(userData.getEmail())
                || isBlank(userData.getPassword())) {
            throw new IllegalArgumentException("Validation failed: fields cannot be empty");
        }

        if (userRepository.existsByUsername(userData.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(userData.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(userData.getUsername());
        user.setEmail(userData.getEmail());
        user.setPassword(passwordEncoder.encode(userData.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean verifyPassword(String rawPassword, String passwordHash) {
        return passwordEncoder.matches(rawPassword, passwordHash);
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }

    @Override
    public String login(LoginRequestDto loginData) {
        if (loginData == null
                || isBlank(loginData.getEmail())
                || isBlank(loginData.getPassword())) {
            throw new IllegalArgumentException("Validation failed: email and password cannot be empty");
        }

        User user = userRepository.findByEmail(loginData.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(loginData.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }


        return "dummy-token-for-" + user.getEmail();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
