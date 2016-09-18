<%@page import="eduportal.model.*"%>
<%@page import="eduportal.dao.entity.*"%>
<%@page import="eduportal.dao.*"%>
<%@page import="eduportal.api.*"%>
<%@page import="eduportal.util.*"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cookie swapper</title>
</head>
<body>
<h1> Hello
	<%

	Employee empl = null;
	String sesToken = null;
	String mainToken = null;
	String target = null;
	for (Cookie c : request.getCookies()) {
		if (c.getName().equals("sesToken")) {
			sesToken = c.getValue();
		}
		if (c.getName().equals("mainToken")) {
			mainToken = c.getValue();
		}
		if (c.getName().equals("target")) {
			target = c.getValue();
		}
	}
	
	if (target != null) {
		Employee emp = AuthContainer.getEmp(sesToken);
		UserEntity user = UserDAO.get(target);
		if (user != null) {
			AuthToken tok = AuthContainer.requireAuth(emp, user);
			if (tok != null) {
				response.addCookie(new Cookie("sesToken", tok.getSessionId()));
				out.print("Успешно. <a href = 'workspace.html'>Продолжить</a>");
			} else {
				out.print("Что-то прошло не так. <a href = 'admin/moderator.jsp'>Return</a>");
			}
		} else {
			out.print("User not found? Обратитесь к разработчикам. <a href = 'admin/moderator.jsp'>Return</a>");
		}
	} else if (mainToken != null) {
		response.addCookie(new Cookie("sesToken", mainToken));
		out.print("Target switched back. <a href = 'admin/moderator.jsp'>Return</a>");
	}
	
	%>
</h1>
</body>
</html>