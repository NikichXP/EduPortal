<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cookiez</title>
</head>
<body>

	<table>
	<tr>
	<td>Name</td>
	<td>Value</td>
	</tr>
	<%
	String token = request.getParameter("token");
	
	
		for (Cookie c : request.getCookies()) {
			out.println("<tr><td>" + c.getName() + "</td><td>" + c.getValue() + "</td></tr>");
		
			if (c.getName().equals("sesToken")) {
				Cookie killMyCookie = new Cookie("sesToken", null);
				killMyCookie.setMaxAge(0);
				killMyCookie.setPath("/");
				response.addCookie(killMyCookie);
			}
		}
		
		if (token != null) {
			response.addCookie(new Cookie("sesToken", token));
		}
	%>
	
	</table>
	TOKEN CHANGED
</body>
</html>