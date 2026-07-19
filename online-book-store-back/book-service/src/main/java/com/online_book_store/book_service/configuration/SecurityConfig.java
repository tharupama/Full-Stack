package com.online_book_store.book_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final GatewayTrustFilter gatewayTrustFilter;

    public SecurityConfig(GatewayTrustFilter gatewayTrustFilter) {
        this.gatewayTrustFilter = gatewayTrustFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/book-controller/get-by-page", "/api/v1/book-controller/get-by-page-and-title","/api/v1/book-controller/update-quantity").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        // Matches exactly what the Gateway is sending: "ROLE_Admin"
                        .requestMatchers("/api/v1/book-controller/save", "/api/v1/book-controller/update", "/api/v1/book-controller/delete")
                        .hasRole("Admin")//has role add automaticall ROLE_ prefix to the role name

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())

                // ⚠️ THIS LINE IS MANDATORY. Without it, the filter never runs!
                .addFilterBefore(gatewayTrustFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}