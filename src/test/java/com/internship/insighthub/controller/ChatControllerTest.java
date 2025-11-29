package com.internship.insighthub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.insighthub.dto.ChatMessageHistoryDto;
import com.internship.insighthub.dto.ChatRequestDto;
import com.internship.insighthub.dto.ChatResponseDto;
import com.internship.insighthub.dto.ChatSessionSummaryDto;
import com.internship.insighthub.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ 1. Простой чат (без истории)
    @Test
    void chatWithoutAuth_shouldReturnOkAndReply() throws Exception {
        ChatRequestDto dto = new ChatRequestDto(null, "Hello");

        when(chatService.processMessage("Hello")).thenReturn("AI reply");

        mockMvc.perform(post("/api/chat/simple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value("AI reply"))
                .andExpect(jsonPath("$.chatSessionId").doesNotExist()); // или isEmpty()
    }

    // ✅ 2. Чат с историей
    @Test
    void chatWithHistory_shouldReturnSessionIdAndReply() throws Exception {
        ChatRequestDto dto = new ChatRequestDto(null, "Hi with history");

        ChatResponseDto responseDto = new ChatResponseDto(10L, "History reply");

        when(chatService.chatWithHistory(any(ChatRequestDto.class), any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/chat/with-history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatSessionId").value(10L))
                .andExpect(jsonPath("$.reply").value("History reply"));
    }

    // ✅ 3. Список сессий
    @Test
    void getSessions_shouldReturnListOfSessions() throws Exception {
        List<ChatSessionSummaryDto> sessions = List.of(
                new ChatSessionSummaryDto(1L, "First chat", LocalDateTime.now()),
                new ChatSessionSummaryDto(2L, "Second chat", LocalDateTime.now())
        );

        when(chatService.getChatSessionsForUser(any()))
                .thenReturn(sessions);

        mockMvc.perform(get("/api/chat/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("First chat"))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    // ✅ 4. Сообщения конкретной сессии
    @Test
    void getMessages_shouldReturnListOfMessages() throws Exception {
        List<ChatMessageHistoryDto> messages = List.of(
                new ChatMessageHistoryDto("user", "Hello", LocalDateTime.now()),
                new ChatMessageHistoryDto("ai", "Hi!", LocalDateTime.now())
        );

        when(chatService.getMessagesForSession(eq(5L), any()))
                .thenReturn(messages);

        mockMvc.perform(get("/api/chat/sessions/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role").value("user"))
                .andExpect(jsonPath("$[0].content").value("Hello"))
                .andExpect(jsonPath("$[1].role").value("ai"));
    }
}
