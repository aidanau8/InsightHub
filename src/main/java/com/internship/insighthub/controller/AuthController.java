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

    // ✅ Регистрация нового пользователя (REST API)
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody UserRegistrationDto userData,
            BindingResult bindingResult
    ) {

        // ❗ Если поля пустые / не проходят Bean Validation → 400 BAD_REQUEST
        if (bindingResult.hasErrors()) {
            Map<String, Object> body = new HashMap<>();
            body.put("status", HttpStatus.BAD_REQUEST.value());
            body.put("message", "Validation failed");

            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            body.put("errors", errors);

            return ResponseEntity.badRequest().body(body);
        }

        try {
            User user = userService.registerUser(userData);

            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.CREATED.value());
            response.put("message", "User registered successfully");
            response.put("username", user.getUsername());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // Например, пользователь с таким email/username уже есть
            Map<String, Object> body = new HashMap<>();
            body.put("status", HttpStatus.CONFLICT.value());
            body.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }
    }

    // ✅ Логин по email + пароль
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginData) {
        try {
            User user = userService.findByEmail(loginData.getEmail());
            boolean isValid = userService.verifyPassword(
                    loginData.getPassword(),
                    user.getPasswordHash()
            );

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
