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
<title>Управление: администратор</title>
<link rel='stylesheet' type='text/css' href='s_admin.css' />
</head>
<body>
	<div id="header">
		<div id="header-main">
			<H1>Администрирование</H1>
		</div>
	</div>
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
	<div id='main-div'>
		<div class="div-form-button">
			<a href=moderator.jsp?token=e24379d8-9874-4a52-8b85-242a565d6b9f>Управление</a>
		</div>
		<h1>Компании</h1>
		<div class='table-div'>
			<br>
			<table class='table-list'>
				<tr class='table-list-header'>
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
					<td><%=comp.getOwner().getMail()%></td>
					<td><a href="edit.jsp?corp=<%=comp.getId()%>&token=<%=token%>">Edit</a></td>
				</tr>
				<%
					}
				%>
			</table>
		</div>
		<div class="div-form-button">
			<a href="createagent.jsp">Добавить новую компанию</a>
		</div>
		<%
			StringBuilder sb;
		%>
		<br>
		<h1>Сотрудники Вашей фирмы</h1>
		<br>
		<div class="div-form-button">
			<a
				href="/Admin.jsp?token=<%=request.getParameter("token")%><%out.println((request.getParameter("all") == null) ? "&all=true" : "");%>">
				Показать/скрыть всех</a>
		</div>
		<br>

		<div class='table-div'>
			<table class='table-list'>
				<tr class='table-list-header'>
					<td>HEADER</td>
					<td>HEADER</td>
					<td>HEADER</td>
					<td>HEADER</td>
					<td>HEADER</td>
					<td>HEADER</td>
				</tr>
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
					<td><%=u.getMail()%></td>
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
						<%
							out.print((u.getAccessLevel() > 1000) ? "BOSS" : u.getAccessLevel() + "");
						%>
					</td>
					<td><a href="edituser.jsp?corp=<%=u.getId()%>">Edit</a></td>
				</tr>
				<%
					}
				%>
			</table>
		</div>
	</div>
</body>
</html>