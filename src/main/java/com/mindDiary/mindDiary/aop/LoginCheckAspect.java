package com.mindDiary.mindDiary.aop;

import com.mindDiary.mindDiary.annotation.LoginCheck;
import com.mindDiary.mindDiary.annotation.LoginCheck.CheckLevel;
import com.mindDiary.mindDiary.dto.Role;
import com.mindDiary.mindDiary.exception.businessException.PermissionDeniedException;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginCheckAspect {

  @Value("${jwt.header}")
  private String requestHeaderKey;

  private final TokenStrategy tokenStrategy;

  @Around("@annotation(loginCheck)")
  public Object loginCheck(ProceedingJoinPoint proceedingJoinPoint, LoginCheck loginCheck)
      throws Throwable {

    ServletRequestAttributes requestAttributes =
        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = requestAttributes.getRequest();

    String token = request.getHeader(requestHeaderKey);

    tokenStrategy.validateToken(token);

    String role = tokenStrategy.getUserRole(token);
    if (loginCheck.checkLevel() == CheckLevel.ADMIN) {
      checkAdmin(role);
    }

    if (loginCheck.checkLevel() == CheckLevel.USER) {
      checkUser(role);
    }
    int id = tokenStrategy.getUserId(token);

    Object[] modifiedArgs = proceedingJoinPoint.getArgs();
    if(proceedingJoinPoint.getArgs()!=null) {
      modifiedArgs[modifiedArgs.length - 1] = id;
    }
    return proceedingJoinPoint.proceed(modifiedArgs);

  }

  private void checkAdmin(String role) {
    if (!Role.isAdmin(role)) {
      throw new PermissionDeniedException();
    }
  }

  private void checkUser(String role) {
    if (!Role.isUser(role)) {
      throw new PermissionDeniedException();
    }
  }
}
