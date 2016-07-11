<%@page import="eduportal.model.*"%>
<%@page import="eduportal.dao.entity.*"%>
<%@page import="eduportal.dao.*"%>
<%@page import="eduportal.api.UserAPI"%>
<%@page import="java.util.*"%>
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
	<%
		UserEntity user = null;
		for (Cookie c : request.getCookies()) {
			if (c.getName().equals("sesToken")) {
				user = AuthContainer.getUser(c.getValue());
			}
		}
		if (user == null) {
			return;
		} else if (((Employee)user).getAccessLevel() < 10 || ((Employee)user).getCorporation().equals(AccessSettings.OWNERCORP_NAME)) {
			return;
		}
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		City c = null;
		Country ct = null;
		if (type.equals("city")) {
			c = GeoDAO.getCityById(Long.parseLong(id)); 
		} else {
			ct = GeoDAO.getCountryById(Long.parseLong(id));
		}
		if (request.getParameter("cyr") != null) {
			if (type.equals("city")) {
				c.setCyrname(request.getParameter("cyr"));
				c.setName(request.getParameter("lat"));
				GeoDAO.saveCity(c);
			} else {
				ct.setCyrname(request.getParameter("cyr"));
				ct.setName(request.getParameter("lat"));
				GeoDAO.saveCountry(ct);
			}
		}
		String cyr = ((c != null) ? c.getCyrname() : ct.getCyrname());
		String lat = ((c != null) ? c.getName() : ct.getName());
	%>
	<form action = "geoedit.jsp">
	<table>
	<tr>
	<td>Имя кириллицей</td>
	<td><input type="text" name = "cyr" value="<%=cyr %>"></td>
	</tr>
	<tr>
	<td>Имя латиницей</td>
	<td><input type="text" name = "lat" value="<%=lat %>"></td>
	</tr>
	</table>
	<input type="hidden" name = "id" value = "<%=id%>">
	<input type="hidden" name = "type" value = "<%=type%>">
	<input type="submit" value= "edit">
	</form>
	<a href="/admin/admin.jsp">Назад</a>
</body> 
</html>