package com.mindDiary.mindDiary.aop;

import com.mindDiary.mindDiary.annotation.LoginCheck;
import com.mindDiary.mindDiary.annotation.LoginCheck.CheckLevel;
import com.mindDiary.mindDiary.dto.Role;
import com.mindDiary.mindDiary.exception.businessException.PermissionDeniedException;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
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
  private static final String USER_ID = "userId";

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

    Object[] modifiedArgs = modifyArgsWithUserID(tokenStrategy.getUserId(token),proceedingJoinPoint);

    return proceedingJoinPoint.proceed(modifiedArgs);

  }

  private Object[] modifyArgsWithUserID(int id, ProceedingJoinPoint proceedingJoinPoint) {
    Object[] parameters = proceedingJoinPoint.getArgs();

    MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
    Method method = signature.getMethod();

    for (int i = 0; i < method.getParameters().length; i++) {
      String parameterName = method.getParameters()[i].getName();
      if (parameterName.equals(USER_ID)) {
        parameters[i] = id;
      }
    }
    return parameters;
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
