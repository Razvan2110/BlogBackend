//CREATED AT:8/18/2025
//BY: UNGUREANU RAZVAN
//CLASS FOR USER S ROLE BASED ACTIONS



package com.example.blog.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // ✅ CORS activat în 6.x
                .authorizeHttpRequests(auth -> auth
                        // login, register
                        .requestMatchers("/api/auth/**").permitAll()

                        // user management doar ADMIN
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // GET posts pentru toată lumea
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()

                        // CRUD posts doar ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/posts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/posts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/**").hasRole("ADMIN")


                        // CRUD homepagePost
                        .requestMatchers(HttpMethod.GET, "/api/homepage/**").permitAll() // toată lumea poate citi
                        .requestMatchers("/api/homepage/**").hasRole("ADMIN")           // doar adminul poate crea/update/delete

                        // orice altceva → login necesar
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

