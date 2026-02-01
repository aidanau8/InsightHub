package com.internship.insighthub.dto;

import lombok.Data;

@Data
public class ChatRequestDto {

    // тело запроса: { "question": "Hello", "sessionId": 1 }
    private String question;
    private Long sessionId;
}
