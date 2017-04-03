package com.eduportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

@SpringBootApplication
@RestController
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

	@RequestMapping("/")
	public void index(HttpServletResponse response) throws IOException {
		LinkedList x;
		response.sendRedirect("/auth.html");
	}
}
