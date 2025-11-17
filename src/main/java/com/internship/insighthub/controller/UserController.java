package com.internship.insighthub.controller;

import com.internship.insighthub.dto.UserDto;
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

    // GET /api/users/{username}
    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getByUsername(@PathVariable String username) {
        UserDto user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }
}

