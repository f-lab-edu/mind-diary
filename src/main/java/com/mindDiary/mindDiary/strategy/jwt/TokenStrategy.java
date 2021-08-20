package com.mindDiary.mindDiary.strategy.jwt;

import com.mindDiary.mindDiary.domain.User;
public interface TokenStrategy {

  String createToken(int id, int role, String email, long validityInSeconds);

  String createAccessToken(int id, int role, String email);

  String createRefreshToken(int id, int role, String email);

  Integer getUserId(String token);

  Integer getUserRole(String token);

  String getUserEmail(String originToken);

  User getUserByToken(String originToken);

  boolean validateToken(String originToken);
}
