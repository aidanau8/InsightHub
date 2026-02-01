package com.internship.insighthub.service;

import com.internship.insighthub.dto.ConversationDto;

public interface SectionConversationService {

    // создать новую беседу (таб) в section
    ConversationDto createNewConversation(Long sectionId, Long userId, String modelName, String title);

    // очистить чат: сделать новую беседу и (опционально) деактивировать старые
    ConversationDto clearAndStartNew(Long sectionId, Long userId, String modelName);
}