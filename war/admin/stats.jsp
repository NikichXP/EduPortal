<%@page import="eduportal.model.*"%>
<%@page import="eduportal.dao.entity.*"%>
<%@page import="eduportal.dao.*"%>
<%@page import="eduportal.util.*"%>
<%@page import="eduportal.api.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.math.*"%>
<%@page import="eduportal.dao.entity.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel='stylesheet' type='text/css' href='s_admin.css' />
<script type="text/javascript" src="jquery-2.2.3.min.js"></script>
<title>Statistics</title>
</head>
<%
	UserAPI u = new UserAPI();
	Employee user = null;
	String token = null;
	for (Cookie c : request.getCookies()) {
		if (c.getName().equals("sesToken")) {
			user = AuthContainer.getEmp(c.getValue());
			token = c.getValue();
		}
	}

	//TODO MODIFY, CLEANUP, OPTIMIZE

	if (user == null) {
		AuthToken t = AuthContainer.authenticate("admin@corp.com", "pass");
		response.addCookie(new Cookie("sesToken", t.getSessionId()));
		user = AuthContainer.getEmp(t.getSessionId());
		token = t.getSessionId();
	}
	if (user == null || AccessLogic.canAccessAdminPanel(user) == false) {
		out.print("<body>Доступ ограничен</body></html>");
		return;
	}
%>
<body>
	<div id='main-div'>
		<div class="div-form-button">
			<a href=/admin/moderator.jsp>Назад на панель управления</a>
		</div>
		<h1>
			Статистика:
			<%=user.getCorporation()%>
		</h1>
		<h2 id='optlist'>
			<a>Программы (press to expand)</a>
		</h2>
		<div class='table-div'>
			<div id='optlisttable'>
				<table class='table-list'>
					<tr class='table-list-header'>
						<td>Название продукта</td>
						<td>Количество заказов</td>
						<td>Оплачено</td>
						<td>Итого</td>
						<td>Долг</td>
					</tr>
					<%
						List<Product> prodlist = OrderDAO.getProductsByCompany(user.getCorporation());
						HashMap<City, Double> cityActual = new HashMap<City, Double>();
						HashMap<City, Double> citySumm = new HashMap<City, Double>();
						DecimalFormat df = new DecimalFormat("#,###,###.##");
						df.setRoundingMode(RoundingMode.CEILING);
						for (Product prod : prodlist) {
							List<Order> orders = OrderDAO.getOrdersByProduct(prod);
							double act = 0.0, summ = 0.0;
							for (Order o : orders) {
								act += (double) o.getPaid();
								summ += (double) o.getPrice();
							}
							if (cityActual.get(prod.getCity()) == null) {
								cityActual.put(prod.getCity(), act);
								citySumm.put(prod.getCity(), summ);
							} else {
								cityActual.put(prod.getCity(), cityActual.get(prod.getCity()) + act);
								citySumm.put(prod.getCity(), citySumm.get(prod.getCity()) + summ);
							}
					%>
					<tr>
						<td><%=prod.getTitle()%></td>
						<td><%=orders.size()%></td>
						<td><%=df.format(act)%></td>
						<td><%=df.format(summ)%></td>
						<td><%=df.format(act - summ)%></td>
					</tr>
					<%
						}
					%>
				</table>
				<script>
			$('#optlisttable').css('display', 'none');
			$('#optlist').on('click', function () {
				if ($('#optlisttable').css('display') == 'block') {
					$('#optlisttable').css('display', 'none');
				} else {
					$('#optlisttable').css('display', 'block');
				}
			})
			</script>
			</div>
			<h2>Статистика по городу/стране</h2>
			<table class='table-list'>
				<tr class='table-list-header'>
					<td>Страна/Город</td>
					<td>Количество заказов</td>
					<td>Оплачено</td>
					<td>Итого</td>
					<td>Долг</td>
				</tr>

				<%
					for (City city : cityActual.keySet()) {
				%>
				<tr class='table-list-selector' id='city<%=city.getName()%>'>
					<td><b><%=city.getName()%></b></td>
					<td>Количество заказов</td>
					<td><b><%=df.format(cityActual.get(city))%></b></td>
					<td><b><%=df.format(citySumm.get(city))%></b></td>
					<td><b><%=df.format(cityActual.get(city) - citySumm.get(city))%></b></td>
				</tr>

				<%
					for (Product prod : prodlist) {
							if (prod.getCity().equals(city)) {
								List<Order> orders = OrderDAO.getOrdersByProduct(prod);
								double act = 0.0, summ = 0.0;
								for (Order o : orders) {
									act += (double) o.getPaid();
									summ += (double) o.getPrice();
								}
				%>
				<tr id='prod<%=prod.getId()%>'>
					<td><%=prod.getTitle()%></td>
					<td><%=orders.size()%></td>
					<td><%=df.format(act)%></td>
					<td><%=df.format(summ)%></td>
					<td><%=df.format(act - summ)%></td>
				</tr>
				<script>
				$('#prod<%=prod.getId() + "'"%>).css('display', 'none');
				</script>
				<%
					}
						}
				%>

				<script>
				var isCity<%=city.getName()%>Visible = false;
				$('#city<%=city.getName() + "'"%>).on('click', function(){
					isCity<%=city.getName()%>Visible = !isCity<%=city.getName()%>Visible;
					
						<%for (Product prod : prodlist) {
					if (prod.getCity().equals(city)) {
						out.print("$('#prod" + prod.getId() + "').css('display', (isCity" + city.getName()
								+ "Visible) ?'table-row' : 'none');");
					}
				}%>
				})
				</script>

				<%
					}
				%>
			</table>
		</div>

		<h1>Provider stats</h1>
		<div class='table-div'>
			<table class='table-list'>
				<tr class='table-list-header'>
					<td>Провайдер</td>
					<td>Количество заказов</td>
					<td>Оплачено</td>
					<td>Итого</td>
					<td>Долг</td>
				</tr>
				<%
				HashMap <String, Double> compPaid = new HashMap();
				HashMap <String, Double> compTotal = new HashMap();
				for (Product prod : prodlist) {
					double summPaid = (compPaid.get(prod.getProvider()) != null) ? compPaid.get(prod.getProvider()) : 0;
					double summTotal = (compTotal.get(prod.getProvider()) != null) ? compTotal.get(prod.getProvider()) : 0;
					for (Order ord : OrderDAO.getOrdersByProduct(prod)) {
						summPaid += ord.getPaid();
						summTotal += ord.getPrice();
					}
					compPaid.put(prod.getProvider(), summPaid);
					compTotal.put(prod.getProvider(), summTotal);
				}
				for (String str : compPaid.keySet()) {
				%>
				<tr>
				<td><%=str %></td>
				<td>%%%</td>
				<td><%=compPaid.get(str) %></td>
				<td><%=compTotal.get(str) %></td>
				<td><%=compPaid.get(str)-compTotal.get(str) %></td>
				</tr>
				<%}%>
			</table>

		</div>
		
		<h1>Agent stats</h1>
		<div class='table-div'>
			<table class='table-list'>
				<tr class='table-list-header'>
					<td>Агент</td>
					<td>Количество заказов</td>
					<td>Сумма на заказы</td>
				</tr>
				<%
				for (Employee emp: UserDAO.getEmployeeList()) {
					List<Order> ordList = OrderDAO.getOrdersByUser(emp);
					double summ = 0;
					for (Order o : ordList) {
						summ += o.getPrice();
					}
				%>
				<tr>
				<td><%=emp %></td>
				<td><%= ordList.size() %></td>
				<td><%= summ %></td>
				</tr>
				<%}%>
			</table>

		</div>

	</div>
</body>
</html>