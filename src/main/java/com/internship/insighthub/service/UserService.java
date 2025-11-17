package com.internship.insighthub.service;

import com.internship.insighthub.dto.UserDto;
import com.internship.insighthub.dto.UserRegistrationDto;
import com.internship.insighthub.entity.User;

public interface UserService {

    // 🔹 Регистрация нового пользователя
    User registerUser(UserRegistrationDto userData);

    // 🔹 Поиск пользователя по email (для логина)
    User findByEmail(String email);

    // 🔹 Проверка пароля (сырое значение + захешированное)
    boolean verifyPassword(String rawPassword, String passwordHash);

    // 🔹 Новый метод для UserController — получить пользователя по username как DTO
    UserDto findByUsername(String username);
}





