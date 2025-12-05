package com.internship.insighthub.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ConversationSummaryDto {

    private Long id;
    private String title;
    private String modelName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private Integer messageCount;
    private String lastMessagePreview;
}
