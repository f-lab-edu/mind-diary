package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.dto.request.UserLoginRequestDTO;
import com.mindDiary.mindDiary.dto.response.UserLoginResponseDTO;
import com.mindDiary.mindDiary.service.UserService;
import com.mindDiary.mindDiary.strategy.cookie.CookieStrategy;
import com.mindDiary.mindDiary.strategy.jwt.JwtStrategy;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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

  @PostMapping("/join")
  public ResponseEntity join(@RequestBody @Valid UserJoinRequestDTO userJoinRequestDTO) {

    User nicknameDuplicateUser = userService.findByEmail(userJoinRequestDTO.getEmail());
    if (nicknameDuplicateUser != null) {
      return new ResponseEntity(HttpStatus.CONFLICT);
    }
    User emailDuplicateUser = userService.findByEmail(userJoinRequestDTO.getNickname());
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

    String accessToken = jwtStrategy.createAccessToken(user.getId(), user.getRole());
    String refreshToken = jwtStrategy.createRefreshToken(user.getId(), user.getRole());

    UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO();
    userLoginResponseDTO.setAccessToken(accessToken);

    Cookie cookie  = cookieStrategy.createCookie("refreshToken", refreshToken);

    httpServletResponse.addCookie(cookie);
    return new ResponseEntity(userLoginResponseDTO, HttpStatus.OK);
  }

}
