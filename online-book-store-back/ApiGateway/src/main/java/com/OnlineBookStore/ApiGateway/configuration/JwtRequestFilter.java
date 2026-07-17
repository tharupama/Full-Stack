package com.OnlineBookStore.ApiGateway.configuration;


import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                Claims claims = jwtUtil.getAllClaimsFromToken(jwtToken);
                username = claims.getSubject();

                if (jwtUtil.validateTokenOnly(jwtToken)) {
                    List<?> rolesList = claims.get("roles", List.class);
                    final String roles;
                    if (rolesList != null) {
                        roles = rolesList.stream().map(Object::toString).collect(Collectors.joining(","));
                    } else {
                        roles = "";
                    }

                    System.out.println("🎟️ [GATEWAY] Token valid! Username: " + username + ", Roles: " + roles);

                    final String finalUsername = username;
                    final String finalRoles = roles;

                    // WRAP THE REQUEST TO ADD HEADERS
                    HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request) {
                        @Override
                        public String getHeader(String name) {
                            if ("X-User-Name".equalsIgnoreCase(name)) return finalUsername;
                            if ("X-User-Roles".equalsIgnoreCase(name)) return finalRoles;
                            return super.getHeader(name);
                        }

                        @Override
                        public Enumeration<String> getHeaders(String name) {
                            if ("X-User-Name".equalsIgnoreCase(name)) return Collections.enumeration(List.of(finalUsername));
                            if ("X-User-Roles".equalsIgnoreCase(name)) return Collections.enumeration(List.of(finalRoles));
                            return super.getHeaders(name);
                        }

                        // 🆕 THIS IS THE MISSING PIECE THAT FIXES THE NULL HEADERS!
                        @Override
                        public Enumeration<String> getHeaderNames() {
                            List<String> names = Collections.list(super.getHeaderNames());
                            if (!names.contains("X-User-Name")) names.add("X-User-Name");
                            if (!names.contains("X-User-Roles")) names.add("X-User-Roles");
                            return Collections.enumeration(names);
                        }
                    };

                    filterChain.doFilter(wrappedRequest, response);
                    return;
                }
            } catch (Exception e) {
                System.out.println("❌ [GATEWAY] Invalid Token: " + e.getMessage());
            }
        }

        System.out.println("⚠️ [GATEWAY] No valid token, passing request without wristband");
        filterChain.doFilter(request, response);
    }
}