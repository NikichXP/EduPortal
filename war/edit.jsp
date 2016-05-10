<%@page import="eduportal.dao.*"%>
<%@page import="eduportal.dao.entity.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit order</title>
</head>
<body>
	<%! Order order;%>
	<% order = OrderDAO.getOrder(request.getParameter("id")); %>

	<form action="edit2.jsp">
		
		Order #<%= order.getIdHexString() %><br>
		Paid: <input type="text" name="one" value = "<%= order.getPaid() %>"> of <%= order.getPrice() %><br>
		

		<p>
			<input type="submit">
		</p>
	</form>
</body>
</html>