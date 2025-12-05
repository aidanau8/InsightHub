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

    @PostMapping("/ask")
    public ResponseEntity<ChatResponseDto> ask(@RequestBody ChatRequestDto request) {
        // 👇 Временная заглушка: работаем от имени одного пользователя
        String email = "aidana20@example.com";

        ChatResponseDto response = chatService.chat(email, request);
        return ResponseEntity.ok(response);
    }
}
