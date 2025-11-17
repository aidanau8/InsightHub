package com.internship.insighthub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // красивое имя для интерфейса
    private String username;

    // логин — по email
    @Column(nullable = false, unique = true)
    private String email;

    // храним ТОЛЬКО ХЭШ
    @Column(nullable = false)
    private String passwordHash;
}
