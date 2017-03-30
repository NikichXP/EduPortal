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
				url: apiPath + 'user/checkToken',
				data: { token: getCookie("sesToken")},
				success: function(resData) { 
					if (resData.value == 'false') window.location = "../auth.html";
				},
			});		
	}, 60000);
    
    $.ajax({
		type: 'GET',
		url: apiPath + 'moderator/listProducts',
		data: { token: getCookie("sesToken")},
		success: function(resData) { 
			for (var i = 0; i < resData.length; i++) {
				$('.table-products').append("<tr>" +
						"<td>" + resData[i].id + "</td>" +
						"<td>" + resData[i].title + "</td>" +
                        "<td>" + resData[i].description + "</td>" +
                        "<td>" + resData[i].city.cyrname + "</td>" +
                        "<td>" + resData[i].start + "</td>" +
                        "<td>" + resData[i].defaultPrice + "</td>" +
					"</tr>");	
			};
		},
	});	
    
    $.ajax({
		type: 'GET',
		url: apiPath + 'moderator/myClients',
		data: { token: getCookie("sesToken")},
		success: function(resData) { 
			for (var i = 0; i < resData.length; i++) {
				$('.table-clients').append("<tr>" +
						"<td>" + resData[i].name + " " + resData[i].surname + "</td>" +
						"<td>" + resData[i].mail + "</td>" +
                        "<td>мда</td>" +
                        "<td><button value='" + resData[i].id + "' class='reset-pass btn btn-default'>RESET</button></td>" +
						"<td><button value='" + resData[i].id + "' class='reset-pass btn btn-default'>RESET</button></td>" +
					"</tr>");	
			};
		},
	});
    
    $.ajax({
		type: 'GET',
		url: apiPath + 'moderator/myOrders',
		data: { token: getCookie("sesToken")},
		success: function(resData) { 
			for (var i = 0; i < resData.length; i++) {
				$('.table-orders').append("<tr>" +
                        "<td>" + resData[i].id + "</td>" +
						"<td>" + resData[i].client.name + " " + resData[i].client.surname + "</td>" +
						"<td>" + resData[i].productName + "</td>" +
                        "<td>" + resData[i].creatorName + "</td>" +
                        "<td>" + resData[i].price + "</td>" +
						"<td>" + resData[i].paid + "</td>" +
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
            url: apiPath + 'moderator/product/add',
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

