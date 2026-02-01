package com.internship.insighthub.service;

import com.internship.insighthub.dto.ChatRequestDto;
import com.internship.insighthub.dto.ChatResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    @Override
    @Transactional
    public ChatResponseDto chatWithoutHistory(ChatRequestDto request) {
        ChatResponseDto response = new ChatResponseDto();
        // заглушка: просто эхо
        response.setResponse("Echo (без истории): " + request.getQuestion());
        response.setChatSessionId(request.getSessionId());
        return response;
    }

    @Override
    @Transactional
    public ChatResponseDto chatWithHistory(ChatRequestDto request) {
        ChatResponseDto response = new ChatResponseDto();
        // пока тоже заглушка
        response.setResponse("Echo (с историей): " + request.getQuestion());
        response.setChatSessionId(request.getSessionId());
        return response;
    }
}
