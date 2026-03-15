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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;


    @Column(nullable = false)
    private Instant createdAt = Instant.now();


    @Column(nullable = false)
    private boolean active = true;


    @OneToMany(mappedBy = "session",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TutorChatMessage> messages = new ArrayList<>();
}
