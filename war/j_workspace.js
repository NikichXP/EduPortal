$(function(){
	var count;	
	var $resDiv = $('#div-right');
	var dateObj = new Date();
	
	var authSesToken = getCookie("sesToken");
	var authSesTO = getCookie("sesTO");
	
	if (authSesToken != null)
		if(authSesTO - dateObj < 0)
			window.location = "auth.html";
	
	var ordersList = {};
	var tokenJson = {
			token: authSesToken,
		};
		
	$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/getname',
		data: tokenJson,
		success: function(resData) {
			$('#p-greeting').append("<h3>Добро пожаловать, " + resData.name + "!</h3>");
		},
		
	});	
	$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/order/v1/allOrders',
		data: tokenJson,
		success: function(resData) { 
			for (var i = 0; i < resData.items.length; i++)
			{
				$('#table-orders').append("<tr class='tr-order'>" +
						"<td>" + (i + 1) + "</td>" +
						"<td>" + resData.items[i].id + "</td>" +
						"<td>" + resData.items[i].clientName + "</td>" +
						"<td>" + resData.items[i].productName + "</td>" +
						"<td>" + resData.items[i].creatorName + "</td>" +
						"<td>" + checkBool(resData.items[i].donePaid) + "</td>" +
						"<td class='td-order-edit'>Edit</td>" +
					"</tr>");	
			}
			$('#li-open-orders').append(" " + i)
			
		},
	});	
		
	$('#menu-showses').on('click', function(){
		$resDiv.append("TokenId = " + getCookie("sesToken") + "; tokenTO = " + getCookie("sesTO"));	
	});
	
	$('#menu-logout').on('click', function(){
		deleteCookie("sesToken");	
		deleteCookie("sesTO");	
		window.location = "auth.html";
	});
	
	$('#opacity').on('click', function(){
		$(this).css('display', 'none');	
		$('#order-edit-form').css('display', 'none');
		$('#client-list').css('display', 'none');
	});
	
	$('#table-client-list').on("click", "td.td-client-list", function() {
		$('#order-client-name').html($(this).html())
		$('#client-list').css('display', 'none');
	});
			
	$('#order-client-name').on('click', function(){
		$('#table-client-list').html("<tbody></tbody>");
		
		$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/getMyClients',
		data: tokenJson,
		success: function(resData) {
			var imax = resData.items.length;
			var count = 0;
			for (var i = 0; i < resData.items.length; i++)
			{
				if (count == imax) break;
				$('#table-client-list tbody').append("<tr id='tr-client-list-" + (i + 1) + "'>");
				for (var j = 0; j < 5; j++)
				{
					$('#tr-client-list-' + (i + 1)).append(
					"<td class='td-client-list'>" + resData.items[count].name + " " + resData.items[count].surname + "</td>"
					);
					count++;
				}
				$('#table-client-list').append("</tr>");				
			}
			$('#client-list').css('display', 'block');
		},
		});	
		
	
	});
	
	
	$('#table-orders').on("click", "td.td-order-edit", function() {
		var $tr = $(this).closest('tr');
		var rowIndex = parseInt($tr.index()) - 1;
		$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/order/v1/allOrders',
		data: tokenJson,
		success: function(resData) { 
			$('label[for="order-id"]').html("ID заказа: " + resData.items[rowIndex].id);
			$('#order-client-name').append(resData.items[rowIndex].clientName);	
			$('#order-product-name').val(resData.items[rowIndex].productName);	
			
			if (resData.items[rowIndex].donePaid == true) var radioChecked = "#done-paid-yes";
			else var radioChecked = "#done-paid-no";
			
			$(radioChecked).attr("checked", "checked");
			
			$('#opacity').css('display', 'block');
			$('#order-edit-form').css('display', 'block');
		},
		});	
	});
	
});

function checkBool(data)
{
	if (data == true)
		return "Да";
	else return "Нет";
};

