package com.mindDiary.mindDiary.strategy.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtStrategy {

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

  public String createToken(int id, int role, String email, long validityInSeconds) {
    Claims claims = Jwts.claims();
    claims.put("userId", id);
    claims.put("userRole", role);
    claims.put("userEmail", email);

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

  public String createAccessToken(int id, int role, String email) {
    return createToken(id, role, email, accessTokenValidityInSeconds);
  }

  public String createRefreshToken(int id, int role, String email) {
    return createToken(id, role, email, refreshTokenValidityInSeconds);
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

  public String getUserEmail(String token) {
    return extractAllClaims(token).get("userEmail",String.class);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .setSigningKey(getSigningKey(secret))
          .parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      //잘못된 JWT 서명
      return false;
    } catch (ExpiredJwtException e) {
      //만료된 JWT 토큰
      return false;
    } catch (UnsupportedJwtException e) {
      //지원되지 않는 JWT 토큰
      return false;
    } catch (IllegalArgumentException e) {
      //JWT 토큰이 잘못되었습니다.
      return false;
    }
  }
}
