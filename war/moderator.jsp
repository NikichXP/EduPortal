<%@page import="eduportal.model.*"%>
<%@page import="eduportal.dao.entity.*"%>
<%@page import="eduportal.dao.*"%>
<%@page import="eduportal.api.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Управление: модератор</title>
</head>
<body>
<%
		UserEntity user = null;
		String token = "";
		for (Cookie c : request.getCookies()) {
			if (c.getName().equals("sesToken")) {
				token = c.getValue();
			}
		}
		if (request.getParameter("token") != null) {
			token = request.getParameter("token");
		}
		user = AuthContainer.getUser(token);
	%>
	<h1>Неактивированные клиенты:</h1>
	<table>
	<% for (UserEntity client : UserDAO.getUnactiveClientsByCorp(user, false)) { %>
	<tr>
	<td><%= client.getName() + " " + client.getSurname() %></td>
	<td>Файлов:<%= client.getFiles().size() %></td>
	<td><a href="activation.jsp?user=<%=client.getId()%>">Обзор</a></td>
	</tr>
	<% } %>
	</table>
</body>
</html>