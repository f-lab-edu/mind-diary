package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {

  User findByEmail(String email);

  User findByNickname(String nickname);
}
