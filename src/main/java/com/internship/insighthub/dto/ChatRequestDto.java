package com.internship.insighthub.dto;

public record ChatRequestDto(
        String question,
        Long sessionId
) {
}

