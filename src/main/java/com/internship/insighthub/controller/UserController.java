package com.internship.insighthub.controller;

import com.internship.insighthub.model.User;
import com.internship.insighthub.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestParam String username,
                                       @RequestParam String passwordHash) {
        return ResponseEntity.ok(service.create(username, passwordHash));
    }

    @GetMapping
    public List<User> all() {
        return service.all();
    }
}
