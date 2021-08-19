package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.dto.request.UserRefreshRequestDTO;
import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.dto.request.UserLoginRequestDTO;
import com.mindDiary.mindDiary.dto.response.AccessTokenResponseDTO;
import com.mindDiary.mindDiary.service.UserService;
import com.mindDiary.mindDiary.strategy.cookie.CookieStrategy;
import com.mindDiary.mindDiary.strategy.jwt.JwtStrategy;
import com.mindDiary.mindDiary.strategy.redis.RedisStrategy;
import java.util.Arrays;
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


  @PostMapping("/join")
  public ResponseEntity join(@RequestBody @Valid UserJoinRequestDTO userJoinRequestDTO) {

    User nicknameDuplicateUser = userService.findByEmail(userJoinRequestDTO.getEmail());
    if (nicknameDuplicateUser != null) {
      return new ResponseEntity(HttpStatus.CONFLICT);
    }
    User emailDuplicateUser = userService.findByNickname(userJoinRequestDTO.getNickname());
    if (emailDuplicateUser != null) {
      return new ResponseEntity(HttpStatus.CONFLICT);
    }

    userService.join(userJoinRequestDTO);
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

    User user = userService.findByEmail(userLoginRequestDTO.getEmail());
    if (user == null) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    if (!passwordEncoder.matches(userLoginRequestDTO.getPassword(), user.getPassword())) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    String accessToken = jwtStrategy.createAccessToken(user.getId(), user.getRole(), user.getEmail());
    String refreshToken = jwtStrategy.createRefreshToken(user.getId(), user.getRole(), user.getEmail());

    AccessTokenResponseDTO accessTokenResponseDTO = new AccessTokenResponseDTO();
    accessTokenResponseDTO.setAccessToken(accessToken);

    Cookie cookie  = cookieStrategy.createCookie("refreshToken", refreshToken);

    redisStrategy.setValueExpire(refreshToken, user.getEmail(), refreshTokenValidityInSeconds);
    httpServletResponse.addCookie(cookie);
    return new ResponseEntity(accessTokenResponseDTO, HttpStatus.OK);
  }

  @PostMapping("/refresh")
  public ResponseEntity updateToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {


    Cookie cookie = cookieStrategy.getCookie("refreshToken", httpServletRequest);
    String refreshTokenTakenFromCookie = cookie.getValue();

    //만료기간 지나지 않았는지 토큰 유효성
    if (!jwtStrategy.validateToken(refreshTokenTakenFromCookie)) {
      return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    String emailTakenFromCache = redisStrategy.getValueData(refreshTokenTakenFromCookie);
    if (!emailTakenFromCache.equals(jwtStrategy.getUserEmail(refreshTokenTakenFromCookie))) {
      return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    //새로운 refreshToken, accessToken을 리턴.
    int userId = jwtStrategy.getUserId(refreshTokenTakenFromCookie);
    int userRole = jwtStrategy.getUserRole(refreshTokenTakenFromCookie);
    String userEmail = jwtStrategy.getUserEmail(refreshTokenTakenFromCookie);
    String newAccessToken = jwtStrategy.createAccessToken(userId, userRole, userEmail);
    String newRefreshToken = jwtStrategy.createRefreshToken(userId, userRole, userEmail);

    AccessTokenResponseDTO accessTokenResponseDTO = new AccessTokenResponseDTO();
    accessTokenResponseDTO.setAccessToken(newAccessToken);

    Cookie newCookie  = cookieStrategy.createCookie("refreshToken", newRefreshToken);

    redisStrategy.deleteValue(refreshTokenTakenFromCookie);
    redisStrategy.setValueExpire(newRefreshToken, userEmail, refreshTokenValidityInSeconds);
    httpServletResponse.addCookie(newCookie);

    return new ResponseEntity(accessTokenResponseDTO, HttpStatus.OK);
  }

}
