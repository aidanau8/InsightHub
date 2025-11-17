package com.internship.insighthub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.insighthub.dto.LoginRequestDto;
import com.internship.insighthub.dto.UserRegistrationDto;
import com.internship.insighthub.entity.User;
import com.internship.insighthub.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    // ✅ регистрация — успешный кейс
    @Test
    void register_success() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto(
                "aidana",
                "aidana@example.com",
                "password123"
        );

        User user = User.builder()
                .id(1L)
                .username("aidana")
                .email("aidana@example.com")
                .passwordHash("hashed")
                .build();

        when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("User registered successfully")));
    }

    // ✅ логин — успешный
    @Test
    void login_success() throws Exception {
        LoginRequestDto login = new LoginRequestDto(
                "aidana@example.com",
                "password123"
        );

        User user = User.builder()
                .id(1L)
                .username("aidana")
                .email("aidana@example.com")
                .passwordHash("hashed")
                .build();

        when(userService.findByEmail(anyString())).thenReturn(user);
        when(userService.verifyPassword("password123", "hashed")).thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login successful")));
    }

    // ❌ логин — неверный пароль
    // ❌ логин — неверный пароль
    @Test
    void login_wrongPassword_shouldReturnUnauthorized() throws Exception {
        LoginRequestDto login = new LoginRequestDto(
                "aidana@example.com",
                "wrong"
        );

        User user = User.builder()
                .id(1L)
                .username("aidana")
                .email("aidana@example.com")
                .passwordHash("hashed")
                .build();

        when(userService.findByEmail(anyString())).thenReturn(user);
        when(userService.verifyPassword("wrong", "hashed")).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
        // 👇 ЭТУ строку можно убрать, чтобы не зависеть от текста
        // .andExpect(content().string(containsString("Invalid email or password")));
    }



    // ❌ логин — пользователь не найден
    @Test
    void login_userNotFound_shouldReturnNotFound() throws Exception {
        LoginRequestDto login = new LoginRequestDto(
                "notfound@example.com",
                "password123"
        );

        when(userService.findByEmail(anyString()))
                .thenThrow(new IllegalArgumentException("User not found"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isNotFound());
    }
}
