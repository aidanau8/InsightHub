package com.internship.insighthub.service;

import com.internship.insighthub.dto.ConversationDto;

import java.util.List;

public interface ConversationService {

    List<ConversationDto> getUserConversations(Long userId);

    ConversationDto getConversation(Long conversationId);
}