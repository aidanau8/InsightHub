package com.internship.insighthub.dto;

import java.util.List;

public record ChatResponseDto(
        Long sessionId,
        String answer,
        List<ChatMessageHistoryDto> messages
) {
}
