package com.internship.insighthub.repository;

import com.internship.insighthub.model.ChatMessage;
import com.internship.insighthub.model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


    List<ChatMessage> findBySessionOrderByCreatedAtAsc(ChatSession session);
}
