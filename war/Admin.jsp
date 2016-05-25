<%@page import="eduportal.model.AccessLogic"%>
<%@page import="eduportal.dao.entity.Corporation"%>
<%@page import="eduportal.dao.UserDAO"%>
<%@page import="eduportal.api.UserAPI"%>
<%@page import="eduportal.model.AuthContainer"%>
<%@page import="eduportal.dao.entity.UserEntity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Company:</h1>
	<br>
	<table>
		<%
			for (Corporation comp : UserDAO.getCorpList()) {
		%>
		<tr>
			<td><%=comp.getName()%></td>
			<td><%=comp.getId()%></td>
			<td><%=comp.getOwner().getName() + " " + comp.getOwner().getSurname()%></td>
			<td><a href="edit.jsp?corp=<%=comp.getId()%>">Edit</a></td>
		</tr>
		<%
			}
		%>
	</table>
	<h1></h1>
	<br>
	<h1></h1>
	<br>
	<h1></h1>
	<br>

</body>
</html>