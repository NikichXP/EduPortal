<%@page import="eduportal.model.*"%>
<%@page import="eduportal.dao.entity.*"%>
<%@page import="eduportal.dao.*"%>
<%@page import="eduportal.api.UserAPI"%>
<%@page import="java.util.*"%>
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
	<div id='main-div'>
		<div class="div-form-button">
			<a href=/admin/moderator.jsp>Назад на панель управления</a>
		</div>
		<%
			UserEntity user = null;
			String token = null;
			for (Cookie c : request.getCookies()) {
				if (c.getName().equals("sesToken")) {
					user = AuthContainer.getUser(c.getValue());
				}
			}
			if (user == null || AccessLogic.canAccessAdminPanel(user) == false) {
				out.print("</div></body></html>");
				return;
			}
			StringBuilder sb;
			if (AccessLogic.canAccessAdminPanel(user)) {
		%>
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
			}
		%>
		
		<br>
		<h1>Сотрудники Вашей фирмы</h1>
		<br>
		<div class="div-form-button">
			<a
				href="/admin/admin.jsp?<%out.println((request.getParameter("all") == null) ? "&all=true" : "");%>">
				Показать/скрыть всех</a>
		</div>
		<br>
		<div class='table-div'>
			<table class='table-list'>
				<tr class='table-list-header'>
					<td>Имя</td>
					<td>e-mail</td>
					<td>Доступные страны</td>
					<td>Доступ</td>
					<td>Редактирование</td>
				</tr>
				<%
					List<UserEntity> users = UserDAO.getCorpEmployees(user.corporationEntity());
					UserEntity tmp;
					int minIndex;
					for (int i = 0; i < users.size(); i++) {
						for (int j = i; j < users.size(); j++) {
							minIndex = i;
							if (users.get(i).compareTo(users.get(j)) > 0) {
								minIndex = j;
							}
							if (minIndex != i) {
								tmp = users.get(minIndex);
								users.set(minIndex, users.get(i));
								users.set(i, tmp);
							}
						}
					}
					for (UserEntity u : users) {
						if (request.getParameter("all") == null) {
							if (u.getAccessLevel() < AccessSettings.MODERATOR_LEVEL) {
								continue;
							}
						}
						if (u.getId() == user.getId()) {
							continue;
						}
				%>
				<tr>
					<td><%=u.getName() + " " + u.getSurname()%></td>
					<td><%=u.getMail()%></td>
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
							out.print((u.getAccessLevel() > 1000) ? "Администратор"
										: (u.getAccessLevel() >= AccessSettings.MODERATOR_LEVEL) ? "Модератор" : "Клиент");
						%>
					</td>
					<td><a href="edituser.jsp?emp=<%=u.getId()%>">Edit</a></td>
				</tr>
				<%
					}
				%>
			</table>
		</div>
	</div>
</body>
</html>