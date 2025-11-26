package com.internship.insighthub.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ChatService {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String apiKey;

    public ChatService(
            RestTemplate restTemplate,
            @Value("${ai.api.url:https://example.com/ai/chat}") String apiUrl,
            @Value("${ai.api.key:dummy-key}") String apiKey
    ) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    public String processMessage(String userMessage) {
        // Заголовки
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.setBearerAuth(apiKey);

        // Тело запроса
        Map<String, String> body = Map.of("message", userMessage);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            AiResponse response = restTemplate.postForObject(
                    apiUrl,
                    requestEntity,
                    AiResponse.class
            );

            if (response == null || response.reply() == null) {
                throw new IllegalStateException("AI reply is empty");
            }

            return response.reply();
        } catch (RestClientException ex) {
            throw new RuntimeException("AI service error", ex);
        }
    }

    // Мини-DTO для ответа внешнего сервиса
    public record AiResponse(String reply) {
    }
}
