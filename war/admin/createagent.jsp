<%@page import="eduportal.model.AccessSettings"%>
<%@page import="eduportal.dao.*"%>
<%@page import="eduportal.dao.entity.*;"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form action="createagent.jsp">
		<input type="text" name="name" value="New company name"><br>
		<input type="text" name="mail" value="Owner mail"><br> <input
			type="submit">
	</form>
	<a href = "admin.jsp">Go back</a>

	<%
		if (request.getParameter("name") == null || request.getParameter("mail") == null) {
			out.print("</body>	</html>");
			return;
		}
	String mail = request.getParameter("mail");
	UserEntity u = UserDAO.getUserByMail(mail);
	if (u == null) {
		out.print("</body>	</html>");
		return;
	}
	Corporation corp = new Corporation(request.getParameter("name"));
	corp.setOwner(u);
	u.defineCorporation(corp);
	u.setAccessLevel(AccessSettings.ADMIN_LEVEL);
	UserDAO.createCorp(corp);
	UserDAO.update(u);
	%>
</body>
</html>