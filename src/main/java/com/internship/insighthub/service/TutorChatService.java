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

    private final TutorChatMessageRepository messageRepository;



    public void resetChatForSection(Long sectionId) {

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Section not found: " + sectionId));


        List<TutorChatSession> sessions = sessionRepository.findAllBySection(section);


        sessionRepository.deleteAll(sessions);


        TutorChatSession newSession = new TutorChatSession();
        newSession.setSection(section);
        newSession.setCreatedAt(Instant.now());
        newSession.setActive(true);

        sessionRepository.save(newSession);
    }
}