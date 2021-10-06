package com.mindDiary.mindDiary.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.PageCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DiagnosisDAO {

  private static final String HASH_KEY = "diagnosis:hash";
  private static final String ZSET_KEY = "diagnosis:zset";
  private static final String FIELD_KEY = "diagnosisId:";
  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;


  private String getFieldKey(int diagnosisId) {
    return FIELD_KEY + diagnosisId;
  }


  public void saveAll(List<Diagnosis> diagnoses) {

    RedisSerializer keySerializer = redisTemplate.getKeySerializer();
    RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
    RedisSerializer hashKeySerializer = redisTemplate.getHashKeySerializer();
    RedisSerializer hashValueSerializer = redisTemplate.getHashValueSerializer();

    redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
      diagnoses.forEach(diagnosis -> {
        String fieldKey = getFieldKey(diagnosis.getId());

        connection.hashCommands().hSet(keySerializer.serialize(HASH_KEY),
            hashKeySerializer.serialize(fieldKey),
            hashValueSerializer.serialize(diagnosis));


        connection.zSetCommands().zAdd(keySerializer.serialize(ZSET_KEY),
            diagnosis.getId(),
            valueSerializer.serialize(fieldKey));
      });
      return null;
    });

  }

  public void save(Diagnosis diagnosis) {

    String fieldKey = getFieldKey(diagnosis.getId());
    redisTemplate.opsForHash().put(HASH_KEY, fieldKey, diagnosis);
    redisTemplate.opsForZSet().add(ZSET_KEY, fieldKey, diagnosis.getId());
  }



  public List<Diagnosis> findAll(PageCriteria pageCriteria) {

    int start = pageCriteria.getPageStart();
    int end = start + pageCriteria.getPerPageNum();

    Set<Object> zsetValues = redisTemplate.opsForZSet().range(ZSET_KEY, start, end);

    List<Diagnosis> diagnoses = new ArrayList<>();
    for (Object zsetValue : zsetValues) {
      Object result = redisTemplate.opsForHash().get(HASH_KEY, zsetValue);
      diagnoses.add(objectMapper.convertValue(result, Diagnosis.class));
    }

    return diagnoses;
  }


  public Diagnosis findById(int diagnosisId) {
    String hashKey = getFieldKey(diagnosisId);
    Object object = redisTemplate.opsForHash().get(HASH_KEY, hashKey);
    return objectMapper.convertValue(object, Diagnosis.class);
  }

}
