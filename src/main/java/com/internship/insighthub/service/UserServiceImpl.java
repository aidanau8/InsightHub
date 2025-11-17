package com.internship.insighthub.service;

import com.internship.insighthub.dto.UserDto;
import com.internship.insighthub.dto.UserRegistrationDto;
import com.internship.insighthub.entity.User;
import com.internship.insighthub.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 🔹 Регистрация нового пользователя (используется в AuthController.register)
    @Override
    public User registerUser(UserRegistrationDto userData) {

        // 1) Проверка: username уже занят?
        userRepository.findByUsername(userData.username())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Username already exists");
                });

        // 2) Проверка: email уже используется?
        userRepository.findByEmail(userData.email())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Email already exists");
                });

        // 3) Создаём нового пользователя
        User user = new User();
        user.setUsername(userData.username());
        user.setEmail(userData.email());
        // TODO: здесь позже добавим шифрование пароля через PasswordEncoder
        user.setPasswordHash(userData.password());  // или passwordHash(), если поле так называется

        // 4) Сохраняем в базе
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public boolean verifyPassword(String rawPassword, String passwordHash) {
        return rawPassword.equals(passwordHash);
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        return dto;
    }
}
