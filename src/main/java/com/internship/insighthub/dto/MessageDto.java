package com.internship.insighthub.dto;

import com.internship.insighthub.entity.MessageRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor   // конструктор со всеми полями
@NoArgsConstructor    // пустой конструктор
public class MessageDto {

    private Long id;
    private MessageRole role;
    private String content;
    private LocalDateTime createdAt;
}
