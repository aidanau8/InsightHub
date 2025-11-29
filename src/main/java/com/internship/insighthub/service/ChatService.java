package com.internship.insighthub.service;

import com.internship.insighthub.dto.ChatRequestDto;
import com.internship.insighthub.dto.ChatResponseDto;
import com.internship.insighthub.entity.User;
import com.internship.insighthub.model.ChatMessage;
import com.internship.insighthub.model.ChatSession;
import com.internship.insighthub.repository.ChatMessageRepository;
import com.internship.insighthub.repository.ChatSessionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.internship.insighthub.dto.ChatMessageHistoryDto;
import com.internship.insighthub.dto.ChatSessionSummaryDto;

import java.util.Comparator;
import java.util.List;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ChatService {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String apiKey;

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatService(
            RestTemplate restTemplate,
            ChatSessionRepository chatSessionRepository,
            ChatMessageRepository chatMessageRepository,
            @Value("${ai.api.url:https://example.com/ai/chat}") String apiUrl,
            @Value("${ai.api.key:dummy-key}") String apiKey
    ) {
        this.restTemplate = restTemplate;
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    /**
     * Старый метод: просто ходит во внешний AI по строке.
     * Его НЕ трогаем — он нужен для уже существующего контроллера и тестов.
     */
    public String processMessage(String userMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

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

    /**
     * Новый метод: сохраняет историю в PostgreSQL
     * и возвращает sessionId + ответ AI.
     */
    public ChatResponseDto chatWithHistory(ChatRequestDto request, User user) {
        // 1. Находим или создаём сессию
        ChatSession session;

        if (request.chatSessionId() == null) {
            // новая сессия
            String title = generateTitleFromMessage(request.message());

            session = ChatSession.builder()
                    .title(title)
                    .createdAt(LocalDateTime.now())
                    .user(user)
                    .build();

            session = chatSessionRepository.save(session);
        } else {
            // продолжаем существующую
            session = chatSessionRepository.findById(request.chatSessionId())
                    .orElseThrow(() -> new IllegalArgumentException("Chat session not found"));
        }

        // 2. Сохраняем сообщение пользователя
        ChatMessage userMessage = ChatMessage.builder()
                .role("user")
                .content(request.message())
                .createdAt(LocalDateTime.now())
                .session(session)
                .build();
        chatMessageRepository.save(userMessage);

        // 3. Получаем ответ от AI (используем уже готовый метод)
        String aiReply = processMessage(request.message());

        // 4. Сохраняем ответ AI
        ChatMessage aiMessage = ChatMessage.builder()
                .role("ai")
                .content(aiReply)
                .createdAt(LocalDateTime.now())
                .session(session)
                .build();
        chatMessageRepository.save(aiMessage);

        // 5. Возвращаем DTO с ID сессии и ответом
        return new ChatResponseDto(session.getId(), aiReply);
    }

    private String generateTitleFromMessage(String message) {
        if (message == null || message.isBlank()) {
            return "New chat";
        }
        String trimmed = message.trim();
        return trimmed.length() <= 50 ? trimmed : trimmed.substring(0, 50) + "...";
    }

    // Мини-DTO для ответа внешнего сервиса
    public record AiResponse(String reply) {
    }


    /**
     * Получить список чатов для пользователя (последние сверху).
     */
    public List<ChatSessionSummaryDto> getChatSessionsForUser(User user) {
        var sessions = chatSessionRepository.findByUserOrderByCreatedAtDesc(user);
        return sessions.stream()
                .map(s -> new ChatSessionSummaryDto(
                        s.getId(),
                        s.getTitle(),
                        s.getCreatedAt()
                ))
                .toList();
    }

    /**
     * Получить сообщения конкретной сессии (отфильтровать по пользователю).
     */
    public List<ChatMessageHistoryDto> getMessagesForSession(Long sessionId, User user) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Chat session not found"));

        // простая проверка "принадлежит ли чат этому пользователю"
        if (session.getUser() == null || !session.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Chat session does not belong to user");
        }

        return session.getMessages().stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedAt))
                .map(m -> new ChatMessageHistoryDto(
                        m.getRole(),
                        m.getContent(),
                        m.getCreatedAt()
                ))
                .toList();
    }

}
