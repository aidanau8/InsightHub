package com.internship.insighthub.controller;

import com.internship.insighthub.dto.LoginRequestDto;
import com.internship.insighthub.dto.UserRegistrationDto;
import com.internship.insighthub.model.User;
import com.internship.insighthub.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Регистрация нового пользователя
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto userData) {
        try {
            User user = userService.registerUser(userData);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully: " + user.getUsername());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    // ✅ Логин
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginData) {
        try {
            User user = userService.findByUsername(loginData.username());
            boolean isValid = userService.verifyPassword(loginData.password(), user.getPasswordHash());

            if (!isValid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid credentials");
            }

            return ResponseEntity.ok("Login successful for user: " + user.getUsername());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
