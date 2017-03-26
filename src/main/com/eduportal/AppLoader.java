package com.eduportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AppLoader {

	public static final ApplicationContext ctx = SpringApplication.run(AppLoader.class, new String[]{});

	public static void main(String[] args) {
		while(ctx == null) {}
		System.out.println("Loading complete");
	}

	public static <T> T get(Class<T> clazz) {
		while (ctx == null) {}
		return ctx.getBean(clazz);
	}
}
