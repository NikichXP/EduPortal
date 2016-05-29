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
<script type="text/javascript" src="jquery-2.2.3.min.js"></script>
<script type="text/javascript" src="admin.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Product edit page</title>
<link rel='stylesheet' type='text/css' href='s_admin.css' />
</head>
<body>
	<%
		UserEntity user = null;
		for (Cookie c : request.getCookies()) {
			if (c.getName().equals("sesToken")) {
				user = AuthContainer.getUser(c.getValue());
			}
		}
		if (user == null || AccessLogic.canAccessAdminPanel(user) == false) {
			out.print("</body></html>");
			return;
		}
		Product product = OrderDAO.getProduct(request.getParameter("id"));
	%>
	<div id="header">
		<div id="header-main">
			<H1>Администрирование: редактирование продуктов</H1>
		</div>
	</div>

	<form>
		<input type="hidden" name="id" value="<%=product.getId()%>" id="prodid">
		<table>
			<tr>
				<td>Название</td>
				<td><input type="text" name="title" id="title"
					value="<%=product.getTitle()%>"></td>
			</tr>
			<tr>
				<td>Описание</td>
				<td><input type="text" name="title" id="descr"
					value="<%=product.getDescription()%>"></td>
			</tr>
			<tr>
				<td>Цена</td>
				<td><input type="text" name="title" id="price"
					value="<%=product.getDefaultPrice()%>"></td>
			</tr>
			<tr>
				<td>Дата начала</td>
				<td><input type="text" name="title" id="begin"
					value="<%=product.getStart()%>"></td>
			</tr>
			<tr>
				<td>Дата окончания</td>
				<td><input type="text" name="title" id="end"
					value="<%=product.getEnd()%>"></td>
			</tr>
			<tr>
				<td>Город проведения</td>
				<td><input type="text" name="title" id="city"
					value="<%=product.getCity().getCyrname()%>"></td>
			</tr>
		</table>
		<input type="submit" value="Submit changes" id="prodSubmit">
	</form>

</body>
</html>