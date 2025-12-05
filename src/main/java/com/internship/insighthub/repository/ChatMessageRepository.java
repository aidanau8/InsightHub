package com.internship.insighthub.repository;

import com.internship.insighthub.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 🔹 Этот метод мы добавляем
    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(Long sessionId);
}
