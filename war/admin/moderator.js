$(function(){
	var count;	
	var $resDiv = $('#div-right');
	var dateObj = new Date();
	
	var authSesTO = getCookie("sesTO");

	var ordersList = {};
	 
    
	
	if (getCookie("sesToken") != null)
		if(getCookie("sesTO") - dateObj < 0)
			window.location = "../auth.html";

	//check if session ok every 60 secs
	setInterval(function() 
	{ 
		if (getCookie("sesToken") == null) window.location = "../auth.html";
		else 
			$.ajax({
				type: 'GET',
				url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/checkToken',
				data: { token: getCookie("sesToken")},
				success: function(resData) { 
					if (resData.value == 'false') window.location = "../auth.html";
				},
			});		
	}, 60000);
    
    $.ajax({
		type: 'GET',
		url: 'https://eduportal-1277.appspot.com/_ah/api/moderator/v1/listProducts',
		data: { token: getCookie("sesToken")},
		success: function(resData) { 
			for (var i = 0; i < resData.items.length; i++) {
				$('.table-products').append("<tr>" +
						"<td>" + resData.items[i].id + "</td>" +
						"<td>" + resData.items[i].title + "</td>" +
                        "<td>" + resData.items[i].description + "</td>" +
                        "<td>" + resData.items[i].city.cyrname + "</td>" +
                        "<td>" + resData.items[i].start + "</td>" +
                        "<td>" + resData.items[i].defaultPrice + "</td>" +
					"</tr>");	
			};
		},
	});	
    
    $.ajax({
		type: 'GET',
		url: 'https://eduportal-1277.appspot.com/_ah/api/moderator/v1/myClients',
		data: { token: getCookie("sesToken")},
		success: function(resData) { 
			for (var i = 0; i < resData.items.length; i++) {
				$('.table-clients').append("<tr>" +
						"<td>" + resData.items[i].name + " " + resData.items[i].surname + "</td>" +
						"<td>" + resData.items[i].mail + "</td>" +
                        "<td>мда</td>" +
                        "<td><button value='" + resData.items[i].id + "' class='reset-pass btn btn-default'>RESET</button></td>" +
						"<td><button value='" + resData.items[i].id + "' class='reset-pass btn btn-default'>RESET</button></td>" +
					"</tr>");	
			};
		},
	});
    
    $.ajax({
		type: 'GET',
		url: 'https://eduportal-1277.appspot.com/_ah/api/moderator/v1/myOrders',
		data: { token: getCookie("sesToken")},
		success: function(resData) { 
			for (var i = 0; i < resData.items.length; i++) {
				$('.table-orders').append("<tr>" +
                        "<td>" + resData.items[i].id + "</td>" +
						"<td>" + resData.items[i].client.name + " " + resData.items[i].client.surname + "</td>" +
						"<td>" + resData.items[i].productName + "</td>" +
                        "<td>" + resData.items[i].creatorName + "</td>" +
                        "<td>" + resData.items[i].price + "</td>" +
						"<td>" + resData.items[i].paid + "</td>" +
					"</tr>");	
			};
		},
	});

    
    
    
    $('body').on("click", "#button-back", function() {
		window.location.href = '../workspace.html';
	});
    
    $('body').on("click", "#toggle-products", function() {
		$(".table-products").toggle();
        $(".menu-products").toggle();
	});
    
    $('body').on("click", "#toggle-clients", function() {
		$(".table-clients").toggle();
	});
    
    $('body').on("click", "#toggle-orders", function() {
		$(".table-orders").toggle();
	});
    
    $('body').on("click", "#add-product", function() {
        $.ajax({
            type: 'GET',
            url: 'https://eduportal-1277.appspot.com/_ah/api/moderator/v1/product/add',
    		data: { 
                token: getCookie("sesToken"), 
                cityname: $("#product-city").val(), 
                title: $("#product-name").val(), 
                description: $("#product-desc").val(), 
                price: $("#product-price").val(),
                begin: $("#product-date").val()
            },
            success: function(resData) { 
                //window.location.reload();
            },
        });		
	});
	
    
    

	
});

function returnAccessLevel(a) {
    
    switch (a) {
        case 10: return 'Модератор'; break;
        case 0: return 'Агент'; break;
        case 47821: return 'Администратор'; break;
    }
    
    return 0;
    
}

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

