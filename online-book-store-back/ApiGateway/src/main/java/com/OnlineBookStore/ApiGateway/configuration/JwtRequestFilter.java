package com.OnlineBookStore.ApiGateway.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
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

        // 1. If there is NO Authorization header, allow it to pass (for public routes like /login or /register)
        if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
            System.out.println("⚠️ [GATEWAY] No Bearer token found. Passing request as public/unauthenticated.");
            filterChain.doFilter(request, response);
            return; // Stop here
        }

        // 2. Token is present, let's validate it
        String jwtToken = requestTokenHeader.substring(7);

        try {
            Claims claims = jwtUtil.getAllClaimsFromToken(jwtToken);
            String username = claims.getSubject();

            if (jwtUtil.validateTokenOnly(jwtToken)) {
                List<?> rolesList = claims.get("roles", List.class);
                String roles = (rolesList != null)
                        ? rolesList.stream().map(Object::toString).collect(Collectors.joining(","))
                        : "";

                System.out.println("🎟️ [GATEWAY] Token valid! Username: " + username + ", Roles: " + roles);

                HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request) {
                    @Override
                    public String getHeader(String name) {
                        if ("X-User-Name".equalsIgnoreCase(name)) return username;
                        if ("X-User-Roles".equalsIgnoreCase(name)) return roles;
                        return super.getHeader(name);
                    }

                    @Override
                    public Enumeration<String> getHeaders(String name) {
                        if ("X-User-Name".equalsIgnoreCase(name)) return Collections.enumeration(List.of(username));
                        if ("X-User-Roles".equalsIgnoreCase(name)) return Collections.enumeration(List.of(roles));
                        return super.getHeaders(name);
                    }

                    @Override
                    public Enumeration<String> getHeaderNames() {
                        List<String> names = Collections.list(super.getHeaderNames());
                        if (!names.contains("X-User-Name")) names.add("X-User-Name");
                        if (!names.contains("X-User-Roles")) names.add("X-User-Roles");
                        return Collections.enumeration(names);
                    }
                };

                // Token is valid, proceed to microservice
                filterChain.doFilter(wrappedRequest, response);
                return; // Success! Stop execution here.

            } else {
                // Token parsed, but custom validation (e.g., user disabled) failed
                System.out.println("❌ [GATEWAY] Token failed custom validation.");
                sendErrorResponse(response, "Token is invalid or user is inactive");
                return; // BLOCK the request!
            }

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            System.out.println("❌ [GATEWAY] Token validation failed: " + e.getMessage());
            sendErrorResponse(response, "Invalid or expired token");
            return; // BLOCK the request!

        } catch (Exception e) {
            System.out.println("❌ [GATEWAY] Unexpected error during token validation: " + e.getMessage());
            sendErrorResponse(response, "Authentication failed");
            return; // BLOCK the request!
        }
    }

    /**
     * Helper method to cleanly send a 401 Unauthorized JSON response
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        // ✅ ADD THESE CORS HEADERS
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200"); // Allow your Angular app
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, No-Auth, X-User-Name, X-User-Roles");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}