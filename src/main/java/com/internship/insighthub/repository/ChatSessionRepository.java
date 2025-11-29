package com.internship.insighthub.repository;

import com.internship.insighthub.model.ChatSession;
import com.internship.insighthub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {


    List<ChatSession> findByUserOrderByCreatedAtDesc(User user);
}
