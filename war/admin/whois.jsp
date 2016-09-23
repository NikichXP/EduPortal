<%@page import="eduportal.model.*"%>
<%@page import="eduportal.dao.entity.*"%>
<%@ page language="java" contentType="text/plain; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	Employee user = null;
	String token = "";
	for (Cookie c : request.getCookies()) {
		if (c.getName().equals("sesToken")) {
			token = c.getValue();
		}
	}
	user = AuthContainer.getEmp(token);
%>
<%= "sesToken:" + token + "   " + user.toString() %>