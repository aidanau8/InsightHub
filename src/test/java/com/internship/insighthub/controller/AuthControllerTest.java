package com.internship.insighthub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.insighthub.dto.UserRegistrationDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 🟣 1) Пустое body → BAD_REQUEST
    @Test
    void register_withEmptyBody_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // 🟣 2) Пустые поля → BAD_REQUEST
    @Test
    void register_withEmptyFields_shouldReturnBadRequest() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("");
        dto.setEmail("");
        dto.setPassword("");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}

