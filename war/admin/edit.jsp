<%@page import="eduportal.dao.*"%>
<%@page import="eduportal.dao.entity.*"%>
<%@page import="com.googlecode.objectify.*"%>
<%@page import="java.util.*;"%>

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
		<%=corp.getName()%>
	</h1>

	<form action="edit.jsp">
		Corporation name <input type="text" name="Name"
			value="<%=corp.getName()%>"> <br> Select new admin: <select
			name="owner">
			<%
				String ownerid = corp.getOwner().getId();
				List<UserEntity> users = ObjectifyService.ofy().load().type(UserEntity.class).list();
				for (UserEntity user : UserDAO.getCorpEmployees(corp.getName())) {
					out.println("<option " + ((user.getId() == ownerid) ? " selected " : "") + " value=\"" + user.getId()
							+ "\">" + user.getName() + " " + user.getSurname() + "</option>");

				}
			%>
		</select> <br> <input type="hidden" name="corp"
			value="<%=request.getParameter("corp")%>"> <input
			type="submit" value = "GO">
	</form>
</body>
</html>