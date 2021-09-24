package com.mindDiary.mindDiary.service;

public interface UserTransactionService {
  void removeCacheAfterRollback(String emailToken);
}
