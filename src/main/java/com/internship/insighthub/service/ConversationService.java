package com.internship.insighthub.service;

import com.internship.insighthub.dto.ConversationDetailDto;
import com.internship.insighthub.dto.ConversationSummaryDto;

import java.util.List;

public interface ConversationService {


    List<ConversationSummaryDto> getUserConversations(String userEmail);


    ConversationDetailDto getConversationDetail(String userEmail, Long conversationId);


    void deleteConversation(String userEmail, Long conversationId);
}

