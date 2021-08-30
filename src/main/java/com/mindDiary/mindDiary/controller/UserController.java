package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.dto.TokenDTO;
import com.mindDiary.mindDiary.dto.UserDTO;
import com.mindDiary.mindDiary.service.UserService;
import com.mindDiary.mindDiary.strategy.cookie.CookieStrategy;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

  private final UserService userService;
  private final CookieStrategy cookieStrategy;


  @PostMapping("/join")
  public ResponseEntity join(@RequestBody @Valid UserDTO userDTO) {
    userService.join(userDTO);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping("/check-email-token")
  public ResponseEntity checkEmailToken(@RequestParam(value = "token") String token,
      @RequestParam(value = "email") String email) {
    userService.checkEmailToken(token, email);

    return new ResponseEntity(HttpStatus.OK);
  }


  @PostMapping("/login")
  public ResponseEntity login(@RequestBody @Valid UserDTO userDTO,
      HttpServletResponse httpServletResponse) {

    TokenDTO tokenDTO = userService.login(userDTO);

    Cookie cookie = tokenDTO.createRefreshTokenCookie(cookieStrategy);
    httpServletResponse.addCookie(cookie);

    return new ResponseEntity(tokenDTO.createAccessTokenDTO(), HttpStatus.OK);
  }


  @PostMapping("/refresh")
  public ResponseEntity updateToken(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse) {

    Cookie cookie = cookieStrategy.getRefreshTokenCookie(httpServletRequest);
    String refreshTokenTakenFromCookie = cookie.getValue();

    TokenDTO tokenDTO = userService.refresh(refreshTokenTakenFromCookie);

    cookieStrategy.deleteRefreshTokenCookie(httpServletRequest, httpServletResponse);

    Cookie newCookie = tokenDTO.createRefreshTokenCookie(cookieStrategy);
    httpServletResponse.addCookie(newCookie);

    return new ResponseEntity(tokenDTO.createAccessTokenDTO(), HttpStatus.OK);
  }
}
