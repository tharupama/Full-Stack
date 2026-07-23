package com.OnlineBookService.order_service.configuration;

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

                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        // Matches exactly what the Gateway is sending: "ROLE_Admin"
                        .requestMatchers("/api/v1/notification-controller/**","/api/v1/order-controller/updateStatus","/api/v1/order-controller/get-orders","/api/v1/order-controller/delete/{id}","/api/v1/order-controller/get-order-customer-details")
                        .hasRole("Admin")//has role add automaticall ROLE_ prefix to the role name
                        .requestMatchers("/api/v1/order-controller/webhook").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())

                // ⚠️ THIS LINE IS MANDATORY. Without it, the filter never runs!
                .addFilterBefore(gatewayTrustFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}