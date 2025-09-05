package com.polarisoffice.secuone.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenService {

  private final Key key;
  private final long ttlMillis;

  public JwtTokenService(
      @Value("${app.jwt.secret:change-this-secret-32bytes-min}") String secret,
      @Value("${app.jwt.ttl-millis:2592000000}") long ttlMillis // 기본 30일
  ) {
    // 최소 32바이트 권장
    byte[] bytes = secret.length() >= 32 ? secret.getBytes(StandardCharsets.UTF_8)
                                         : (secret + "________________________________").substring(0,32)
                                           .getBytes(StandardCharsets.UTF_8);
    this.key = Keys.hmacShaKeyFor(bytes);
    this.ttlMillis = ttlMillis;
  }

  // 토큰 생성: subject=USERNAME, claim: uid/role
  public String createToken(Long userId, String username, String role) {
    long now = System.currentTimeMillis();
    return Jwts.builder()
        .setSubject(username)
        .claim("uid", userId)
        .claim("role", role)
        .setIssuedAt(new Date(now))
        .setExpiration(new Date(now + ttlMillis))
        .signWith(key, Jwts.SIG.HS256)
        .compact();
  }

  // 파싱해서 JwtPrincipal 로 변환
  public Optional<JwtPrincipal> decode(String rawToken) {
    try {
      Jws<Claims> jws = Jwts.parser().verifyWith(key).build().parseSignedClaims(rawToken);
      Claims c = ((Jws) jws).getPayload();
      String username = c.getSubject();
      String role = c.get("role", String.class);
      Long uid = null;
      Object uidObj = c.get("uid");
      if (uidObj instanceof Number n) uid = n.longValue();
      else if (uidObj instanceof String s) uid = Long.parseLong(s);

      if (username == null || role == null) return Optional.empty();
      return Optional.of(new JwtPrincipal(uid, username, role));
    } catch (JwtException | IllegalArgumentException e) {
      return Optional.empty();
    }
  }
}