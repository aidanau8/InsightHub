package com.internship.insighthub.dto;

import java.time.LocalDateTime;

public record ChatMessageHistoryDto(
        String role,
        String content,
        LocalDateTime createdAt
) {

}

