package com.internship.insighthub.repository;

import com.internship.insighthub.entity.Section;
import com.internship.insighthub.entity.TutorChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TutorChatSessionRepository extends JpaRepository<TutorChatSession, Long> {

    // Находим последнюю (самую новую) сессию по секции
    Optional<TutorChatSession> findFirstBySectionOrderByCreatedAtDesc(Section section);

    // Находим все сессии по секции (для reset)
    List<TutorChatSession> findAllBySection(Section section);
}
