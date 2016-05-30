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
<title>Insert title here</title>
</head>
<body>
	<%
		UserEntity admin = null;
		for (Cookie c : request.getCookies()) {
			if (c.getName().equals("sesToken")) {
				admin = AuthContainer.getUser(c.getValue());
			}
		}
		if (admin.getAccessLevel() < AccessSettings.MODERATOR_LEVEL) {
			return;
		}
		if (admin.getCorporation() != AccessSettings.OWNERCORP().getId()) {
			return;
		}
		String title = request.getParameter("title");
		if (title != null) {
			Product prod = new Product();
			prod.setTitle(title);
			prod.setDescription(request.getParameter("descr"));
			prod.setCity(GeoDAO.getCity(request.getParameter("city")));
			prod.setStart(request.getParameter("start"));
			prod.setEnd(request.getParameter("end"));
			prod.setCurrency(request.getParameter("curr"));
			try {
				prod.setDefaultPrice(Double.parseDouble(request.getParameter("price")));
				ProductDAO.save(prod);
			} catch (Exception e) {
				out.println(
						"<script>alert('Вы ввели неверный формат числа цены. Используйте следующий: \"12345.67\"')</script>");
				return;
			}
			out.print("Продукт создан. Ищите его <a href=\"/workspace.html\">на главной странице</a>. Не забудьте вначале его активировать.");
		} else {
	%>
	<h2>Введите данные продукта</h2>
	<form action="createproduct.jsp">
		<table>
			<tr>
				<td>Название продукта</td>
				<td><input type="text" name="title"></td>
			</tr>
			<tr>
				<td>Описание</td>
				<td><input type="text" name="descr"></td>
			</tr>
			<tr>
				<td>Город</td>
				<td><input type="text" name="city"></td>
			</tr>
			<tr>
				<td>Цена</td>
				<td><input type="text" name="price"></td>
			</tr>
			<tr>
				<td>Валюта</td>
				<td><input type="text" name="curr"></td>
			</tr>
			<tr>
				<td>Дата начала (ДД/ММ)</td>
				<td><input type="text" name="start" value="01/09"></td>
			</tr>
			<tr>
				<td>Дата окончания (ДД/ММ)</td>
				<td><input type="text" name="end" value="31/05"></td>
			</tr>
		</table>
		<input type="submit" value = "Создать заказ">
	</form>
	<%
		}
	%>
</body>
</html>