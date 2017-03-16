<%@page import="eduportal.model.*"%>
<%@page import="eduportal.dao.entity.*"%>
<%@page import="eduportal.dao.*"%>
<%@page import="eduportal.api.*"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UserEditor</title>
<link rel='stylesheet' type='text/css' href='s_admin.css' />
</head>
<body>
	<div id="header">
		<div id="header-main">
			<H1>Управление пользователями</H1>
		</div>
	</div>
	<%
		UserEntity admin = null;
		for (Cookie c : request.getCookies()) {
			if (c.getName().equals("sesToken")) {
				admin = AuthContainer.getUser(c.getValue());
			}
		}
		UserEntity user = UserDAO.get(request.getParameter("id"));
		if (admin.getAccessLevel() < AccessSettings.MODERATOR_LEVEL) {
			return;
		}
		if (user == null) {
			response.sendRedirect("/admin/moderator.jsp");
			return;
		}

		if (request.getParameter("mail") != null) {
			for (String param : UserEntity.userParams) {
				if (request.getParameter(param) != null) {
					user.putData(param, request.getParameter(param));
				}
			}
		}
		UserDAO.update(user);
	%>

	<div id='main-div'>
		<div class="div-form-button">
			<a href=/admin/moderator.jsp>Назад на панель управления</a>
		</div>
		<div class='table-div'>
			<form action="edituser.jsp">
				<input type="hidden" name="id" value="<%=user.getId()%>">
				<table class='table-list'>
					<tr class='table-list-header'>
						<td>Ключ</td>
						<td>Значение</td>
					</tr>
					<tr>
						<td>E-mail:</td>
						<td><input type="text" name="mail"
							value="<%=user.getMail()%>"></td>
					</tr>
					<tr>
						<td>Номер телефона:</td>
						<td><input type="text" name="phone"
							value="<%=user.getPhone()%>"></td>
					</tr>
					<tr>
						<td>Паспорт:</td>
						<td><input type="text" name="паспорт"
							value="<%=user.getPassport()%>"></td>
					</tr>
					<%
						for (String param : UserEntity.userParams) {
					%>
					<tr>
						<td><%=param%></td>
						<td><input type="text" name="<%=param%>"
							value="<%out.print(((user.getData(param) != null) ? user.getData(param) : ""));%>"></td>
					</tr>
					<%
						}
					%>
				</table>
				<input type="submit" value="Change the world">
				</form>
		</div>
		
	</div>
</body>
</html>