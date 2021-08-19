package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.dto.request.UserLoginRequestDTO;
import com.mindDiary.mindDiary.dto.response.AccessTokenResponseDTO;
import com.mindDiary.mindDiary.dto.response.TokenResponseDTO;
import com.mindDiary.mindDiary.service.UserService;
import com.mindDiary.mindDiary.strategy.cookie.CookieStrategy;
import com.mindDiary.mindDiary.strategy.cookie.CreateCookieStrategy;
import com.mindDiary.mindDiary.strategy.jwt.JwtStrategy;
import com.mindDiary.mindDiary.strategy.redis.RedisStrategy;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
  private final JwtStrategy jwtStrategy;
  private final CookieStrategy cookieStrategy;
  private final PasswordEncoder passwordEncoder;
  private final RedisStrategy redisStrategy;

  @Value("${jwt.refresh-token-validity-in-seconds}")
  private long refreshTokenValidityInSeconds;

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

    Cookie cookie = tokenResponseDTO.createTokenCookie(new CreateCookieStrategy(), cookieRefreshTokenKey);
    httpServletResponse.addCookie(cookie);

    return new ResponseEntity(tokenResponseDTO.createAccessTokenResponseDTO(), HttpStatus.OK);
  }

  @PostMapping("/refresh")
  public ResponseEntity updateToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    Cookie cookie = cookieStrategy.getCookie("refreshToken", httpServletRequest);
    String refreshTokenTakenFromCookie = cookie.getValue();

    if (!jwtStrategy.validateToken(refreshTokenTakenFromCookie)) {
      return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    String emailTakenFromCache = redisStrategy.getValueData(refreshTokenTakenFromCookie);
    if (!emailTakenFromCache.equals(jwtStrategy.getUserEmail(refreshTokenTakenFromCookie))) {
      return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    int userId = jwtStrategy.getUserId(refreshTokenTakenFromCookie);
    int userRole = jwtStrategy.getUserRole(refreshTokenTakenFromCookie);
    String userEmail = jwtStrategy.getUserEmail(refreshTokenTakenFromCookie);

    String newAccessToken = jwtStrategy.createAccessToken(userId, userRole, userEmail);
    String newRefreshToken = jwtStrategy.createRefreshToken(userId, userRole, userEmail);

    AccessTokenResponseDTO accessTokenResponseDTO = new AccessTokenResponseDTO();
    accessTokenResponseDTO.setAccessToken(newAccessToken);

    Cookie newCookie  = cookieStrategy.createCookie(cookieRefreshTokenKey, newRefreshToken);

    redisStrategy.deleteValue(refreshTokenTakenFromCookie);
    cookieStrategy.deleteCookie(cookieRefreshTokenKey, httpServletRequest, httpServletResponse);
    redisStrategy.setValueExpire(newRefreshToken, userEmail, refreshTokenValidityInSeconds);
    httpServletResponse.addCookie(newCookie);


    return new ResponseEntity(accessTokenResponseDTO, HttpStatus.OK);
  }

}
