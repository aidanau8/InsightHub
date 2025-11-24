package com.internship.insighthub.controller;

import com.internship.insighthub.dto.LoginRequestDto;
import com.internship.insighthub.dto.UserRegistrationDto;
import com.internship.insighthub.entity.User;
import com.internship.insighthub.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto userData) {

        // 🔍 Валидация пустых полей
        if (userData == null
                || isBlank(userData.getEmail())
                || isBlank(userData.getPassword())
                || isBlank(userData.getUsername())) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Validation failed: fields cannot be empty");
        }

        try {
            User user = userService.registerUser(userData);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully: " + user.getUsername());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
