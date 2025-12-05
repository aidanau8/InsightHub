package com.internship.insighthub.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ConversationDetailDto {

    private Long id;
    private String title;
    private String modelName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<MessageDto> messages;
}

