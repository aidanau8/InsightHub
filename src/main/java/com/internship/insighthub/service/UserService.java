package com.internship.insighthub.service;

import com.internship.insighthub.model.User;
import com.internship.insighthub.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User create(String username, String passwordHash) {
        if (repo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }
        return repo.save(new User(username, passwordHash));
    }

    @Transactional(readOnly = true)
    public List<User> all() {
        return repo.findAll();
    }
}
