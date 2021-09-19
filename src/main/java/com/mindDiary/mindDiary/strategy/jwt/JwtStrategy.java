package com.mindDiary.mindDiary.strategy.jwt;

import com.mindDiary.mindDiary.exception.InvalidJwtException;
import com.mindDiary.mindDiary.exception.businessException.BusinessException;
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
public class JwtStrategy implements TokenStrategy {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.access-token-validity-in-seconds}")
  private long accessTokenValidityInSeconds;

  @Value("${jwt.refresh-token-validity-in-seconds}")
  private long refreshTokenValidityInSeconds;

  private static final String USER_ID = "userId";
  private static final String USER_ROLE = "userRole";
  private static final String USER_EMAIL = "userEmail";
  private static final String INVALID_VALUE_EXCEPTION = "토큰을 발급할 수 없습니다";


  private Key getSigningKey(String secretKey) {
    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }


  @Override
  public String createAccessToken(int id, String role, String email) {

    Claims claims = Jwts.claims();
    claims.put(USER_ID, id);
    claims.put(USER_ROLE, role);
    claims.put(USER_EMAIL, email);

    return createToken(claims, accessTokenValidityInSeconds);
  }

  @Override
  public String createRefreshToken(int id) {

    Claims claims = Jwts.claims();
    claims.put(USER_ID, id);

    return createToken(claims, refreshTokenValidityInSeconds);
  }

  public String createToken(Claims claims, long validityInSeconds) {

    validateClaims(claims);
    validateSeconds(validityInSeconds);

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

  private void validateSeconds(long validityInSeconds) {
    if (validityInSeconds <= 0) {
      throw new InvalidJwtException(INVALID_VALUE_EXCEPTION);
    }
  }

  private void validateClaims(Claims claims) {
    if (claims == null || claims.isEmpty() || claims.size() == 0) {
      throw new InvalidJwtException(INVALID_VALUE_EXCEPTION);
    }
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .setSigningKey(getSigningKey(secret))
        .parseClaimsJws(token)
        .getBody();
  }

  public Integer getUserId(String token) {
    return extractAllClaims(token).get(USER_ID, Integer.class);
  }

  public String getUserRole(String token) {
    return extractAllClaims(token).get(USER_ROLE, String.class);
  }

  public String getUserEmail(String token) {
    return extractAllClaims(token).get(USER_EMAIL, String.class);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .setSigningKey(getSigningKey(secret))
          .parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      //잘못된 JWT 서명
      throw new InvalidJwtException(e);
    } catch (ExpiredJwtException e) {
      //만료된 JWT 토큰
      throw new InvalidJwtException(e);
    } catch (UnsupportedJwtException e) {
      //지원되지 않는 JWT 토큰
      throw new InvalidJwtException(e);
    } catch (IllegalArgumentException e) {
      //JWT 토큰이 잘못되었습니다.
      throw new InvalidJwtException(e);
    }
  }
}
