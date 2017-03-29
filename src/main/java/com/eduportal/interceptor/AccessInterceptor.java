package com.eduportal.interceptor;

import com.eduportal.AppLoader;
import com.eduportal.entity.UserEntity;
import com.eduportal.model.AuthContainer;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

public class AccessInterceptor extends HandlerInterceptorAdapter {

	private AuthContainer authController;

	public AccessInterceptor() {
		new Thread(() -> {
			try {
				while (AppLoader.ctx == null) {
					Thread.sleep(10);
				}
				authController = AppLoader.ctx.getBean(AuthContainer.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Method method = ((HandlerMethod) handler).getMethod();
		Auth auth = (method.getAnnotation(Auth.class) != null) ? method.getAnnotation(Auth.class) :
				method.getDeclaringClass().getAnnotation(Auth.class);

		if (auth == null) {
			return true;
		}

		UserEntity user = null;
		try {
			user = getUser(request);
		} catch (Exception e) {
			return true; //TODO false
		}
		if (user == null) {
			return true; //TODO false
		}

		//TODO Normalize
		if (user.getEntityClassName().compareToIgnoreCase(user.getEntityClassName()) == 0) {
			return true;
		}

		System.out.println("Check auth: " + ((HandlerMethod) handler).getMethod().getAnnotation(Auth.class).value());
		return true;
	}

	private UserEntity getUser(HttpServletRequest request) {
		String token = Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals("sessionId"))
				.map(Cookie::getValue)
				.findAny().orElseGet(() -> (request.getParameter("token") != null) ? request.getParameter("token") : request.getParameter("sessionId"));
		return (token == null) ? null : authController.getUser(token);
	}

}
