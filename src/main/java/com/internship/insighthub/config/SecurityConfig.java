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

                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Разрешаем главную страницу и статику
                        .requestMatchers(
                                "/", "/index", "/register",
                                "/css/**", "/js/**", "/images/**", "/webjars/**"
                        ).permitAll()


                        .requestMatchers("/api/**").permitAll()


                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
