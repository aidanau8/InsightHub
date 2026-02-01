package com.internship.insighthub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sections")
public class TutorChatController {

    @PostMapping("/{sectionId}/chat")
    public ResponseEntity<Map<String, String>> chatWithTutor(
            @PathVariable Long sectionId,
            @RequestBody Map<String, String> body
    ) {
        // Сообщение пользователя из тела запроса (week11.js отправляет { "message": "..." })
        String question = body.getOrDefault("message", "");

        // Простейший "AI"-ответ (заглушка)
        String answer = """
                Thanks for your question about this section! 👩‍🏫
                                
                Section ID: %d
                Your question: "%s"
                                
                In this section we talk about:
                - primitive types (int, double, boolean, char)
                - reference types (String, arrays, objects)
                - type casting between different types.
                                
                Try to write a tiny Java example with 2–3 primitive variables
                and print them in the console.
                """.formatted(sectionId, question);

        // JSON, который ждёт week11.js → { "reply": "..." }
        return ResponseEntity.ok(Map.of("reply", answer));
    }
}
