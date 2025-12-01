package com.internship.insighthub.controller;

import com.internship.insighthub.dto.ChatRequestDto;
import com.internship.insighthub.dto.ChatResponseDto;
import com.internship.insighthub.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // POST /api/chat/ask
    @PostMapping("/ask")
    public ResponseEntity<ChatResponseDto> askQuestion(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ChatRequestDto request
    ) {
        // ожидаем: "Bearer dummy-token-for-aidana@example.com"
        String token = authorization.replace("Bearer", "").trim();

        String email = token;
        String prefix = "dummy-token-for-";
        if (token.startsWith(prefix)) {
            email = token.substring(prefix.length());
        }

        ChatResponseDto response = chatService.askQuestion(email, request);
        return ResponseEntity.ok(response);
    }
}
