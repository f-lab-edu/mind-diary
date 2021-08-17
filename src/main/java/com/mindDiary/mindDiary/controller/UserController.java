package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
  private final UserService userService;

  @PostMapping("/join")
  public ResponseEntity join(@RequestBody @Valid UserJoinRequestDTO userJoinRequestDTO) {

    if (userService.isDuplicate(userJoinRequestDTO)) {
      return new ResponseEntity(HttpStatus.CONFLICT);
    }


//    userService.join(userJoinRequestDTO);

    return new ResponseEntity(HttpStatus.OK);
  }
}
