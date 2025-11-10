package com.internship.insighthub.controller;

import com.internship.insighthub.model.User;
import com.internship.insighthub.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/users/aidana
    @GetMapping("/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }
}

