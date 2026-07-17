package com.OnlineBookStore.ApiGateway.configuration; // Adjust package for Gateway

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "this-is-a-secret-key-must-be-at-least-32-characters-long!";
    private static final long TOKEN_VALIDITY = 3600 * 5;

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T getClaimFromToken(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    // 🆕 ADD THIS NEW METHOD for API Gateway (no database needed!)
    public boolean validateTokenOnly(String token) {
        return !isTokenExpired(token);
        // The signature is already verified when we call getAllClaimsFromToken()
        // If the signature is invalid, it throws an exception before reaching here
    }

    private boolean isTokenExpired(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        final Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

}