package com.internship.insighthub.service;

import com.internship.insighthub.dto.ChatRequestDto;
import com.internship.insighthub.dto.ChatResponseDto;

/**
 * Сервис для работы с чат-запросами пользователя.
 */
public interface ChatService {

    /**
     * Обрабатывает вопрос пользователя.
     *
     * @param email   email пользователя, извлечённый из токена
     * @param request тело запроса с полями question и sessionId
     * @return ответ чата (reply + chatSessionId)
     */
    ChatResponseDto askQuestion(String email, ChatRequestDto request);
}
