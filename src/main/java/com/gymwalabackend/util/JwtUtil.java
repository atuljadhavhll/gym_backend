package com.gymwalabackend.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
//    private String secret = "MeG8P9cjmDkmeNTcJGgzAtiv/uZoTaehJ7dguz0PiNg=";
    @Value("${app.jwt.secret}")
    private String secret;
    private final long expirationMs = 86400000; // 1 day

    private Key getSigningKey() {
        return new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(Map<String, Object> claims) {
        try {
            if (claims == null || claims.isEmpty()) {
                logger.error("Claims map is null or empty");
                throw new IllegalArgumentException("Claims cannot be null or empty");
            }
            
            // Extract email - required for JWT subject
            Object emailObj = claims.get("email");
            String email = emailObj != null ? emailObj.toString() : null;
            
            if (email == null || email.isEmpty()) {
                logger.error("Email claim is missing or empty");
                throw new IllegalArgumentException("Email is required in claims");
            }
            
            logger.debug("Generating JWT token for email: {}", email);
            
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
            
            logger.info("JWT token generated successfully");
            return token;
            
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument in token generation: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Error generating JWT token", e);
            logger.error("Exception type: {}", e.getClass().getName());
            logger.error("Exception message: {}", e.getMessage());
            throw new RuntimeException("Failed to generate JWT token: " + e.getMessage(), e);
        }
    }
}