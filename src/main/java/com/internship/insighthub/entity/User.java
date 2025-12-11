package com.internship.insighthub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Email пользователя, должен быть уникальным
    @Column(nullable = false, unique = true)
    private String email;

    // Захешенный пароль
    @Column(nullable = false)
    private String password;

    // Отображаемое имя пользователя / username
    @Column(nullable = false)
    private String username;

    // 🔹 Много-ко-многим: пользователь подписан на несколько курсов
    @ManyToMany
    @JoinTable(
            name = "user_courses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> enrolledCourses = new HashSet<>();

    // 🔹 Один пользователь -> много chat-сессий (если у тебя есть ChatSession)
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ChatSession> chatSessions = new ArrayList<>();

    // Удобный helper-метод для добавления сессий
    public void addChatSession(ChatSession session) {
        chatSessions.add(session);
        session.setUser(this);
    }

    public void removeChatSession(ChatSession session) {
        chatSessions.remove(session);
        session.setUser(null);
    }
}
