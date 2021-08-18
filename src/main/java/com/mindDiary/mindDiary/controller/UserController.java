package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.dto.request.UserLoginRequestDTO;
import com.mindDiary.mindDiary.dto.response.UserLoginResponseDTO;
import com.mindDiary.mindDiary.service.UserService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

  private final UserService userService;

  @PostMapping("/join")
  public ResponseEntity join(@RequestBody @Valid User user) {

    if (userService.isNicknameDuplicate(user.getNickname())
        || userService.isEmailDuplicate(user.getEmail())) {
      return new ResponseEntity(HttpStatus.CONFLICT);
    }

    userService.join(user);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping("/check-email-token")
  public ResponseEntity checkEmailToken(@RequestParam(value = "token") String token,
      @RequestParam(value = "email") String email) {
    boolean check = userService.checkEmailToken(token,email);
    if (!check) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity(HttpStatus.OK);
  }

  @PostMapping("/auth/login")
  public ResponseEntity login(@RequestBody @Valid UserLoginRequestDTO userLoginRequestDTO,
      HttpServletResponse httpServletResponse) {

    User user = userService.findByEmail(userLoginRequestDTO.getEmail());
    if (user == null) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    if (!userService.passwordMatches(userLoginRequestDTO.getPassword(), user.getPassword())) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    UserLoginResponseDTO userLoginResponseDTO = userService.login(user.getId(), user.getRole());

    Cookie cookieWithRefreshToken = new Cookie("refreshToken",userLoginResponseDTO.getRefreshToken());
    httpServletResponse.addCookie(cookieWithRefreshToken);
    return new ResponseEntity(userLoginResponseDTO.getAccessToken(), HttpStatus.OK);
  }

}
