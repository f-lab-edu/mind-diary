package com.mindDiary.mindDiary.strategy;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.access-token-validity-in-seconds}")
  private long accessTokenValidityInSeconds;

  @Value("${jwt.refresh-token-validity-in-seconds}")
  private long refreshTokenValidityInSeconds;


  private Key getSigningKey(String secretKey) {
    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String createToken(int id, int role, long validityInSeconds) {
    Claims claims = Jwts.claims();
    claims.put("userId", id);
    claims.put("userRole", role);

    long now = System.currentTimeMillis();
    Date nowDate = new Date(now);
    Date validity = new Date(now + (validityInSeconds * 1000));

    String jwt = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(nowDate)
        .setExpiration(validity)
        .signWith(getSigningKey(secret), SignatureAlgorithm.HS256)
        .compact();

    return jwt;
  }

  public String createAccessToken(int id, int role) {
    return createToken(id, role, accessTokenValidityInSeconds);
  }

  public String createRefreshToken(int id, int role) {
    return createToken(id, role, refreshTokenValidityInSeconds);
  }

  public Claims extractAllClaims(String token) {
    return Jwts.parser()
        .setSigningKey(getSigningKey(secret))
        .parseClaimsJws(token)
        .getBody();
  }

  public Integer getUserId(String token) {
    return extractAllClaims(token).get("userId",Integer.class);
  }

  public Integer getUserRole(String token) {
    return extractAllClaims(token).get("userRole",Integer.class);
  }

}
