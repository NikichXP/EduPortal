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
</head>
<body>
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
	%>

	<form action="edituser.jsp">
	<input type="hidden" name="id" id="<%=user.getId() %>">
		<table>
			<tr>
				<td>E-mail:</td>
				<td><input type="text" name="mail" value="<%=user.getMail()%>"></td>
			</tr>
			<tr>
				<td>Номер телефона:</td>
				<td><input type="text" name="phone" value="<%=user.getPhone()%>"></td>
			</tr>
			<tr>
				<td>Паспорт:</td>
				<td><input type="text" name="паспорт" value="<%=user.getPassport()%>"></td>
			</tr>
		</table>
	</form>
	<%= request.getParameter("паспорт") %>
</body>
</html>