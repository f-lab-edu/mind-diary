package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.dto.request.UserLoginRequestDTO;
import com.mindDiary.mindDiary.dto.response.TokenResponseDTO;
import com.mindDiary.mindDiary.service.UserService;
import com.mindDiary.mindDiary.strategy.cookie.CookieStrategy;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
  private final CookieStrategy cookieStrategy;

  @Value("${cookie.key.refresh-token}")
  private String cookieRefreshTokenKey;


  @PostMapping("/join")
  public ResponseEntity join(@RequestBody @Valid UserJoinRequestDTO userJoinRequestDTO) {
    boolean flag = userService.join(userJoinRequestDTO);
    if (!flag) {
      return new ResponseEntity(HttpStatus.CONFLICT);
    }
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

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody @Valid UserLoginRequestDTO userLoginRequestDTO,
      HttpServletResponse httpServletResponse) {

    TokenResponseDTO tokenResponseDTO = userService.login(userLoginRequestDTO);

    if (tokenResponseDTO == null) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    Cookie cookie = tokenResponseDTO.createTokenCookie(cookieStrategy, cookieRefreshTokenKey);
    httpServletResponse.addCookie(cookie);

    return new ResponseEntity(tokenResponseDTO.createAccessTokenResponseDTO(), HttpStatus.OK);
  }

  @PostMapping("/refresh")
  public ResponseEntity updateToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    Cookie cookie = cookieStrategy.getCookie(cookieRefreshTokenKey, httpServletRequest);
    String refreshTokenTakenFromCookie = cookie.getValue();

    TokenResponseDTO tokenResponseDTO = userService.refresh(refreshTokenTakenFromCookie);
    if (tokenResponseDTO == null) {
      return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    cookieStrategy.deleteCookie(cookieRefreshTokenKey, httpServletRequest, httpServletResponse);

    Cookie newCookie = tokenResponseDTO.createTokenCookie(cookieStrategy, cookieRefreshTokenKey);
    httpServletResponse.addCookie(newCookie);

    return new ResponseEntity(tokenResponseDTO.createAccessTokenResponseDTO(), HttpStatus.OK);
  }

}
