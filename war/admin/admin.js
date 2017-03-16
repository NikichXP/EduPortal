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

	
    

	$.ajax({
		type: 'GET',
		url: 'https://eduportal-1277.appspot.com/_ah/api/admin/v1/myEmployees',
		data: { token: getCookie("sesToken")},
		success: function(resData) { 
			for (var i = 0; i < resData.items.length; i++) {
				$('.table-emp').append("<tr>" +
						"<td>" + resData.items[i].name + " " + resData.items[i].surname + "</td>" +
						"<td>" + resData.items[i].mail + "</td>" +
                        "<td>где</td>" +
						"<td>" + returnAccessLevel(resData.items[i].accessLevel) + "</td>" +
					"</tr>");	
			};
		},
	});	
    
    $.ajax({
		type: 'GET',
		url: 'https://eduportal-1277.appspot.com/_ah/api/admin/v1/myClients',
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
		url: 'https://eduportal-1277.appspot.com/_ah/api/admin/v1/inactiveClients',
		data: { token: getCookie("sesToken")},
		success: function(resData) { 
			for (var i = 0; i < resData.items.length; i++) {
				$('.table-inactive').append("<tr>" +
						"<td>" + resData.items[i].name + " " + resData.items[i].surname + "</td>" +
						"<td>" + resData.items[i].creator + "</td>" +
                        "<td>мда</td>" +
                        "<td><button value='" + resData.items[i].id + "' class='user-fields btn btn-default'>RESET</button></td>" +
					"</tr>");	
			};
		},
	});
    
    $.ajax({
		type: 'GET',
		url: 'https://eduportal-1277.appspot.com/_ah/api/admin/v1/cityList',
//		data: { token: getCookie("sesToken")},
		success: function(resData) { 
			for (var i = 0; i < resData.items.length; i++) {
				$('.table-city').append("<tr>" +
						"<td>" + resData.items[i].id + "</td>" +
						"<td>" + resData.items[i].cyrname + "</td>" +
                        "<td>" + resData.items[i].name + "</td>" +
                        "<td>" + resData.items[i].country.cyrname + "</td>" +
                        "<td>" + resData.items[i].country.name + "</td>" +
					"</tr>");	
			};
		},
	});	
    
    
    
    
    $('body').on("click", "#button-back", function() {
		window.location.href='../workspace.html';
	});
    
    $('body').on("click", "#toggle-emp", function() {
		$(".table-emp").toggle();
	});
    
    $('body').on("click", "#toggle-clients", function() {
		$(".table-clients").toggle();
	});
    
    $('body').on("click", "#toggle-inactive", function() {
		$(".table-inactive").toggle();
	});
    
    $('body').on("click", "#toggle-city", function() {
		$(".table-city").toggle();
        $(".menu-city").toggle();
	});
    
    $('body').on("click", "#add-city", function() {
        $.ajax({
            type: 'GET',
            url: 'https://eduportal-1277.appspot.com/_ah/api/admin/v1/createcity',
    		data: { token: getCookie("sesToken"), city: $("#city-name").val(), country: $("#city-country").val() },
            success: function(resData) { 
                window.location.reload();
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

