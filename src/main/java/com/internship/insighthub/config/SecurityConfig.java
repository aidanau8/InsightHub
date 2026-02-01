package com.internship.insighthub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // На время стажировки просто всё упрощаем:
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Разрешаем главную страницу и статику
                        .requestMatchers(
                                "/", "/index", "/register",
                                "/css/**", "/js/**", "/images/**", "/webjars/**"
                        ).permitAll()

                        // Разрешаем весь наш API (для курсов, секций, чата)
                        .requestMatchers("/api/**").permitAll()

                        // Остальное тоже не блокируем (можно позже ужесточить)
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
