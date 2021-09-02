package com.mindDiary.mindDiary.annotation;

import com.mindDiary.mindDiary.dto.Role;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LoginCheck {
  Role checkLevel() default Role.USER;
}

