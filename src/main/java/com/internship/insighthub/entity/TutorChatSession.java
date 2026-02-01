package com.internship.insighthub.entity;

import com.internship.insighthub.model.TutorChatMessage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tutor_chat_sessions")
@Getter
@Setter
@NoArgsConstructor
public class TutorChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // К какой секции относится этот чат
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    // Когда создана сессия
    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    // Флаг активности (на будущее)
    @Column(nullable = false)
    private boolean active = true;

    // Сообщения внутри сессии
    @OneToMany(mappedBy = "session",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TutorChatMessage> messages = new ArrayList<>();
}
