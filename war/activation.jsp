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
<title>Insert title here</title>
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
		UserEntity client = UserDAO.get(request.getParameter("user"));
		if (client == null) {
			return;
		}
		if (request.getParameter("act") != null) {
			client.setActive(true);
			UserDAO.update(client);
			response.sendRedirect("/moderator.jsp?token="+ token);
			return;
		}
	%><h1>Прикрепленные файлы:</h1>
	<table>
		<%
			for (SavedFile file : client.getFiles()) {
		%>
		<tr>
			<td><a href="/FileProcessorServlet?blob-file=<%=file.getId()%>"><%=file.getId()%></a></td>
		</tr>
		<%
			}
		%>
	</table>
	<a href="moderator.jsp?token=<%=token%>">Вернуться в прежнее меню</a>
	<a href="activation.jsp?token=<%=token + "&user=" + client.getId() + "&act=true"%>">Активировать</a>
</body>
</html>