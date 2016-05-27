$(function(){
			
	//open client menu
	$('#menu-client-open').on("click", function() {
				
		$('#table-client-menu').html("<tbody></tbody>");
		$('#client-menu-text-block').html("<H2>Мои клиенты</H2>");
		$('#client-new').css('display', 'none');
		
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
			$('#client-menu').css('display', 'block');
			$('#opacity').css('display', 'block');
		},
		});	
	});
	
	//client creation menu
	$('#client-menu-create').on('click', function() {
		
		$('#client-menu-create').html("Принять");
		
		if ($('#client-new').css('display') == 'block')
		{
			$('#client-new').css('display', 'none');
			$('#client-menu-create').html("Добавить клиента");
			$('#client-menu').css('height', '322px');
			
			var clData = new FormData();    
			clData.append('token', authSesToken);
			clData.append('mail', $('#client-new-email').val());
			clData.append('name', $('#client-new-name').val());
			clData.append('surname', $('#client-new-surname').val());
			clData.append('phone', $('#client-new-phone').val());
			clData.append('pass', '');
			
			$.ajax({
			type: 'POST',
			url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/createuser',
			data: clData,
			processData: false,
			contentType: false,
			success: location.reload(),	
			});	
		}
		else 
		{
			$('#client-new').css('display', 'block');
			$('#client-menu').css('height', '637px');
			$('#client-menu-dismiss').css('display', 'block');
		}
		
	});
	//cancel client creation
	$('#client-menu-dismiss').on('click', function() {

		$('#client-new').css('display', 'none');
		$('#client-menu-create').html("Добавить клиента");
		$('#client-menu').css('height', '322px');	
		$('#client-menu-dismiss').css('display', 'none');	

		window.close();
	});

	$('#select-client-day').html(" ");
	for (var i = 1; i < 32; i++)
	{
		$('#select-client-day').append("<option id='day-" + i + "'>" + i + "</option>");
	}
	$('#select-client-month').html(" ");
	for (var i = 1; i < 13; i++)
	{
		$('#select-client-month').append("<option id='month-" + i + "'>" + i + "</option>");
	}
	$('#select-client-year').html(" ");
	for (var i = 1; i < 21; i++)
	{
		$('#select-client-year').append("<option id='year-" + (i + 1980) + "'>" + (i + 1980) + "</option>");
	}

	$('#select-client-month').on('change', function() {
  		if ($('#select-client-month').find(":selected").text() == '2')
  		{
  			$('#select-client-day').html(" ");

			for (var i = 1; i < 30; i++)
			{
				$('#select-client-day').append("<option id='day-" + i + "'>" + i + "</option>");
			}
		}
	});

	$('#select-pas-day').html(" ");
	for (var i = 1; i < 32; i++)
	{
		$('#select-pas-day').append("<option id='day-" + i + "'>" + i + "</option>");
	}
	$('#select-pas-month').html(" ");
	for (var i = 1; i < 13; i++)
	{
		$('#select-pas-month').append("<option id='month-" + i + "'>" + i + "</option>");
	}
	$('#select-pas-year').html(" ");
	for (var i = 1; i < 21; i++)
	{
		$('#select-pas-year').append("<option id='year-" + (i + 2015) + "'>" + (i + 2015) + "</option>");
	}

	$('#select-pas-month').on('change', function() {
  		if ($('#select-pas-month').find(":selected").text() == '2')
  		{
  			$('#select-pas-day').html(" ");

			for (var i = 1; i < 30; i++)
			{
				$('#select-pas-day').append("<option id='day-" + i + "'>" + i + "</option>");
			}
		}
	});

	
	$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/getname',
		data: {'token' : getCookie("sesToken")},
		success: function(resData) {
			$('#client-new-emp').val(resData.name + " " + resData.surname);
		},	
	});

	$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/order/v1/allProducts',
		data: {'token' : getCookie("sesToken")},
		success: function(resData) {
			for (var i = 0; i < resData.items.length; i++)
			{
				$('#select-product').append("<option id='product-" + i + "' value='" + resData.items[i].id + "'>" 
					+ resData.items[i].title 
					+ "</option>" 
				);
			}
		},
	});		
});


