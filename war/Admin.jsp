<%@page import="eduportal.model.*"%>
<%@page import="eduportal.dao.entity.*"%>
<%@page import="eduportal.dao.*"%>
<%@page import="eduportal.api.UserAPI"%>
<%@page import="eduportal.dao.entity.UserEntity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		UserEntity user = null;
		for (Cookie c : request.getCookies()) {
			if (c.getName().equals("sesToken")) {
				user = AuthContainer.getUser(c.getValue());
			}
		}
	%>
	<h1>Company:</h1>
	<br>
	<table>
		<%
			for (Corporation comp : UserDAO.getCorpList()) {
		%>
		<tr>
			<td><%=comp.getName()%></td>
			<td><%=comp.getId()%></td>
			<td><%=comp.getOwner().getName() + " " + comp.getOwner().getSurname()%></td>
			<td><a href="edit.jsp?corp=<%=comp.getId()%>">Edit</a></td>
		</tr>
		<%
			}
		%>
	</table>
	<%
		StringBuilder sb;
	%>
	<br>
	<h1>Сотрудники Вашей фирмы</h1>
	<table>
		<%
			for (UserEntity u : UserDAO.getCorpEmployees(user.getPermission().corporationEntity())) {
				if (u.getAccessLevel() >= AccessSettings.MODERATOR_LEVEL) {
		%>
		<tr>
			<td><%=u.getName() + " " + u.getSurname()%></td>
			<td>
				<%
					sb = new StringBuilder();
							for (City c : u.getPermission().cityList()) {
								sb.append(c.getName() + ", ");
							}
							out.println(sb.toString());
				%>
			</td>
			<td>
				<%
					sb = new StringBuilder();
							for (Country c : u.getPermission().countryList()) {
								sb.append(c.getName() + ", ");
							}
							out.println(sb.toString());
				%>
			</td>
			<td><a href="edituser.jsp?corp=<%=u.getId()%>">Edit</a></td>
		</tr>
		<%
			}
			}
		%>
	</table>
</body>
</html>