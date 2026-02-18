package com.internship.insighthub.controller;

import com.internship.insighthub.service.OpenAiChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sections")
public class TutorChatController {

    private final OpenAiChatService openAiChatService;

    public TutorChatController(OpenAiChatService openAiChatService) {
        this.openAiChatService = openAiChatService;
    }

    @PostMapping("/{sectionId}/chat")
    public ResponseEntity<Map<String, String>> chatWithTutor(
            @PathVariable Long sectionId,
            @RequestBody Map<String, String> body
    ) {
        String question = body.getOrDefault("message", "").trim();
        if (question.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("response", "Please type a question."));
        }

        String systemPrompt = """
                You are an AI tutor inside a learning app called InsightHub.
                Be concise, clear, friendly. Use simple examples.
                The current sectionId is %d.
                """.formatted(sectionId);

        String answer = openAiChatService.ask(systemPrompt, question);
        return ResponseEntity.ok(Map.of("response", answer));
    }
}
