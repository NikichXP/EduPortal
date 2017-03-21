$(function(){
	var count;	
	var $resDiv = $('#div-right');
	var dateObj = new Date();
	
	var authSesTO = getCookie("sesTO");

	var ordersList = {};
		
	
	if (getCookie("sesToken") != null)
		if(getCookie("sesTO") - dateObj < 0)
			window.location = "auth.html";
    
    if (getCookie("sesToken") == null) window.location = "auth.html";

	//check if session ok every 60 secs
	setInterval(function() 
	{ 
		if (getCookie("sesToken") == null) window.location = "auth.html";
		else 
			$.ajax({
				type: 'GET',
				url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/checkToken',
				data: { token: getCookie("sesToken")},
				success: function(resData) { 
					if (resData.value == 'false') window.location = "auth.html";
				},
			});		
	}, 60000);

	if (getCookie("accessLevel") == 'MODERATOR')
	{
		$('#ul-side-menu').append('<li id="menu-mod">Меню модератора</li>');
	}

	if (getCookie("accessLevel") == 'ADMIN')
	{
		$('#ul-side-menu').append('<li id="menu-adm">Меню администратора</li>');
		$('#ul-side-menu').append('<li id="menu-mod">Меню модератора</li>');		
	}

	if (getCookie("accessLevel") == 'AGENT')
	{
		$('#ul-side-menu').append('<li id="menu-mod">Меню модератора</li>');
		$('#order-payment-edit').css('display', 'none');
	}


	if (getCookie("accessLevel") == 'USER')
	{
		$('li#menu-client-open').toggle();
		//$('li#menu-create-order').toggle();
		$('li#menu-product-page').toggle();
		$('div#order-payment-edit').css('display', 'none');
		//$('#ul-side-menu').append('<li id="menu-mod">Меню модератора</li>');
	}
    
    




	$('#menu-adm').on('click', function(){
		window.location = "admin/admin.html";
	});

	$('#menu-mod').on('click', function(){
		window.location = "admin/moderator.html";
	});
	


	//get user name	
	$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/getname',
		data: { token: getCookie("sesToken")},
		success: function(resData) {
			$('#p-greeting').append("<h3>Добро пожаловать, " + resData.name + "!</h3>");
		},	
	});	
	//dynamic table of orders
	$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/order/v1/allOrders',
		data: { token: getCookie("sesToken")},
		success: function(resData) { 
			for (var i = 0; i < resData.items.length; i++)
			{
				$('#table-orders').append("<tr class='tr-hover'>" +
						"<td>" + (i + 1) + "</td>" +
						"<td>" + resData.items[i].id + "</td>" +
						"<td>" + resData.items[i].clientName + "</td>" +
						"<td>" + resData.items[i].productName + "</td>" +
						"<td>" + resData.items[i].creatorName + "</td>" +
						"<td>" + resData.items[i].paid + "</td>" +
						"<td>" + resData.items[i].currency + "</td>" +
						"<td>" + checkFiles(resData.items[i]) + "</td>" +
					"</tr>");	
			};
			$('#li-open-orders').append(" " + i);
			var k = 0;
			for (var i = 0; i < resData.items.length; i++)
				if (resData.items[i].donePaid == false) k++;
			$('#li-done-orders').append(" " + k);
		},
	});	
	//debug button to get token	
	$('#menu-showses').on('click', function(){
		$resDiv.append("TokenId = " + getCookie("sesToken") + "; tokenTO = " + getCookie("sesTO") + "; accessLevel = " + getCookie("accessLevel") + ";");	
	});
	//log out
	$('#menu-logout').on('click', function(){
		if (getCookie('sesToken') == getCookie('mainToken')) 
		{
			deleteCookie("sesToken");	
			deleteCookie("mainToken");
			deleteCookie("sesTO");	
			window.location = "auth.html";
		}
		else
		{
			setCookie('mainToken', getCookie('sesToken'));
			location.reload();
		}
		
	});
	//hide order edit block
	$('#opacity').on('click', function(){
		$(this).css('display', 'none');	
		
		$('#order-edit-form').css('display', 'none');
		
		$('#order-edit-payment').css('display', 'none');
		
		$('#client-list').css('display', 'none');
		
		$('#product-list').css('display', 'none');
		
		$('#lists-container').css('display', 'none');
		
		$('#order-create').css('display', 'none');
		
		$('#client-menu').css('display', 'none');
		$('#client-menu').css('height', '322px');
		$('#client-menu-create').html("Добавить клиента");
		
		$('#order-create-file-upload').css('display', 'none');
		$('#order-create-file-upload').html("");

		$('#product-menu').css('display', 'none');
	});

	//hide lists block
	$('#lists-container').on('click', function(){
		$(this).css('display', 'none');	
		$('#client-list').css('display', 'none');
		$('#product-list').css('display', 'none');
		$('#lists-container').css('display', 'none');
	});

	//get client name from table
	$('#table-client-list').on("click", "td.td-client-list", function() {
		var chosenClientId = $(this).children('input.inner-client-input-1').val();
		$('#order-client-name').html($(this).html());
		$('#client-list').css('display', 'none');
		$('#lists-container').css('display', 'none');
		
		$('#input-order-client-id').val(chosenClientId);
	});

	//get product name from table
	$('#table-product-list').on("click", "td.td-product-list", function() {
		var chosenProduct = $(this).html();
		var chosenProductId = $(this).children('input.inner-product-input-1').val();
		var chosenProductPrice = $(this).children('input.inner-product-input-2').val();
		
		$('#order-product-name').html(chosenProduct);
		$('#order-product-price').html(chosenProductPrice);
		$('#input-order-product-id').val(chosenProductId);
		$('#product-list').css('display', 'none');
		$('#lists-container').css('display', 'none');
	});

	//show client names table		
	$('#order-client-name').on('click', function(){
		$('#table-client-list').html("<tbody></tbody>");
		
		$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/getMyClients',
		data: { token: getCookie("sesToken")},
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
					"<td class='td-client-list'>" + resData.items[count].name + " " + resData.items[count].surname + 
					"<input type='hidden' class='inner-client-input-1' value=" + resData.items[count].id + ">" +
					"</td>"
					);
					count++;
					if (count == imax) break;
				}
				$('#table-client-list').append("</tr>");				
			}
			$('#client-list').css('display', 'block');
			$('#lists-container').css('display', 'block');
		},
		});	
	});
	
	//show products table
	$('#order-product-name').on('click', function(){
		$('#table-product-list').html("<tbody></tbody>");
		
		$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/order/v1/allProducts',
		data: { token: getCookie("sesToken")},
		success: function(resData) {
			var imax = resData.items.length;
			var count = 0;
			for (var i = 0; i < resData.items.length; i++)
			{
				if (count == imax) break;
				$('#table-product-list tbody').append("<tr id='tr-product-list-" + (i + 1) + "'>");
				for (var j = 0; j < 5; j++)
				{
					$('#tr-product-list-' + (i + 1)).append(
					"<td class='td-product-list'>" + 
					resData.items[count].title + 
					"<input type='hidden' class='inner-product-input-1' value=" + resData.items[count].id + ">" +
					"<input type='hidden' class='inner-product-input-2' value=" + resData.items[count].defaultPrice + ">" 
					+ "</td>"
					);
					count++;
					if (count == imax) break;
				}
				$('#table-product-list tbody').append("</tr>");				
			}
			$('#product-list').css('display', 'block');
			$('#lists-container').css('display', 'block');
		},
		});	
	});
	
	//open payment edit menu
	$('#table-orders').on("click", "tr.tr-hover", function() {
		
		var rowIndex = $(this).index() - 1;
		
		$('#table-order-payment').html("<tbody>" + 
			"<tr id='table-order-payment-header'>" + 
			"<td>Клиент</td>" + 
			"<td>Продукт</td>" +
			"<td>Стоимость</td>" +
			"<td>Внесенная оплата</td>" +
			"<td>Осталось</td>" +
			"<td>Файлы</td>" +
		"</tbody>");
		
		$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/order/v1/allOrders',
		data: { token: getCookie("sesToken")},
		success: function(resData) { 
			$('#table-order-payment').html("<tbody></tbody>");
			$('#order-payment-text-block').html("<H2>Редактирование заказа #" + resData.items[rowIndex].id + "</H2>");
			
			$('#table-order-payment tbody').append("<tr>" + 
				"<td>" + resData.items[rowIndex].clientName + "</td>" + 
				"<td>" + resData.items[rowIndex].productName + "</td>" +
				"<td>" + resData.items[rowIndex].price + "</td>" + 
				"<td>" + resData.items[rowIndex].paid + "</td>" + 
				"<td>" + (resData.items[rowIndex].price - resData.items[rowIndex].paid) + "</td>" + 
				"<td>" + checkFiles(resData.items[rowIndex]) + "</td>" + 
			+ "</tr>");
            
            $('.file-list').html("");
            
            for (var i = 0; i < resData.items[rowIndex].files.length; i++) {
                $('.file-list').append('<li id=' + resData.items[rowIndex].files[i].id + '>' + (i + 1) + '</li>');    
            }
			
			$('#input-order-Id').val(resData.items[rowIndex].id);
			$('#opacity').css('display', 'block');
			$('#order-edit-payment').css('display', 'block');
		},
		});	
	});
    
    //dload file
    $('body').on("click", ".file-list li", function() {
        var shittyURL = 'https://eduportal-1277.appspot.com/FileProcessorServlet?key=' + $(this).attr('id');
        window.open(shittyURL, '_blank');
		
	});
    
    
	//Send order payment
	$('#order-edit-send').on('click', function(){

		if ($('#input-paid').val() != "")	
			var payment = Math.round($('#input-paid').val() * 100) / 100;
		else var payment = 0;
		
		var paymentData = {
			token: getCookie("sesToken"),
			orderid: $('#input-order-Id').val(),
			paid: payment,
		};
		
		$.ajax({
			type: 'GET',
			url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/order/v1/editorder',
			data: paymentData,
			success: location.reload(),	
		});		

	});
	
	//Add order files 
	$('#order-edit-file').on('click', function(){
	
		var url = "admin/file.jsp?order=" + $('#input-order-Id').val() + "&token=" + getCookie("sesToken");
		var windowName = "File Upload";
		var windowSize = ["width=500, height=500"];
		window.open(url, windowName, windowSize);
		event.preventDefault();

	});
	
	//Delete order
	$('#order-edit-del').on('click', function(){

		var delData = {
			token: getCookie("sesToken"),
			orderid: $('#input-order-Id').val(),
		};
		
		$.ajax({
			type: 'GET',
			url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/order/v1/cancelOrder',
			data: delData,
			success: location.reload(),	
		});		

	});
	
	//open order creation menu
	$('#menu-create-order').on('click', function() {
		if (getCookie('accessLevel') == 'USER')
		{
			$.ajax({
				type: 'GET',
				url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/getInfo',
				data: {'token' : getCookie("sesToken")},
				success: function(resData) {
					if (resData.active)
					{
						var url = "order-create-table.html?token=" + getCookie("sesToken");
						var windowName = "Order creation";
						var windowSize = ["width=450, height=800"];
						window.open(url, windowName, windowSize);
						event.preventDefault();
					}
					else
						alert("Вы должны заполнить все поля в профиле чтоб оформить заказ!");
				}
			});
		}
		else 
		{
			var url = "order-create-table.html?token=" + getCookie("sesToken");
			var windowName = "Order creation";
			var windowSize = ["width=450, height=900"];
			window.open(url, windowName, windowSize);
			event.preventDefault();
		}		
	});
	
	//open client menu
	$('#menu-client-open').on("click", function() {
		//$("#client-menu").load("client-create-table.html");
		$('#table-client-menu').html("<tbody></tbody>");
		$('#client-menu-text-block').html("<H2>Мои клиенты</H2>"); 
		//$('#client-new').css('display', 'none');
		
		$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/getMyClients',
		data: { token: getCookie("sesToken")},
		success: function(resData) {
			if (resData.items)
			{
				$('#table-client-menu').html("<tbody></tbody>");
				var imax = resData.items.length;
				var count = 0;
				for (var i = 0; i < resData.items.length; i++)
				{
					if (count == imax) break;
					$('#table-client-menu tbody').append("<tr id='tr-client-menu-" + (i + 1) + "'>");
					for (var j = 0; j < 5; j++)
					{
						$('#tr-client-menu-' + (i + 1)).append(
						"<td class='td-client-menu'>" + resData.items[count].name + " " + resData.items[count].surname + 
						"<input type='hidden' class='inner-client-menu-input-1' value=" + resData.items[count].id + ">" +
						"</td>"
						);
						count++;
						if (count == imax) break;
					}
					$('#table-client-menu').append("</tr>");				
				}
			};
			$('#client-menu').css('display', 'block');
			$('#opacity').css('display', 'block');
		},
		});	
	}); 
	
	//client creation menu
	$('#client-menu-create').on('click', function() {

		var url = "client-create-table.html?token=" + getCookie("sesToken");
		var windowName = "User creation";
		var windowSize = ["width=520, height=900"];
		window.open(url, windowName, windowSize);
		event.preventDefault();
	});

	//open profile
	$('#table-client-menu').on("click", "td.td-client-menu", function() {

		setCookie('clientID', $(this).children('input.inner-client-menu-input-1').val());
		var url = "user-profile.html?token=" + getCookie("sesToken");
		var windowName = "User profile";
		var windowSize = ["width=520, height=900"];
		window.open(url, windowName, windowSize);
		event.preventDefault();
	});
	//open my frofile
	$('#menu-showprof').on("click", function() {

		deleteCookie('clientID');
		var url = "user-profile.html?token=" + getCookie("sesToken");
		var windowName = "User profile";
		var windowSize = ["width=520, height=900"];
		window.open(url, windowName, windowSize);
		event.preventDefault();
	});
	//cancel client creation
	/* $('#client-menu-dismiss').on('click', function() {

		$('#client-new').css('display', 'none');
		$('#client-menu-create').html("Добавить клиента");
		$('#client-menu').css('height', '322px');	
		$('#client-menu-dismiss').css('display', 'none');		
	}); */

	//open product menu
	$('#menu-product-page').on("click", function() {
		//$("#client-menu").load("client-create-table.html");
		$('#table-product-menu').html("<tbody></tbody>");
		$('#product-menu-text-block').html("<H2>Продукты</H2>"); 
		//$('#client-new').css('display', 'none');
		
		$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/order/v1/products',
		data: { token: getCookie("sesToken")},
		success: function(resData) {
			var imax = resData.items.length;
			var count = 0;
			$('#table-product-menu').html("<tbody></tbody>");
			for (var i = 0; i < resData.items.length; i++)
			{
				if (count == imax) break;
				$('#table-product-menu tbody').append("<tr id='tr-product-menu-" + (i + 1) + "'>");
				for (var j = 0; j < 5; j++)
				{
					$('#tr-product-menu-' + (i + 1)).append(
					"<td class='td-product-menu'>" + resData.items[count].title +  
					"<input type='hidden' class='inner-product-menu-input-1' value=" + resData.items[count].id + ">" +
					"</td>"
					);
					count++;
					if (count == imax) break;
				}
				$('#table-product-menu').append("</tr>");				
			}
			$('#product-menu').css('display', 'block');
			$('#opacity').css('display', 'block');
		},
		});	
	}); 

	//open product reate
	$('#product-menu-create').on("click", function() {

		var url = "admin/createproduct.jsp?token=" + getCookie("sesToken");
		var windowName = "Product creation";
		var windowSize = ["width=520, height=800"];
		window.open(url, windowName, windowSize);
		event.preventDefault();
	});

	//product profile menu
	$('#table-product-menu').on("click", "td.td-product-menu", function() {

		setCookie('productID', $(this).children('input.inner-product-menu-input-1').val());
		var url = "product-profile.html?token=" + getCookie("sesToken");
		var windowName = "Product creation";
		var windowSize = ["width=520, height=800"];
		window.open(url, windowName, windowSize);
		event.preventDefault();
	});

	
});

function checkBool(data)
{
	if (data == true)
		return "Да";
	else return "Нет";
};

function checkFiles(data)
{
	if (data.files == null) return 0;
	else return data.files.length;
};

