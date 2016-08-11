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
			Employee user = null;
			String token = null;
			for (Cookie c : request.getCookies()) {
				if (c.getName().equals("sesToken")) {
					user = AuthContainer.getEmp(c.getValue());
					token = c.getValue();
				}
			}
			out.print("USER:" + user.toString());
			if (user == null || AccessLogic.canAccessAdminPanel(user) == false) {
				out.print("</div></body></html>");
				return;
			}
			StringBuilder sb;
			if (AccessLogic.canAccessAdminPanel(user)) {
		%>
		<h1>Компании</h1>
		Данный функционал временно изменен и недоступен.
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
					List<UserEntity> users = UserDAO.getCorpEmployees(user.getCorporation());
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
							if (((Employee)u).getAccessLevel() < AccessSettings.MODERATOR_LEVEL) {
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
								for (Country c : ((Employee)u).getCountryList()) {
									sb.append(c.getName() + ", ");
								}
								out.println(sb.toString());
						%>
					</td>
					<td>
						<%
							out.print((((Employee)u).getAccessLevel() > 1000) ? "Администратор"
										: (((Employee)u).getAccessLevel() >= AccessSettings.MODERATOR_LEVEL) ? "Модератор" : "Клиент");
						%>
					</td>
					<td><a href="edituser.jsp?id=<%=u.getId()%>">Edit</a></td>
				</tr>
				<%
					}
				%>
			</table>
			<h1>Города</h1>
			<table class='table-list'>
				<tr class='table-list-header'>
					<td>ID</td>
					<td>Название города</td>
					<td>Название латиницей</td>
					<td>Название страны</td>
					<td>Название латиницей</td>
					<td>Изменить город</td>
					<td>Изменить страну</td>
				</tr>
				<%
					for (City c : GeoDAO.getCityList()) {
				%>
				<tr>
					<td><%=c.getId()%></td>
					<td><%=c.getName()%></td>
					<td><%=c.getCyrname()%></td>
					<td><%=c.getCountry().getName()%></td>
					<td><%=c.getCountry().getCyrname()%></td>
					<td><a href="geoedit.jsp?type=city&id=<%=c.getId()%>">Город</a></td>
					<td><a href="geoedit.jsp?type=country&id=<%=c.getCountry().getId()%>">Страна</a></td>
					<td><a href="/_ah/api/util/v1/deletecity?token=<%=token%>&city=<%=c.getId()%>">Удалить город</a>
				</tr>
				<%
					}
				%>
			</table>
			<form action="/_ah/api/util/v1/createcity">
			<input type="hidden" name="token" value="<%= token%>">
			<input type="text" name = "city" value="Киев"><br>
			<input type="text" name = "country" value="Украина">
			<input type="submit" value = "Создать город">
			</form>
		</div>
	</div>
</body>
</html>