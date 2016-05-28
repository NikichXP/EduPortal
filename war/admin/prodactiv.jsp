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
<title>Product edit page</title>
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

	<form action="prodactiv.jsp">
		<input type="hidden" name="id" value="<%=product.getId()%>">
		<table>
			<tr>
				<td>Title</td>
				<td><input type="text" name="title"	value="<%=product.getTitle()%>"></td>
			</tr>
			
		</table>
	</form>

</body>
</html>