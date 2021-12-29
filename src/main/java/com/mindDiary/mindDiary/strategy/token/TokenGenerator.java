package com.mindDiary.mindDiary.strategy.token;

import io.jsonwebtoken.Claims;

public interface TokenGenerator {

  String createToken(Claims claims, long validityInSeconds);

  Integer getUserId(String token);

  String getUserRole(String token);

  String getUserEmail(String originToken);

  boolean validateToken(String originToken);

  String createRefreshToken(int id);

  String createAccessToken(int id, String role, String email);

}
