$(function(){
	var countOfFields = 0;
			
	//fields
	$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/fields',
		success: function(resData) {
			$('#client-new-fields').html(' ');
			for (var i = 0; i < resData.items.length; i++) 
			{
				$('#client-new-fields').append("<tr id='field-" + i + "'><input type='hidden' id='select-" + i + "' value='" + resData.items[i] + "'>");
					$('#field-' + i).append("<td class='col-left'>");
						$('#field-' + i + ' .col-left').append(resData.items[i]);
					$('#field-' + i).append('</td>');

					$('#field-' + i).append("<td class='col-right'>");
						$('#field-' + i + ' .col-right').append("<textarea class='t-a' rows='2' cols='40' id='field-" + i + "'></textarea>");
					$('#field-' + i).append('</td>');
				$('#client-new-fields').append('</tr>');
				countOfFields++;
			}
		},	
	});

	
	//client creation menu
	$('#client-menu-send').on('click', function() {

		var borndate = $('#select-client-day').find(":selected").val() + "." + $('#select-client-month').find(":selected").val() + "." + $('#select-client-year').find(":selected").val();
		var pasdate = $('#select-pas-day').find(":selected").val() + "." + $('#select-pas-month').find(":selected").val() + "." + $('#select-pas-year').find(":selected").val();

		var clData = new FormData();    
		clData.append('token', getCookie('sesToken'));
		clData.append('mail', $('#field-req-4').val());
		clData.append('name', $('#field-req-1').val());
		clData.append('surname', $('#field-req-2').val());
		clData.append('phone', $('#field-req-3').val());
		clData.append('born', borndate);
		clData.append('passportActive', pasdate);

		var key = new Array();
		var value = new Array();
		// $.ajax({
		// 	type: 'GET',
		// 	url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/fields',
		// 	success: function(resData) {
		// 		for (var i = 0; i < resData.items.length; i++) 
		// 		{
		// 			keys[i] = resData.items[i];
		// 		}
		// 	},	
		// });
		var j = 0;
		for (var i = 0; i < countOfFields; i++) 
		{
			if ($('textarea#field-' + i).val().length > 0)
			{
				key[j] = $('input#select-' + i).val();
				value[j] = $('textarea#field-' + i).val();
				j++;
			}
		}

		var keys = '';
		var values = '';
		for (var i = 0; i < key.length; i++) 
		{
			keys += key[i];
			values += value[i];
			if (i != key.length -1) 
			{
				keys += "ף";
				values += "ף";
			}  
		}

		clData.append('keys', keys);
		clData.append('values', values);
			
		$.ajax({
			type: 'POST',
			url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/createuser',
			data: clData,
			processData: false,
			contentType: false,
			success: window.close(),	
		});	

	});
	//cancel client creation
	$('#client-menu-dismiss').on('click', function() {
		window.close();
	});

	$('#select-client-day').html(" ");
	for (var i = 1; i < 32; i++)
	{
		$('#select-client-day').append("<option value='" + i + "'>" + i + "</option>");
	}
	$('#select-client-month').html(" ");
	for (var i = 1; i < 13; i++)
	{
		$('#select-client-month').append("<option value='" + i + "'>" + i + "</option>");
	}
	$('#select-client-year').html(" ");
	for (var i = 1; i < 20; i++)
	{
		$('#select-client-year').append("<option value='" + (i + 1980) + "'>" + (i + 1980) + "</option>");
	}

	$('#select-client-month').on('change', function() {
  		if ($('#select-client-month').find(":selected").text() == '2')
  		{
  			$('#select-client-day').html(" ");

			for (var i = 1; i < 30; i++)
			{
				$('#select-client-day').append("<option value='" + i + "'>" + i + "</option>");
			}
		}
	});

	$('#select-pas-day').html(" ");
	for (var i = 1; i < 32; i++)
	{
		$('#select-pas-day').append("<option value='" + i + "'>" + i + "</option>");
	}
	$('#select-pas-month').html(" ");
	for (var i = 1; i < 13; i++)
	{
		$('#select-pas-month').append("<option value='" + i + "'>" + i + "</option>");
	}
	$('#select-pas-year').html(" ");
	for (var i = 1; i < 6; i++)
	{
		$('#select-pas-year').append("<option value='" + (i + 2015) + "'>" + (i + 2015) + "</option>");
	}

	$('#select-pas-month').on('change', function() {
  		if ($('#select-pas-month').find(":selected").text() == '2')
  		{
  			$('#select-pas-day').html(" ");

			for (var i = 1; i < 30; i++)
			{
				$('#select-pas-day').append("<option value='" + i + "'>" + i + "</option>");
			}
		}
	});
			
});


