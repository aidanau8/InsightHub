
package com.internship.insighthub.service;

import com.internship.insighthub.dto.UserDto;
import com.internship.insighthub.dto.UserRegistrationDto;
import com.internship.insighthub.entity.User;
import com.internship.insighthub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // ✅ успешная регистрация
    @Test
    void registerUser_success() {
        UserRegistrationDto dto = new UserRegistrationDto(
                "aidana",
                "aidana@example.com",
                "password123"
        );

        // username и email свободны
        when(userRepository.findByUsername("aidana")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("aidana@example.com")).thenReturn(Optional.empty());

        // сохранение пользователя
        User saved = User.builder()
                .id(1L)
                .username("aidana")
                .email("aidana@example.com")
                .passwordHash("password123")
                .build();
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = userService.registerUser(dto);

        assertNotNull(result.getId());
        assertEquals("aidana", result.getUsername());
        assertEquals("aidana@example.com", result.getEmail());

        verify(userRepository).findByUsername("aidana");
        verify(userRepository).findByEmail("aidana@example.com");
        verify(userRepository).save(any(User.class));
    }

    // ❌ регистрация — username уже занят
    @Test
    void registerUser_usernameExists_shouldThrow() {
        UserRegistrationDto dto = new UserRegistrationDto(
                "aidana",
                "other@example.com",
                "password123"
        );

        when(userRepository.findByUsername("aidana"))
                .thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(dto));

        verify(userRepository).findByUsername("aidana");
        verify(userRepository, never()).save(any());
    }

    // ❌ регистрация — email уже используется
    @Test
    void registerUser_emailExists_shouldThrow() {
        UserRegistrationDto dto = new UserRegistrationDto(
                "newuser",
                "aidana@example.com",
                "password123"
        );

        when(userRepository.findByUsername("newuser"))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail("aidana@example.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(dto));

        verify(userRepository).findByUsername("newuser");
        verify(userRepository).findByEmail("aidana@example.com");
        verify(userRepository, never()).save(any());
    }

    // ✅ findByEmail — пользователь найден
    @Test
    void findByEmail_found() {
        User user = User.builder()
                .id(1L)
                .username("aidana")
                .email("aidana@example.com")
                .passwordHash("hash")
                .build();

        when(userRepository.findByEmail("aidana@example.com"))
                .thenReturn(Optional.of(user));

        User result = userService.findByEmail("aidana@example.com");

        assertEquals("aidana", result.getUsername());
        verify(userRepository).findByEmail("aidana@example.com");
    }

    // ❌ findByEmail — не найден
    @Test
    void findByEmail_notFound_shouldThrow() {
        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.findByEmail("missing@example.com"));
    }

    // ✅ verifyPassword — true
    @Test
    void verifyPassword_shouldReturnTrue() {
        assertTrue(userService.verifyPassword("pass", "pass"));
    }

    // ✅ verifyPassword — false
    @Test
    void verifyPassword_shouldReturnFalse() {
        assertFalse(userService.verifyPassword("pass", "other"));
    }

    // ✅ findByUsername — возвращает корректный DTO
    @Test
    void findByUsername_shouldReturnDto() {
        User user = User.builder()
                .id(10L)
                .username("narynGirl")
                .email("ng@example.com")
                .build();

        when(userRepository.findByUsername(eq("narynGirl")))
                .thenReturn(Optional.of(user));

        UserDto dto = userService.findByUsername("narynGirl");

        assertEquals(10L, dto.getId());
        assertEquals("narynGirl", dto.getUsername());
        assertEquals("ng@example.com", dto.getEmail());

        verify(userRepository).findByUsername("narynGirl");
    }
}
