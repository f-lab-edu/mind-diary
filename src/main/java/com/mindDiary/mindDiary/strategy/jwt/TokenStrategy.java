package com.mindDiary.mindDiary.strategy.jwt;

import io.jsonwebtoken.Claims;

public interface TokenStrategy {

  String createToken(Claims claims, long validityInSeconds);

  Integer getUserId(String token);

  Integer getUserRole(String token);

  String getUserEmail(String originToken);

  boolean validateToken(String originToken);

  String createRefreshToken(int id);

  String createAccessToken(int id, int role, String email);
}
