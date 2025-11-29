package com.internship.insighthub.dto;

import java.time.LocalDateTime;

public record ChatSessionSummaryDto(
        Long id,
        String title,
        LocalDateTime createdAt
) {

}

