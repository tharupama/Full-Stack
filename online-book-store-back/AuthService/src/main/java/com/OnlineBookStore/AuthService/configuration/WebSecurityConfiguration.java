package com.OnlineBookStore.AuthService.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private final GatewayTrustFilter gatewayTrustFilter;

    // Inject the filter
    public WebSecurityConfiguration(GatewayTrustFilter gatewayTrustFilter) {
        this.gatewayTrustFilter = gatewayTrustFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (Gateway will pass these without headers, filter does nothing, permitAll allows it)
                        .requestMatchers("/login", "/signup", "/init", "/message", "/actuator/busrefresh").permitAll()

                        // Protected endpoints (Gateway passes headers, Filter authenticates, this rule allows it)
                        .requestMatchers("/save-more-details", "/profile").authenticated()

                        // Swagger
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // ⚠️ IMPORTANT: Change this from .permitAll() to .authenticated()
                        // so any new endpoints you add in the future are secure by default.
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // ⚠️ THIS IS THE MISSING LINK: Add the filter before the default auth filter
                .addFilterBefore(gatewayTrustFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}