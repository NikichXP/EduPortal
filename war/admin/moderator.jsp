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
<script type="text/javascript" src="jquery-2.2.3.min.js"></script>
</head>
<body>
	<div id="header">
		<div id="header-main">
			<H1>Администрирование: панель модератора</H1>
		</div>
	</div>
	<div id='main-div'>
		<%
			Employee user = null;
			String token = "";
			for (Cookie c : request.getCookies()) {
				if (c.getName().equals("sesToken")) {
					token = c.getValue();
				}
			}
			user = AuthContainer.getEmp(token);
			List<Product> products = ProductDAO.getAll();
			List<UserEntity> users = UserDAO.getUnactiveClients(false);
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
					<td><%=prod.getCity().getCyrname()%>
					<td>
						<%
							out.print((prod.isActual()) ? "Активно" : "Неактивно");
						%>
					</td>
					<td><a href="prodactiv.jsp?id=<%=prod.getId()%>">Информация</a></td>
				</tr>
				<%
					}
				%>
			</table>
		</div>


		<h1>Ваши клиенты</h1>
		<div class="div-form-button" id='usertable-button'>
			<a>Скрыть/показать</a>
		</div>
		<div class='table-div' id='usertable'>
			<table class='table-list'>
				<tr class='table-list-header'>
					<td>Имя</td>
					<td>Почта</td>
					<td>Файлы</td>
					<td>Смена пароля</td>
				</tr>
				<%
					for (UserEntity u : UserDAO.getClients(user)) {
				%>
				<tr>
					<td><%=u.getName() + " " + u.getSurname()%></td>
					<td>Создатель</td>
					<td>Файлы</td>
					<td><input type="button" value = 'RESET' id='passreset<%=u.getMail().replaceAll("@", "-at-").replaceAll("\\.", "-dot-") %>'/></td>
					<script>
					$('#passreset<%=u.getMail().replaceAll("@", "-at-").replaceAll("\\.", "-dot-") + "'"%>).on('click', function() {
						if (confirm('Are u sure')) {
							var userData = {
									user: '<%= u.getId() + "'" %>,
									token: '<%= token + "'" %>,
									newpass: '<%= u.getMail().substring(0, u.getMail().indexOf('@')) + "'"%>
								};
							$.ajax({
								type: 'GET',
								url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/resetpass',
								data: userData,
								success: function(resData) {
									alert (resData.value);
								},
							});
						}
					})
					</script>
				</tr>
				<%
					}
				%>
			</table>
		</div>


		<h1>Неактивированные клиенты:</h1>
		<div class="div-form-button" id='unactive-users-button'>
			<a>Скрыть/показать</a>
		</div>
		<div class='table-div' id='unactive-users'>
			<table class='table-list'>
				<tr class='table-list-header'>
					<td>Имя</td>
					<td>Создатель</td>
					<td>Файлы</td>
					<td>Активация</td>
				</tr>
				<%
					Employee temp;
					for (UserEntity client : users) {
				%>
				<tr>
					<%
						temp = client.creatorEntity();
					%>
					<td><%=client.getName() + " " + client.getSurname()%></td>
					<td><%=temp.getName() + " " + temp.getSurname()%></td>
					<td>Файлов:<%=client.getFiles().size()%></td>
					<td><a href="activation.jsp?user=<%=client.getId()%>">Обзор</a></td>
				</tr>
				<%
					}
				%>
			</table>
		</div>
	</div>

	<script type="text/javascript">
		$('#usertable').css('display', 'none');
		$('#usertable-button').on('click', function() {
			if ($('#usertable').css('display') == 'block') {
				$('#usertable').css('display', 'none');
			} else {
				$('#usertable').css('display', 'block');
			}
		});
	</script>
</body>
</html>