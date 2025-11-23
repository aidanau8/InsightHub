package com.internship.insighthub.service;

import com.internship.insighthub.dto.UserDto;
import com.internship.insighthub.dto.UserRegistrationDto;
import com.internship.insighthub.entity.User;

public interface UserService {

    // регистрация нового пользователя
    User registerUser(UserRegistrationDto userData);

    // поиск пользователя по email
    User findByEmail(String email);

    // проверка пароля
    boolean verifyPassword(String rawPassword, String passwordHash);

    // поиск пользователя по username и возврат DTO
    UserDto findByUsername(String username);
}






