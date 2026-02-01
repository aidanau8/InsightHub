package com.internship.insighthub.service;

import com.internship.insighthub.dto.ConversationDto;
import com.internship.insighthub.entity.Conversation;
import com.internship.insighthub.entity.Section;
import com.internship.insighthub.entity.User;
import com.internship.insighthub.exception.NotFoundException;
import com.internship.insighthub.repository.ConversationRepository;
import com.internship.insighthub.repository.SectionRepository;
import com.internship.insighthub.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@Transactional
public class SectionConversationServiceImpl implements SectionConversationService {

    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;

    public SectionConversationServiceImpl(SectionRepository sectionRepository,
                                          UserRepository userRepository,
                                          ConversationRepository conversationRepository) {
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
    }

    @Override
    public ConversationDto createNewConversation(Long sectionId, Long userId, String modelName, String title) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("Section not found: " + sectionId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        Conversation c = new Conversation();
        c.setSection(section);
        c.setUser(user);
        c.setModelName(modelName == null || modelName.isBlank() ? "default-model" : modelName);
        c.setTitle(title == null || title.isBlank() ? "New conversation" : title);
        c.setActive(true);
        c.setCreatedAt(Instant.now());
        c.setUpdatedAt(Instant.now());

        Conversation saved = conversationRepository.save(c);

        return new ConversationDto(
                saved.getId(),
                section.getId(),
                saved.getTitle(),
                saved.isActive(),
                saved.getModelName(),
                toLocalDateTime(saved.getCreatedAt()),
                toLocalDateTime(saved.getUpdatedAt()),
                List.of()
        );
    }

    @Override
    public ConversationDto clearAndStartNew(Long sectionId, Long userId, String modelName) {
        // Option 2: "tabs" — мы НЕ удаляем старые, просто деактивируем
        conversationRepository.deactivateBySectionIdAndUserId(sectionId, userId);

        return createNewConversation(sectionId, userId, modelName, "New conversation");
    }

    private LocalDateTime toLocalDateTime(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}