package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {

  User findByNickname(String nickname);

  User findByEmail(String email);
  void save(User user);

  void updateRole(User user);

  User findById(int id);
}
