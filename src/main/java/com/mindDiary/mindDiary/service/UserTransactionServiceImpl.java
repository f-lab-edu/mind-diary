package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.strategy.redis.RedisStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTransactionServiceImpl implements UserTransactionService {

  private final RedisStrategy redisStrategy;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void removeCacheAfterRollback(String emailToken) {
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCompletion(int status) {
            if (status == STATUS_ROLLED_BACK) {
              redisStrategy.deleteValue(emailToken);
            }
          }
        });
  }
}
