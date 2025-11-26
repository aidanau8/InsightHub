package com.internship.insighthub.controller;

import com.internship.insighthub.dto.ChatRequestDto;
import com.internship.insighthub.dto.ChatResponseDto;
import com.internship.insighthub.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponseDto> chat(@RequestBody ChatRequestDto request) {
        String reply = chatService.processMessage(request.message());
        return ResponseEntity.ok(new ChatResponseDto(reply));
    }
}
