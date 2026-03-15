package com.internship.insighthub.repository;

import com.internship.insighthub.entity.Section;
import com.internship.insighthub.entity.TutorChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TutorChatSessionRepository extends JpaRepository<TutorChatSession, Long> {


    Optional<TutorChatSession> findFirstBySectionOrderByCreatedAtDesc(Section section);


    List<TutorChatSession> findAllBySection(Section section);
}
