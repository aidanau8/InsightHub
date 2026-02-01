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

    @PostMapping("/simple")
    public ResponseEntity<ChatResponseDto> chatWithoutHistory(@RequestBody ChatRequestDto request) {
        ChatResponseDto response = chatService.chatWithoutHistory(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/with-history")
    public ResponseEntity<ChatResponseDto> chatWithHistory(@RequestBody ChatRequestDto request) {
        ChatResponseDto response = chatService.chatWithHistory(request);
        return ResponseEntity.ok(response);
    }
}
