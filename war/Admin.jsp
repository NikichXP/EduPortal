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
	<h1>Companies:</h1>
	<br>
	<table>
	<tr>
	<td>Name</td>
	<td>ID</td>
	<td>Owner</td>
	<td>Mail</td>
	<td>Editor</td>
	</tr>
		<%
			for (Corporation comp : UserDAO.getCorpList()) {
		%>
		<tr>
			<td><%=comp.getName()%></td>
			<td><%=comp.getId()%></td>
			<td><%=comp.getOwner().getName() + " " + comp.getOwner().getSurname()%></td>
			<td><%=comp.getOwner().getMail() %></td>
			<td><a href="edit.jsp?corp=<%=comp.getId()%>&token=<%=token%>">Edit</a></td>
		</tr>
		<%
			}
		%>
	</table>
	<a href = "createagent.jsp">Create new agent company</a>
	<%
		StringBuilder sb;
	%>
	<br>
	<h1>Сотрудники Вашей фирмы</h1>
	<br>
	<a
		href="/Admin.jsp?token=<%=request.getParameter("token")%><%out.println((request.getParameter("all") == null) ? "&all=true" : "");%>">See
		all on/off</a>
	<br>
	<br>
	<br>

	<table>
		<%
			for (UserEntity u : UserDAO.getCorpEmployees(user.corporationEntity())) {
				if (request.getParameter("all") == null) {
					if (u.getAccessLevel() < AccessSettings.MODERATOR_LEVEL) {
						continue;
					}
				}
		%>
		<tr>
			<td><%=u.getName() + " " + u.getSurname()%></td>
			<td><%=u.getMail() %></td>
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
			<td>
				<% out.print((u.getAccessLevel() > 1000) ? "BOSS" : u.getAccessLevel()+""); %>
			</td>
			<td><a href="edituser.jsp?corp=<%=u.getId()%>">Edit</a></td>
		</tr>
		<%
			}
		%>
	</table>

</body>
</html>