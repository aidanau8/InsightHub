package com.internship.insighthub.service;

import com.internship.insighthub.dto.ChatRequestDto;
import com.internship.insighthub.dto.ChatResponseDto;
import com.internship.insighthub.entity.ChatMessage;
import com.internship.insighthub.entity.ChatSession;
import com.internship.insighthub.entity.User;
import com.internship.insighthub.repository.ChatMessageRepository;
import com.internship.insighthub.repository.ChatSessionRepository;
import com.internship.insighthub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional
    public ChatResponseDto chat(String email, ChatRequestDto request) {


        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setUsername(email);
                    // ВАЖНО: пароль не может быть null, иначе падает БД
                    u.setPassword("dummy-temp-password");
                    return userRepository.save(u);
                });


        ChatSession session;

        if (request.getConversationId() != null) {

            session = chatSessionRepository.findById(request.getConversationId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Chat session " + request.getConversationId() + " not found"));
            session.setUpdatedAt(LocalDateTime.now());
        } else {

            session = new ChatSession();
            session.setUser(user);
            session.setTitle(buildTitleFromMessage(request.getMessage()));
            session.setModelName(
                    request.getModelName() != null ? request.getModelName() : "demo-model1"
            );
            session.setCreatedAt(LocalDateTime.now());
            session.setUpdatedAt(LocalDateTime.now());
            session = chatSessionRepository.save(session);
        }


        ChatMessage userMessage = new ChatMessage();
        userMessage.setSession(session);
        userMessage.setRole("USER"); // строка, как в сущности
        userMessage.setContent(request.getMessage());
        userMessage.setCreatedAt(LocalDateTime.now());
        chatMessageRepository.save(userMessage);

        // 4. Генерируем демо-ответ
        String question = request.getMessage();
        String demoReply = "You said: \"" + question + "\" 😊 (demo reply)";

        ChatMessage assistantMessage = new ChatMessage();
        assistantMessage.setSession(session);
        assistantMessage.setRole("ASSISTANT");
        assistantMessage.setContent(demoReply);
        assistantMessage.setCreatedAt(LocalDateTime.now());
        chatMessageRepository.save(assistantMessage);


        List<ChatMessage> allMessages;
        try {
            allMessages = chatMessageRepository
                    .findBySessionIdOrderByCreatedAtAsc(session.getId());
        } catch (Exception e) {
            allMessages = Collections.emptyList();
        }


        ChatResponseDto response = new ChatResponseDto();
        response.setChatSessionId(session.getId());
        response.setResponse(demoReply);
        // history можно добавить позже, если будет нужно

        return response;
    }

    private String buildTitleFromMessage(String message) {
        if (message == null || message.isBlank()) {
            return "New conversation";
        }
        message = message.trim();
        return message.length() <= 50
                ? message
                : message.substring(0, 47) + "...";
    }
}
