package com.internship.insighthub.service;

import com.internship.insighthub.dto.ChatRequestDto;
import com.internship.insighthub.dto.ChatResponseDto;

public interface ChatService {


    ChatResponseDto chat(String email, ChatRequestDto request);
}
