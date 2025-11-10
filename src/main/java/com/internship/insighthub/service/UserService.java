package com.internship.insighthub.service;

import com.internship.insighthub.dto.UserRegistrationDto;
import com.internship.insighthub.model.User;

public interface UserService {
    User registerUser(UserRegistrationDto userData);
    User findByUsername(String username);
    boolean verifyPassword(String rawPassword, String encodedHash);
}
