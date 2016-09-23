<%@page import="eduportal.model.*"%>
<%@page import="eduportal.dao.entity.*"%>
<%@page import="eduportal.dao.*"%>
<%@page import="eduportal.api.*"%>
<%@page import="eduportal.util.*"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	Employee empl = null;
	String sesToken = null;
	String mainToken = null;
	String target = request.getParameter("target");
	for (Cookie c : request.getCookies()) {
		if (c.getName().equals("sesToken")) {
			sesToken = c.getValue();
		}
		if (c.getName().equals("mainToken")) {
			mainToken = c.getValue();
		}
	}
	if (target != null) {
		Employee emp = AuthContainer.getEmp(sesToken);
		UserEntity user = UserDAO.get(target);
		if (user != null) {
			AuthToken tok = AuthContainer.requireAuth(emp, user);
			if (tok != null) {
				out.print("+" + tok.getSessionId());
			} else {
				out.print("unexpected");
			}
		} else {
			out.print("usernotfound");
		}
	} else if (mainToken != null) {
		out.print("deprecated");
	}
%>