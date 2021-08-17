package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public boolean isDuplicate(UserJoinRequestDTO userJoinRequestDTO) {

    if (isDuplicateEmail(userJoinRequestDTO.getEmail())
        || isDuplicateNickname(userJoinRequestDTO.getNickname())) {
      return true;
    }

    return false;
  }

  @Override
  public void join(UserJoinRequestDTO userJoinRequestDTO) {


    //해싱된 패스워드 생성

    ////DB에 기본 정보 & 인증키를 저장
    //메일 링크 클릭 시 파라미터로 전달받은 데이터 (emaill, authkey)가 일치할 경우 authstatus를 1로 업데이

  }

  private boolean isDuplicateEmail(String email) {
    return userRepository.findByEmail(email) != null;
  }

  private boolean isDuplicateNickname(String nickname) {
    return userRepository.findByNickname(nickname) != null;
  }
}
