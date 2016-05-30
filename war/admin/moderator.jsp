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
<title>Управление: модератор</title>
<link rel='stylesheet' type='text/css' href='s_admin.css' />
</head>
<body>
	<div id="header">
		<div id="header-main">
			<H1>Администрирование: панель модератора</H1>
		</div>
	</div>
	<div id='main-div'>
		<%
			UserEntity user = null;
			String token = "";
			for (Cookie c : request.getCookies()) {
				if (c.getName().equals("sesToken")) {
					token = c.getValue();
				}
			}
			user = AuthContainer.getUser(token);
			List<Product> products = ProductDAO.getAll();
			List<UserEntity> users = UserDAO.getUnactiveClientsByCorp(user, false);
		%>
		
		<div class="div-form-button">
			<a href="/workspace.html">Главная страница</a>
		</div>

		<h1>Продукты:</h1>
		
		<div class='table-div'>
			<table class='table-list'>
				<tr class='table-list-header'>
					<td>Имя</td>
					<td>Файлы</td>
					<td>Город</td>
					<td>Статус</td>					
					<td>Активация</td>
				</tr>
				<%
					for (Product prod : products) {
				%>
				<tr>
					<td><%=prod.getTitle()%></td>
					<td>Файлов:<%=prod.getFiles().size()%></td>
					<td><%= prod.getCity().getCyrname() %>
					<td><% out.print((prod.isActual()) ? "Активно" : "Неактивно"); %></td>
					<td><a href="prodactiv.jsp?id=<%=prod.getId()%>">Информация</a></td>
				</tr>
				<%
					}
				%>
			</table>
		</div>

		<h1>Неактивированные клиенты:</h1>
		<div class='table-div'>
			<table class='table-list'>
				<tr class='table-list-header'>
					<td>Имя</td>
					<td>Файлы</td>
					<td>Активация</td>
				</tr>
				<%
					for (UserEntity client : users) {
				%>
				<tr>
					<td><%=client.getName() + " " + client.getSurname()%></td>
					<td>Файлов:<%=client.getFiles().size()%></td>
					<td><a href="activation.jsp?user=<%=client.getId()%>">Обзор</a></td>
				</tr>
				<%
					}
				%>
			</table>
		</div>
	</div>
</body>
</html>