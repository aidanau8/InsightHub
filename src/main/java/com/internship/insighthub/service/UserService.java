package com.internship.insighthub.service;

import com.internship.insighthub.dto.LoginRequestDto;
import com.internship.insighthub.dto.UserDto;
import com.internship.insighthub.dto.UserRegistrationDto;
import com.internship.insighthub.entity.User;

public interface UserService {

    User registerUser(UserRegistrationDto userData);

    User findByEmail(String email);

    boolean verifyPassword(String rawPassword, String passwordHash);

    UserDto findByUsername(String username);


    String login(LoginRequestDto loginData);
}
