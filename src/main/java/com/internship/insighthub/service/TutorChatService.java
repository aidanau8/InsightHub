package com.internship.insighthub.service;

import com.internship.insighthub.dto.TutorChatRequestDto;
import com.internship.insighthub.dto.TutorChatResponseDto;
import com.internship.insighthub.entity.Section;
import com.internship.insighthub.model.TutorChatMessage;
import com.internship.insighthub.entity.TutorChatSession;
import com.internship.insighthub.model.TutorMessageRole;
import com.internship.insighthub.repository.SectionRepository;
import com.internship.insighthub.repository.TutorChatSessionRepository;
import com.internship.insighthub.repository.TutorChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TutorChatService {

    private final SectionRepository sectionRepository;
    private final TutorChatSessionRepository sessionRepository;
    // ⬇️ ВАЖНО: тут тип именно TutorChatMessageRepository, без лишнего "Repository"
    private final TutorChatMessageRepository messageRepository;


    // 👇 тут остаётся твой существующий метод askQuestion(...)
    // public TutorChatResponseDto askQuestion(TutorChatRequestDto request) { ... }

    // 👇 А ВОТ ТУТ — НОВЫЙ МЕТОД resetChatForSection, ВНУТРИ КЛАССА
    public void resetChatForSection(Long sectionId) {
        // 1. Находим секцию по id
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Section not found: " + sectionId));

        // 2. Находим все чат-сессии по этой секции
        List<TutorChatSession> sessions = sessionRepository.findAllBySection(section);

        // 3. Удаляем все старые сессии (сообщения удалятся каскадно
        //    благодаря cascade = ALL + orphanRemoval = true в TutorChatSession)
        sessionRepository.deleteAll(sessions);

        // 4. Создаём новую пустую сессию
        TutorChatSession newSession = new TutorChatSession();
        newSession.setSection(section);
        newSession.setCreatedAt(Instant.now());
        newSession.setActive(true);

        sessionRepository.save(newSession);
    }
}