package com.example.BorrowBoxBackend.security;

import com.example.BorrowBoxBackend.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")  // Fixed: changed from jwtt.secret to jwt.secret
    private String jwtSecret;

    @Value("${jwt.expiration}")  // Fixed: changed from jwtt.expiration to jwt.expiration
    private int jwtExpirationMs;

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())  // ✅ Now works with getter
                .claim("id", user.getId())        // ✅ Fixed: get the ID, not the whole user
                .claim("role", user.getRole())    // ✅ Fixed: get the role, not the whole user
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))  // ✅ Fixed expiration
                .signWith(key(), SignatureAlgorithm.HS256)  // ✅ Fixed signWith method
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}