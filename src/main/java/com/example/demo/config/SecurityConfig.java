package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API development
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/**").permitAll() // Allow all API endpoints
                .requestMatchers("/swagger-ui/**").permitAll() // Allow Swagger UI
                .requestMatchers("/swagger-ui.html").permitAll() // Allow Swagger UI HTML
                .requestMatchers("/v3/api-docs/**").permitAll() // Allow OpenAPI docs
                .requestMatchers("/api-docs/**").permitAll() // Allow API docs
                .anyRequest().permitAll() // Allow all other requests for development
            );
        
        return http.build();
    }
}