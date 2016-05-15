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
	var orderList;
	$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/order/v1/allOrders',
		data: tokenJson,
		success: function(resData) { 
			for (var i = 0; i < resData.items.length; i++)
			{
				$('#table-orders').append("<tr class='tr-order' id='tr-order-" + (i + 1) + "' >" +
						"<td>" + (i + 1) + "</td>" +
						"<td>" + resData.items[i].id + "</td>" +
						"<td>" + resData.items[i].clientName + "</td>" +
						"<td>" + resData.items[i].productName + "</td>" +
						"<td>" + resData.items[i].creatorName + "</td>" +
						"<td>" + checkBool(resData.items[i].donePaid) + "</td>" +
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
	});
	
	$('#table-orders').on("click", "tr", function() {
		var rowIndex = parseInt( $(this).index() ) - 1;
		$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/order/v1/allOrders',
		data: tokenJson,
		success: function(resData) { 
			$('label[for="order-id"]').html("ID заказа: " + resData.items[rowIndex].id);	
			$('#opacity').css('display', 'block');
			$('#order-edit-form').css('display', 'block');
		},
		});	
	});
	
});

function checkBool(data)
{
	if (data = true)
		return "Да";
	else return "Нет";
};

