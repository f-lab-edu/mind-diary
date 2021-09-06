package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.dto.request.JoinUserRequestDTO;
import com.mindDiary.mindDiary.dto.request.LoginUserRequestDTO;
import com.mindDiary.mindDiary.dto.response.AccessTokenResponseDTO;
import com.mindDiary.mindDiary.entity.Token;
import com.mindDiary.mindDiary.entity.User;
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
  public ResponseEntity join(@RequestBody @Valid JoinUserRequestDTO joinUserRequestDTO) {
    User user = joinUserRequestDTO.turnIntoUserEntity();
    userService.join(user);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping("/check-email-token")
  public ResponseEntity checkEmailToken(@RequestParam(value = "token") String token,
      @RequestParam(value = "email") String email) {
    userService.checkEmailToken(token, email);
    return new ResponseEntity(HttpStatus.OK);
  }


  @PostMapping("/login")
  public ResponseEntity login(@RequestBody @Valid LoginUserRequestDTO loginUserRequestDTO,
      HttpServletResponse httpServletResponse) {

    User user = loginUserRequestDTO.turnIntoUserEntity();
    Token token = userService.login(user);
    Cookie cookie = token.turnRefreshTokenInfoCookie(cookieStrategy);
    httpServletResponse.addCookie(cookie);

    AccessTokenResponseDTO accessTokenResponseDTO = token.turnIntoAccessTokenResponseDTO();

    return new ResponseEntity(accessTokenResponseDTO, HttpStatus.OK);
  }


  @PostMapping("/refresh")
  public ResponseEntity updateToken(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse) {

    Cookie cookie = cookieStrategy.getRefreshTokenCookie(httpServletRequest);
    String refreshTokenTakenFromCookie = cookie.getValue();

    Token token = userService.refresh(refreshTokenTakenFromCookie);

    cookieStrategy.deleteRefreshTokenCookie(httpServletRequest, httpServletResponse);
    Cookie newCookie = token.turnRefreshTokenInfoCookie(cookieStrategy);
    httpServletResponse.addCookie(newCookie);

    AccessTokenResponseDTO accessTokenResponseDTO = token.turnIntoAccessTokenResponseDTO();

    return new ResponseEntity(accessTokenResponseDTO, HttpStatus.OK);

  }

}
