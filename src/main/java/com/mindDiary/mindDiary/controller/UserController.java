package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponseEntity join(@RequestBody @Valid User user) {

    if (userService.isDuplicate(user)) {
      return new ResponseEntity(HttpStatus.CONFLICT);
    }

    userService.join(user);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping("/check-email-token")
  public ResponseEntity checkEmailToken() {
    return new ResponseEntity(HttpStatus.OK);
  }

}
