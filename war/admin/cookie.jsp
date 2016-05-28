<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		String token = request.getParameter("token");
		for (Cookie c : request.getCookies()) {
			out.println(c.getName() + " " + c.getValue());
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
</body>
</html>