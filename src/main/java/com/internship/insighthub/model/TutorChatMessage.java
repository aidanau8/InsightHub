package com.internship.insighthub.model;

import com.internship.insighthub.entity.TutorChatSession;
import jakarta.persistence.*;
        import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "tutor_chat_messages")
@Getter
@Setter
@NoArgsConstructor
public class TutorChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private TutorChatSession session;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TutorMessageRole role;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public TutorChatMessage(TutorChatSession session, TutorMessageRole role, String content) {
        this.session = session;
        this.role = role;
        this.content = content;
    }
}
