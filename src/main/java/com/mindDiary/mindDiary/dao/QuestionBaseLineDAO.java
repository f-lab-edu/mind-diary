package com.mindDiary.mindDiary.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QuestionBaseLineDAO {

  private static final int START_INDEX = 0;
  private static final int END_INDEX = -1;
  private static final String key = "questionBaseLine:diagnosisId:";
  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;

  private String getKey(int diagnosisId) {
    return key + diagnosisId;
  }

  public void save(int diagnosisId, List<QuestionBaseLine> questionBaseLines) {
    String key = getKey(diagnosisId);

    RedisSerializer keySerializer = redisTemplate.getStringSerializer();
    RedisSerializer valueSerializer = redisTemplate.getValueSerializer();

    redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
      questionBaseLines.forEach(baseLine -> {
        connection.listCommands().rPush(keySerializer.serialize(key),
            valueSerializer.serialize(baseLine));
      });
      return null;
    });

  }

  public List<QuestionBaseLine> findAllByDiagnosisIds(List<Integer> diagnosisIds) {

    RedisSerializer keySerializer = redisTemplate.getStringSerializer();

    List<Object> objects = redisTemplate.executePipelined(
        new RedisCallback<Object>() {
          public Object doInRedis(RedisConnection connection) throws DataAccessException {
            diagnosisIds.forEach(diagnosisId -> {
              String key = getKey(diagnosisId);

              connection.listCommands()
                  .lRange(keySerializer.serialize(key), START_INDEX, END_INDEX);
            });

            return null;
          }
        });

    return objects.stream()
        .flatMap((o -> convertQuestionBaseLines((List<Object>) o).stream()))
        .collect(Collectors.toList());
  }

  public List<QuestionBaseLine> convertQuestionBaseLines(List<Object> objects) {
    return objects.stream()
        .map(o -> objectMapper.convertValue(o, QuestionBaseLine.class))
        .collect(Collectors.toList());
  }

  public List<QuestionBaseLine> findByDiagnosisId(int diagnosisId) {
    String key = getKey(diagnosisId);
    List<Object> objects = redisTemplate.opsForList().range(key, START_INDEX, END_INDEX);
    return convertQuestionBaseLines(objects);
  }
}
