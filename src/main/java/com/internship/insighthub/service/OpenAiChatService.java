package com.internship.insighthub.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OpenAiChatService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    public String ask(String systemPrompt, String userMessage) {
        String url = "https://api.openai.com/v1/chat/completions";

        ChatCompletionRequest body = new ChatCompletionRequest(
                model,
                List.of(
                        new Message("system", systemPrompt),
                        new Message("user", userMessage)
                ),
                0.4
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<ChatCompletionRequest> entity = new HttpEntity<>(body, headers);

        ResponseEntity<ChatCompletionResponse> res =
                restTemplate.exchange(url, HttpMethod.POST, entity, ChatCompletionResponse.class);

        ChatCompletionResponse response = res.getBody();
        if (response == null || response.choices == null || response.choices.isEmpty()) {
            return "No response from AI.";
        }

        Message msg = response.choices.get(0).message;
        return msg != null && msg.content != null ? msg.content.trim() : "Empty AI response.";
    }

    // ---- DTOs ----
    public record ChatCompletionRequest(String model, List<Message> messages, double temperature) {}

    public record Message(String role, String content) {}

    public static class ChatCompletionResponse {
        public List<Choice> choices;

        public static class Choice {
            public Message message;
        }
    }
}
