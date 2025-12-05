package com.internship.insighthub.controller;

import com.internship.insighthub.dto.LoginRequestDto;
import com.internship.insighthub.dto.UserRegistrationDto;
import com.internship.insighthub.entity.User;
import com.internship.insighthub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto userData) {


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


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginData) {

        if (loginData == null
                || isBlank(loginData.getEmail())
                || isBlank(loginData.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Validation failed: email and password cannot be empty");
        }

        try {

            String token = userService.login(loginData);
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

