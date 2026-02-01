package com.internship.insighthub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TutorChatRequestDto {
    private Long sectionId;
    private String message;
}
