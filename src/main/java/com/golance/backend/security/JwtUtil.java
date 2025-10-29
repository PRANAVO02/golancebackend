package com.golance.backend.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET_KEY_STRING = "a1b2c3d4e5f607189abcdeff00112233445566778899aabbccddeeff12345678";

    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());


    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000  * 60 * 60))
                .signWith(SECRET_KEY, Jwts.SIG.HS256)
                .compact();	}

    public boolean validateToken(String token, UserDetails userDetails) {
        String extractedUname = extractUsername(token);
        return extractedUname.equals(userDetails.getUsername());
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();}

}