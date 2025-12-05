package com.internship.insighthub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRequestDto {

    @NotBlank
    private String message;

    private Long conversationId;

    @NotBlank
    private String modelName;
}
