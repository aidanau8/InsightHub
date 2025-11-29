package com.internship.insighthub.controller;

import com.internship.insighthub.dto.ChatMessageHistoryDto;
import com.internship.insighthub.dto.ChatRequestDto;
import com.internship.insighthub.dto.ChatResponseDto;
import com.internship.insighthub.dto.ChatSessionSummaryDto;
import com.internship.insighthub.entity.User;
import com.internship.insighthub.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // ===== 1. Старый, простой чат (без истории) =====
    @PostMapping("/simple")
    public ResponseEntity<ChatResponseDto> simpleChat(@RequestBody ChatRequestDto request) {
        String reply = chatService.processMessage(request.message());
        // sessionId пока не используем → null
        return ResponseEntity.ok(new ChatResponseDto(null, reply));
    }

    // ВРЕМЕННЫЙ метод: пока нет Spring Security, делаем фейкового пользователя
    private User fakeUser() {
        return User.builder()
                .id(1L)
                .username("test-user")
                .build();
    }

    // ===== 2. Чат с сохранением истории =====
    @PostMapping("/with-history")
    public ResponseEntity<ChatResponseDto> chatWithHistory(@RequestBody ChatRequestDto request) {
        User user = fakeUser(); // позже заменим на реального аутентифицированного
        ChatResponseDto response = chatService.chatWithHistory(request, user);
        return ResponseEntity.ok(response);
    }

    // ===== 3. Список всех чатов пользователя =====
    @GetMapping("/sessions")
    public ResponseEntity<List<ChatSessionSummaryDto>> getSessions() {
        User user = fakeUser();
        List<ChatSessionSummaryDto> sessions = chatService.getChatSessionsForUser(user);
        return ResponseEntity.ok(sessions);
    }

    // ===== 4. Сообщения конкретной сессии =====
    @GetMapping("/sessions/{id}")
    public ResponseEntity<List<ChatMessageHistoryDto>> getMessages(@PathVariable Long id) {
        User user = fakeUser();
        List<ChatMessageHistoryDto> messages = chatService.getMessagesForSession(id, user);
        return ResponseEntity.ok(messages);
    }
}
