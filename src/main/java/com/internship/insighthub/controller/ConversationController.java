package com.internship.insighthub.controller;

import com.internship.insighthub.dto.ConversationDto;
import com.internship.insighthub.service.ConversationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ConversationDto>> getUserConversations(@PathVariable Long userId) {
        return ResponseEntity.ok(conversationService.getUserConversations(userId));
    }


    @GetMapping("/{conversationId}")
    public ResponseEntity<ConversationDto> getConversation(@PathVariable Long conversationId) {
        return ResponseEntity.ok(conversationService.getConversation(conversationId));
    }
}