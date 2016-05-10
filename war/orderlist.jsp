<%@page import="eduportal.dao.*"%>
<%@page import="eduportal.dao.entity.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Orders</title>
</head>
<body>
	<table>
		<tr>
			<td>ID</td>
			<td>Title</td>
			<td>Description</td>
			<td>Name, surname</td>
			<td>Edit</td>
		</tr>
		<%
			for (Order ord : OrderDAO.getAllOrders()) {
		%>
		<tr>
			<td><%=ord.getId()%></td>
			<td><%=ord.getProduct().getTitle()%></td>
			<td><%=ord.getProduct().getDescription()%></td>
			<td><%=ord.getUser().getName() + " " + ord.getUser().getSurname()%>
			</td>
			<td><a href=edit.jsp?id=<%=ord.getId()%>>Edit</a></td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>