<%@page import="eduportal.dao.UserDAO"%>
<%@page import="eduportal.dao.entity.Corporation"%>
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
		Corporation corp = UserDAO.getCorp(request.getParameter("corp"));
	%>
	<h1>
		<%
			if (corp != null) {
				out.print(corp.getName());
			} else {
				out.print("New corp");
				corp = new Corporation();
			}
		%>
	</h1>

	<form action="edit.jsp">
		<input type="text" name="Name" value="<%= corp.getName() %>">
		
	</form>
</body>
</html>