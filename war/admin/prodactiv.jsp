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
<script type="text/javascript" src="jquery-2.2.3.min.js"></script>
<script type="text/javascript" src="admin.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Product edit page</title>
<link rel='stylesheet' type='text/css' href='s_admin.css' />
</head>
<body>
	<div class="div-form-button">
		<a href=/admin/moderator.jsp>Назад на панель управления</a>
	</div>
	<%
		UserEntity user = null;
		String token = null;
		for (Cookie c : request.getCookies()) {
			if (c.getName().equals("sesToken")) {
				user = AuthContainer.getUser(c.getValue());
				token = c.getValue();
			}
		}
		if (user == null || user.getAccessLevel() < AccessSettings.MODERATOR_LEVEL) {
			out.print("</body></html>");
			return;
		}
		Product product = OrderDAO.getProduct(request.getParameter("id"));
		String val;
		for (Object par : request.getParameterMap().keySet()) {
			val = request.getParameter((String) par);
			switch ((String) par) {
			case "title":
				product.setTitle(val);
				break;
			case "descr":
				product.setDescription(val);
				break;
			case "price":
				try {
					product.setDefaultPrice(Double.parseDouble(val));
				} catch (Exception e) {
					out.println(
							"<script>alert('Вы ввели неверный формат числа цены. Используйте следующий: \"12345.67\"')</script>");
				}
				break;
			case "end":
				if (val.matches("\\d\\d[\\/\\-.]\\d\\d") && val.length() <= 5) {
					product.setEnd(val);
				} else {
					out.println(
							"<script>alert('Вы ввели неверный формат даты окончания. Используйте следующий: \"31.03, 31/03 или 31-03\"')</script>");
				}
				break;
			case "city":
				City c = GeoDAO.getCity(request.getParameter("city"));
				if (c != null) {
					product.setCity(c);
				} else {
					out.println("<script>alert('Город отсутствует в БД')</script>");
				}
				break;
			case "begin":
				if (val.matches("\\d\\d[\\/\\-.]\\d\\d") && val.length() <= 5) {
					product.setStart(val);
				} else {
					out.println(
							"<script>alert('Вы ввели неверный формат даты начала. Используйте следующий: \"31.03, 31/03 или 31-03\"')</script>");
				}
				break;
			case "active":
				if (request.getParameter("active").equals("on")) {
					product.setActual(true);
				} else {
					product.setActual(false);
				}
			default:
				break;
			}
		}
		ProductDAO.save(product);
	%>
	<div id="header">
		<div id="header-main">
			<H1>Администрирование: редактирование продуктов</H1>
		</div>
	</div>

	<form action="prodactiv.jsp">
		<div class='table-div'>
			<input type="hidden" name="id" value="<%=product.getId()%>"
				id="prodid">
			<table>
				<tr>
					<td>Название</td>
					<td><input type="text" name="title" id="title"
						value="<%=product.getTitle()%>"></td>
				</tr>
				<tr>
					<td>Описание</td>
					<td><input type="text" name="descr" id="descr"
						value="<%=product.getDescription()%>"></td>
				</tr>
				<tr>
					<td>Цена</td>
					<td><input type="text" name="price" id="price"
						value="<%=product.getDefaultPrice()%>"></td>
				</tr>
				<tr>
					<td>Дата начала</td>
					<td><input type="text" name="begin" id="begin"
						value="<%=product.getStart()%>"></td>
				</tr>
				<tr>
					<td>Дата окончания</td>
					<td><input type="text" name="end" id="end"
						value="<%=product.getEnd()%>"></td>
				</tr>
				<tr>
					<td>Город проведения</td>
					<td><input type="text" name="city" id="city"
						value="<%=product.getCity().getCyrname()%>"></td>
				</tr>
			</table>
			<input type="checkbox" name="active"
				<%if (product.isActual()) {
				out.print("checked");
			}%>>Актуальный
			<input type="submit" value="Submit changes" id="prodSubmit">
			<%
				if (product.getFiles().size() == 0) {
					out.print("<br>No files attached<br>");
				} else {
					for (SavedFile file : product.getFiles()) {
						out.println(file.toString());
						System.out.println(file);
					}
				}
				if (request.getParameterMap().keySet().size() > 1) {
					out.println(
							"<h3>Примечание: результаты могут обновляются. Если данные не старые, то обновите страницу через "
									+ "пару секунд.</h3>");
				}
			%>
			<a href="file.jsp?product=<%=product.getId()%>&token="<%=token%>">Добавить
				файлы к продукту</a>
		</div>
	</form>

</body>
</html>