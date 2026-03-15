package com.internship.insighthub.service;

import com.internship.insighthub.dto.ConversationDto;

public interface SectionConversationService {


    ConversationDto createNewConversation(Long sectionId, Long userId, String modelName, String title);


    ConversationDto clearAndStartNew(Long sectionId, Long userId, String modelName);
}