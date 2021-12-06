package com.mindDiary.mindDiary.service.diary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.entity.Diary;
import com.mindDiary.mindDiary.entity.FeelingStatus;
import com.mindDiary.mindDiary.mapper.DiaryRepository;
import com.mindDiary.mindDiary.service.DiaryServiceImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DiaryServiceTest {

  @InjectMocks
  DiaryServiceImpl diaryService;

  @Mock
  DiaryRepository diaryRepository;


  private Diary createDiary() {
    return Diary.builder()
        .content("content")
        .title("title")
        .createdAt(LocalDateTime.now())
        .feelingStatus(FeelingStatus.GOOD)
        .userId(1)
        .id(1)
        .build();
  }


  @Test
  @DisplayName("유저의 다이어리 목록 조회")
  private void readDiariesSuccess() {

    Diary diary = createDiary();
    int userId = diary.getUserId();
    List<Diary> diaries = new ArrayList<>();
    diaries.add(diary);

    doReturn(diaries)
        .when(diaryRepository)
        .findByUserId(userId);

    List<Diary> results = diaryService.readDiaries(userId);

    assertThat(results.size()).isEqualTo(diaries.size());
    assertThat(results.get(0).getId()).isEqualTo(diaries.get(0).getId());
    assertThat(results.get(0).getContent()).isEqualTo(diaries.get(0).getContent());
    assertThat(results.get(0).getTitle()).isEqualTo(diaries.get(0).getTitle());
    assertThat(results.get(0).getUserId()).isEqualTo(diaries.get(0).getUserId());
    assertThat(results.get(0).getCreatedAt()).isEqualTo(diaries.get(0).getCreatedAt());
    assertThat(results.get(0).getFeelingStatus()).isEqualTo(diaries.get(0).getFeelingStatus());

  }

  @Test
  @DisplayName("다이어리 한개 조회")
  private void readOneDiarySuccess() {

    Diary diary = createDiary();
    int diaryId = diary.getId();

    doReturn(diary)
        .when(diaryRepository)
        .findById(diaryId);

    Diary result = diaryService.readOneDiary(diaryId);

    assertThat(result.getId()).isEqualTo(diary.getId());
    assertThat(result.getContent()).isEqualTo(diary.getContent());
    assertThat(result.getTitle()).isEqualTo(diary.getTitle());
    assertThat(result.getUserId()).isEqualTo(diary.getUserId());
    assertThat(result.getCreatedAt()).isEqualTo(diary.getCreatedAt());
    assertThat(result.getFeelingStatus()).isEqualTo(diary.getFeelingStatus());


  }

  @Test
  @DisplayName("다이어리 생성")
  private void createDiarySuccess() {


    Diary diary = createDiary();

    diaryService.createDiary(diary);

    verify(diaryRepository, times(1))
        .save(argThat(d -> d.getUserId() == diary.getUserId()
            && d.getTitle().equals(diary.getTitle())
            && d.getFeelingStatus().equals(diary.getFeelingStatus())
            && d.getCreatedAt().equals(diary.getCreatedAt())
            && d.getContent().equals(diary.getContent())));
  }


}
