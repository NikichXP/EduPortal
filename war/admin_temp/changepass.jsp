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
<title>Insert title here</title>
<link rel='stylesheet' type='text/css' href='s_admin.css' />
</head>
<body>
	<%
		UserEntity user = null;
		for (Cookie c : request.getCookies()) {
			if (c.getName().equals("sesToken")) {
				user = AuthContainer.getUser(c.getValue());
			}
		}
		if (user == null) {
			response.sendRedirect("/auth.html");
		}
		String exist = request.getParameter("exist");
		String newPass = request.getParameter("new");
		exist = UserUtils.encodePass(exist);
		if (user.getPass().equals(exist) == false) {
			System.out.println("Wrong pass");
			return;
		}
		if (newPass.equals(request.getParameter("valid")) == false) {
			return;
		}
		user.setPass(newPass);
		UserDAO.update(user);
	%>
	<div id="header">
		<div id="header-main">
			<H1>Смена пароля</H1>
		</div>
	</div>
	<h3>
		<form action="changepass.jsp" method="post">
			Текущий пароль: <input type="password" name="exist" value="password"><br>
			Новый пароль: <input type="password" name="new" value="password"><br>
			Повтор пароля: <input type="password" name="valid" value="password"><br>
			<input type="submit" value="Change pass">
		</form>
	</h3>
</body>
</html>