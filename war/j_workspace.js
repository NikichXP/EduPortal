$(function(){
	var count;	
	var $resDiv = $('#div-right');
	var dateObj = new Date();
	
	var authSesToken = getCookie("sesToken");
	var authSesTO = getCookie("sesTO");
	
	if (authSesToken != null)
		if(authSesTO - dateObj < 0)
			window.location = "auth.html";
	
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
				$('#table-orders').append("<tr>" +
						"<td>" + i + "</td>" +
						"<td>" + resData.items[i].id + "</td>" +
						"<td>" + resData.items[i].user.name + " " + resData.items[i].user.surname + "</td>" +
						"<td>" + resData.items[i].product.title + "</td>" +
						"<td>" + resData.items[i].createdBy.name + " " + resData.items[i].createdBy.surname + "</td>" +
						"<td>" + checkBool(resData.items[i].donePaid) + "</td>" +
					"</tr>");	
				countOrders = i + 1;
			}
			$('#li-open-orders').append(" " + count)
			
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
	
});

function checkBool(data)
{
	if (data = true)
		return "Да";
	else return "Нет";
}
