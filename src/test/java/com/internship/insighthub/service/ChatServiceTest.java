package com.internship.insighthub.service;

import com.internship.insighthub.model.ChatMessage;
import com.internship.insighthub.model.ChatSession;
import com.internship.insighthub.repository.ChatMessageRepository;
import com.internship.insighthub.repository.ChatSessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ChatServiceTest {

    @Test
    void processMessage_shouldReturnReply_whenApiOk() {
        // given
        RestTemplate restTemplate = mock(RestTemplate.class);
        ChatSessionRepository chatSessionRepository = mock(ChatSessionRepository.class);
        ChatMessageRepository chatMessageRepository = mock(ChatMessageRepository.class);

        ChatService.AiResponse apiResponse = new ChatService.AiResponse("hello from ai");

        when(restTemplate.postForObject(
                anyString(),
                any(),
                eq(ChatService.AiResponse.class)
        )).thenReturn(apiResponse);

        ChatService chatService = new ChatService(
                restTemplate,
                chatSessionRepository,
                chatMessageRepository,
                "http://fake-url",
                "fake-key"
        );

        // when
        String result = chatService.processMessage("Hi");

        // then
        assertEquals("hello from ai", result);
    }


    @Test
    void processMessage_shouldThrow_whenReplyNull() {
        // given
        RestTemplate restTemplate = mock(RestTemplate.class);
        ChatSessionRepository chatSessionRepository = mock(ChatSessionRepository.class);
        ChatMessageRepository chatMessageRepository = mock(ChatMessageRepository.class);

        when(restTemplate.postForObject(
                anyString(),
                any(),
                eq(ChatService.AiResponse.class)
        )).thenReturn(null);

        ChatService chatService = new ChatService(
                restTemplate,
                chatSessionRepository,
                chatMessageRepository,
                "http://fake-url",
                "fake-key"
        );

        // then
        try {
            chatService.processMessage("Hello");
        } catch (IllegalStateException ex) {
            assertEquals("AI reply is empty", ex.getMessage());
        }
    }
}
