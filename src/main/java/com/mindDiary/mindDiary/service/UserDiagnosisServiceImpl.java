package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dto.response.ReadDiagnosisResultResponseDTO;
import com.mindDiary.mindDiary.entity.UserDiagnosis;
import com.mindDiary.mindDiary.repository.UserDiagnosisRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDiagnosisServiceImpl implements UserDiagnosisService {

  private final UserDiagnosisRepository userDiagnosisRepository;

  @Override
  public void save(UserDiagnosis userDiagnosis) {
    userDiagnosisRepository.save(userDiagnosis);
  }

  @Override
  public List<ReadDiagnosisResultResponseDTO> readMyDiagnosisResults(int userId) {
    List<UserDiagnosis> userDiagnosis = userDiagnosisRepository.findByUserId(userId);

    List<ReadDiagnosisResultResponseDTO> myDiagnosisResultReponses = new ArrayList<>();
    userDiagnosis.forEach(ud -> myDiagnosisResultReponses.add(
        ReadDiagnosisResultResponseDTO.create(ud)));

    return myDiagnosisResultReponses;
  }
}