package com.mindDiary.mindDiary.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindDiary.mindDiary.entity.Question;
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
public class QuestionDAO {

  private static final int START_INDEX = 0;
  private static final int END_INDEX = -1;
  private static final String key = "question:diagnosisId:";
  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;

  private String getKey(int diagnosisId) {
    return key + diagnosisId;
  }

  public void save(int diagnosisId, List<Question> questions) {
    String key = getKey(diagnosisId);

    RedisSerializer keySerializer = redisTemplate.getStringSerializer();
    RedisSerializer valueSerializer = redisTemplate.getValueSerializer();

    redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
      questions.forEach(question -> {
        connection.listCommands().rPush(keySerializer.serialize(key),
            valueSerializer.serialize(question));
      });
      return null;
    });

  }

  public List<Question> findAllByDiagnosisIds(List<Integer> diagnosisIds) {

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
        .flatMap((o -> convertQuestions((List<Object>) o).stream()))
        .collect(Collectors.toList());
  }

  public List<Question> convertQuestions(List<Object> objects) {
    return objects.stream()
        .map(o -> objectMapper.convertValue(o, Question.class))
        .collect(Collectors.toList());
  }

  public List<Question> findAllByDiagnosisId(int diagnosisId) {
    String key = getKey(diagnosisId);
    List<Object> objects = redisTemplate.opsForList().range(key, START_INDEX, END_INDEX);
    return convertQuestions(objects);
  }
}
