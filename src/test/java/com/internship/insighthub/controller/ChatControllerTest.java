package com.internship.insighthub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.insighthub.dto.ChatRequestDto;
import com.internship.insighthub.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
@AutoConfigureMockMvc(addFilters = false)   // 🔥 выключаем Security-фильтры в тестах
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatService chatService;        // мок нашего сервиса

    // 1️⃣ Тест: обычный запрос без токена — просто проверяем, что контроллер работает
    @Test
    void chatWithoutAuth_shouldReturnOkAndReply() throws Exception {
        ChatRequestDto dto = new ChatRequestDto("Hello");

        when(chatService.processMessage("Hello")).thenReturn("AI reply");

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value("AI reply"));
    }

    // 2️⃣ Тест: с @WithMockUser — тоже ожидаем 200 и правильный ответ
    @Test
    @WithMockUser(username = "testUser")
    void chatWithMockUser_shouldReturnOkAndReply() throws Exception {
        ChatRequestDto dto = new ChatRequestDto("Hi");

        when(chatService.processMessage("Hi")).thenReturn("AI reply");

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value("AI reply"));
    }
}
