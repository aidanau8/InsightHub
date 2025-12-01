package com.internship.insighthub.service;

import com.internship.insighthub.dto.ChatRequestDto;
import com.internship.insighthub.dto.ChatResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    @Override
    public ChatResponseDto askQuestion(String email, ChatRequestDto request) {

        // подстрахуемся от null
        String question = request != null ? request.question() : null;

        if (question == null || question.isBlank()) {
            String emptyReply = "Hi, " + email +
                    "! You didn't ask a question. Please send something 🙂";

            // 🔹 ChatResponseDto ожидает: (Long chatSessionId, String reply, List<...> history)
            return new ChatResponseDto(
                    1L,
                    emptyReply,
                    List.of()   // пока пустая история сообщений
            );
        }

        String reply = "Dummy AI answer for user " + email +
                ". You asked: " + question;

        // временно всегда sessionId = 1 и пустая история
        return new ChatResponseDto(
                1L,
                reply,
                List.of()       // тоже пусто
        );
    }
}

