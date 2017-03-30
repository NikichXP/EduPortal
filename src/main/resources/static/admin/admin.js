$(function(){
	var count;	
	var $resDiv = $('#div-right');
	var dateObj = new Date();
	
	var authSesTO = getCookie("sesTO");

	var ordersList = {};
	
    
	
	if (getCookie("sesToken") != null)
		if(getCookie("sesTO") - dateObj < 0)
			window.location = "/auth.html";

	//check if session ok every 60 secs
	setInterval(function() 
	{ 
		if (getCookie("sesToken") == null) window.location = "/auth.html";
		else 
			$.ajax({
				type: 'GET',
				url: apiPath + 'user/checkToken',
				data: { token: getCookie("sesToken")},
				success: function(resData) { 
					if (resData.value == 'false') window.location = "/auth.html";
				},
			});		
	}, 60000);

	
    

	$.ajax({
		type: 'GET',
		url: apiPath + 'admin/myEmployees',
		data: { token: getCookie("sesToken")},
		success: function(resData) { 
			for (var i = 0; i < resData.length; i++) {
				$('.table-emp').append("<tr>" +
						"<td>" + resData[i].name + " " + resData[i].surname + "</td>" +
						"<td>" + resData[i].mail + "</td>" +
                        "<td>где</td>" +
						"<td>" + returnAccessLevel(resData[i].accessLevel) + "</td>" +
					"</tr>");	
			};
		},
	});	
    
    $.ajax({
		type: 'GET',
		url: apiPath + 'admin/myClients',
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
		url: apiPath + 'admin/inactiveClients',
		data: { token: getCookie("sesToken")},
		success: function(resData) { 
			for (var i = 0; i < resData.length; i++) {
				$('.table-inactive').append("<tr>" +
						"<td>" + resData[i].name + " " + resData[i].surname + "</td>" +
						"<td>" + resData[i].creator + "</td>" +
                        "<td>мда</td>" +
                        "<td><button value='" + resData[i].id + "' class='user-fields btn btn-default'>RESET</button></td>" +
					"</tr>");	
			};
		},
	});
    
    $.ajax({
		type: 'GET',
		url: apiPath + 'admin/cityList',
//		data: { token: getCookie("sesToken")},
		success: function(resData) { 
			for (var i = 0; i < resData.length; i++) {
				$('.table-city').append("<tr>" +
						"<td>" + resData[i].id + "</td>" +
						"<td>" + resData[i].cyrname + "</td>" +
                        "<td>" + resData[i].name + "</td>" +
                        "<td>" + resData[i].country.cyrname + "</td>" +
                        "<td>" + resData[i].country.name + "</td>" +
					"</tr>");	
			};
		},
	});	
    
     
    
    
    $('body').on("click", "#button-back", function() {
		window.location.href='../workspace.html';
	});
    
    $('body').on("click", "#button-mod", function() {
		window.location.href='moderator.html';
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
            url: apiPath + 'admin/createcity',
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

