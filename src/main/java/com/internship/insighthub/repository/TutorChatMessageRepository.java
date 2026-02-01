package com.internship.insighthub.repository;

import com.internship.insighthub.entity.TutorChatSession;
import com.internship.insighthub.model.TutorChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutorChatMessageRepository extends JpaRepository<TutorChatMessage, Long> {

    List<TutorChatMessage> findBySessionOrderByCreatedAtAsc(TutorChatSession session);

    void deleteBySession(TutorChatSession session);
}
