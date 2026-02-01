package com.internship.insighthub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDto {

    private Long id;
    private Long sectionId;

    private String title;
    private boolean active;
    private String modelName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<MessageDto> history;
}