package com.internship.insighthub.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatServiceTest {

    @Test
    void processMessage_shouldReturnReply_whenApiOk() {
        // given
        RestTemplate restTemplate = mock(RestTemplate.class);
        ChatService.AiResponse apiResponse =
                new ChatService.AiResponse("hello from ai");

        when(restTemplate.postForObject(
                anyString(),
                any(),
                eq(ChatService.AiResponse.class)
        )).thenReturn(apiResponse);

        ChatService chatService =
                new ChatService(restTemplate, "http://fake-url", "fake-key");

        // when
        String result = chatService.processMessage("Hi");

        // then
        assertEquals("hello from ai", result);
    }

    @Test
    void processMessage_shouldThrow_whenReplyNull() {
        // given
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.postForObject(
                anyString(),
                any(),
                eq(ChatService.AiResponse.class)
        )).thenReturn(new ChatService.AiResponse(null));

        ChatService chatService =
                new ChatService(restTemplate, "http://fake-url", "fake-key");

        // then
        assertThrows(IllegalStateException.class,
                () -> chatService.processMessage("Hi"));
    }

    @Test
    void processMessage_shouldWrapRestException() {
        // given
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.postForObject(
                anyString(),
                any(),
                eq(ChatService.AiResponse.class)
        )).thenThrow(new RestClientException("boom"));

        ChatService chatService =
                new ChatService(restTemplate, "http://fake-url", "fake-key");

        // then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> chatService.processMessage("Hi"));

        assertTrue(ex.getMessage().contains("AI service error"));
    }
}
