package com.mindDiary.mindDiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableCaching
public class MindDiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindDiaryApplication.class, args);
	}

}
