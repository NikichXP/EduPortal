$(function(){
			
	//open product menu
	$('#menu-product-open').on("click", function() {
				
		$('#table-product-menu').html("<tbody></tbody>");
		$('#product-menu-text-block').html("<H2>Мои клиенты</H2>");
		$('#product-new').css('display', 'none');
		
		$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/getMyproducts',
		data: tokenJson,
		success: function(resData) {
			var imax = resData.items.length;
			var count = 0;
			for (var i = 0; i < resData.items.length; i++)
			{
				if (count == imax) break;
				$('#table-product-menu tbody').append("<tr id='tr-product-menu-" + (i + 1) + "'>");
				for (var j = 0; j < 5; j++)
				{
					$('#tr-product-menu-' + (i + 1)).append(
					"<td class='td-product-menu'>" + resData.items[count].name + " " + resData.items[count].surname + 
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
	
	//product creation menu
	$('#product-menu-create').on('click', function() {
		
		$('#product-menu-create').html("Принять");
		
		if ($('#product-new').css('display') == 'block')
		{
			$('#product-new').css('display', 'none');
			$('#product-menu-create').html("Добавить клиента");
			$('#product-menu').css('height', '322px');
			
			var clData = new FormData();    
			clData.append('token', authSesToken);
			clData.append('mail', $('#product-new-email').val());
			clData.append('name', $('#product-new-name').val());
			clData.append('surname', $('#product-new-surname').val());
			clData.append('phone', $('#product-new-phone').val());
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
			$('#product-new').css('display', 'block');
			$('#product-menu').css('height', '637px');
			$('#product-menu-dismiss').css('display', 'block');
		}
		
	});
	//cancel product creation
	$('#product-menu-dismiss').on('click', function() {
		window.close();
	});

	$('#select-product-day').html(" ");
	for (var i = 1; i < 32; i++)
	{
		$('#select-product-day').append("<option id='day-" + i + "'>" + i + "</option>");
	}
	$('#select-product-month').html(" ");
	for (var i = 1; i < 13; i++)
	{
		$('#select-product-month').append("<option id='month-" + i + "'>" + i + "</option>");
	}
	$('#select-product-year').html(" ");
	for (var i = 1; i < 21; i++)
	{
		$('#select-product-year').append("<option id='year-" + (i + 1980) + "'>" + (i + 1980) + "</option>");
	}

	$('#select-product-month').on('change', function() {
  		if ($('#select-product-month').find(":selected").text() == '2')
  		{
  			$('#select-product-day').html(" ");

			for (var i = 1; i < 30; i++)
			{
				$('#select-product-day').append("<option id='day-" + i + "'>" + i + "</option>");
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
		
});


