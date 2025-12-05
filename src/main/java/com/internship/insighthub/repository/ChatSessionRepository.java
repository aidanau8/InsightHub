package com.internship.insighthub.repository;

import com.internship.insighthub.entity.ChatSession;
import com.internship.insighthub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;   // <-- ВАЖНЫЙ импорт

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {


    List<ChatSession> findByUserOrderByUpdatedAtDesc(User user);


    Optional<ChatSession> findByIdAndUserEmail(Long id, String email);
}

