package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {

  UserDTO findByNickname(String nickname);

  UserDTO findByEmail(String email);
  void save(UserDTO user);

  void updateRole(UserDTO user);

  UserDTO findById(int id);
}
