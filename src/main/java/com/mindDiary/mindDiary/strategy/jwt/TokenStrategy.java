package com.mindDiary.mindDiary.strategy.jwt;

import io.jsonwebtoken.Claims;
import java.security.Key;

public interface TokenStrategy {

  Key getSigningKey(String secretKey);

  String createToken(int id, int role, long validityInSeconds);

  String createAccessToken(int id, int role);

  String createRefreshToken(int id, int role);

  Claims extractAllClaims(String token);

  Integer getUserId(String token);

  Integer getUserRole(String token);
}
