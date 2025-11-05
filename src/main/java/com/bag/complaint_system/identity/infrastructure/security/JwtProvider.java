package com.bag.complaint_system.identity.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private long jwtExpirationMs;

  public String generateToken(Long userId, String email, String role) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

    SecretKey key = getSecretKey();

    return Jwts.builder()
        .subject(userId.toString())
        .claim("email", email)
        .claim("role", role)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(key, Jwts.SIG.HS256)
        .compact();
  }

  public Long getUserIdFromJwt(String token) {
    SecretKey key = getSecretKey();
    Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    return Long.parseLong(claims.getSubject());
  }

  public boolean validateToken(String token) {
    try {
      SecretKey key = getSecretKey();
      Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    }
    return false;
  }

  private SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }
}
