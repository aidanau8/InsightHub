package com.internship.insighthub.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatResponseDto {

    private Long chatSessionId;
    private String response;
    private List<MessageDto> history;

    private Long conversationId;
    private boolean newConversation;
    private String conversationTitle;
    private String modelName;
}
