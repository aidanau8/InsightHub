package com.internship.insighthub.repository;

import com.internship.insighthub.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // вся история по одной сессии, по дате
    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(Long sessionId);
}

