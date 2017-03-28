package com.eduportal.interceptor;

import com.eduportal.AppLoader;
import com.eduportal.entity.UserAction;
import com.eduportal.entity.UserEntity;
import com.eduportal.model.AuthContainer;
import com.eduportal.repo.UserActionRepository;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

public class LoggerInterceptor extends HandlerInterceptorAdapter {

	private UserActionRepository repo;

	public LoggerInterceptor() {
		new Thread(() -> {
			try {
				while (AppLoader.ctx == null) {
					Thread.sleep(10);
				}
				repo = AppLoader.ctx.getBean(UserActionRepository.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Method method = ((HandlerMethod) handler).getMethod();
		LogAction log = (method.getAnnotation(LogAction.class) != null) ? method.getAnnotation(LogAction.class)
				: method.getDeclaringClass().getAnnotation(LogAction.class);
		if (log == null) {
			return true;
		}

		String userid = "anon";

		UserEntity user = null;
		try {
			user = getUser(request);
		} catch (Exception e) {

		}

		if (user != null) {
			userid = user.getId();
		}

		repo.insert(
				new UserAction(
						request.getRemoteAddr(),
						userid,
						request.getRequestURL().toString(),
						log.value(),
						method.getName(),
						request.getParameterMap()
				)
		);
		return true;
	}

	private UserEntity getUser(HttpServletRequest request) {
		String token = Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals("sessionId"))
				.map(Cookie::getValue)
				.findAny().orElseGet(() -> (request.getParameter("token") != null) ? request.getParameter("token") : request.getParameter("sessionId"));
		return (token == null) ? null : AuthContainer.getUser(token);
	}

}
