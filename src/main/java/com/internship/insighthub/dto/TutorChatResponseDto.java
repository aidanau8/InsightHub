package com.internship.insighthub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TutorChatResponseDto {
    private Long sessionId;
    private String reply;
}
