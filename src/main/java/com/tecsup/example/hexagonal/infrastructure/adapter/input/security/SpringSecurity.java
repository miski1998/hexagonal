package com.tecsup.example.hexagonal.infrastructure.adapter.input.security;


import com.tecsup.example.hexagonal.infrastructure.adapter.output.security.AuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurity {

    private  final AuthFilter authFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users").permitAll() // Allow user registration
                        .requestMatchers("/api/users/search/lastname").hasRole("ADMIN")
                        .requestMatchers("/api/users/search/dni").hasRole("ADMIN")
                        .requestMatchers("/api/users/search/age").hasRole("MONITOR")
                        .requestMatchers("/api/users/**").hasAnyRole("USER","ADMIN","MONITOR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) ;
        return http.build();
    }
}