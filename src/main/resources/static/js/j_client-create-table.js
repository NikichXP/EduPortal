$(function(){
	var countOfFields = 0;
			
	//fields
	$.ajax({
		type: 'GET',
		url: apiPath + 'user/fields',
		success: function(resData) {
			$('#client-new-fields').html(' ');
			for (var i = 0; i < resData.items.length; i++) 
			{
				if (resData.items[i] == "Срок действия заграничного паспорта")
				{
					$('#client-new-fields').append("<tr id='field-" + i + "'><input type='hidden' id='select-" + i + "' value='" + resData.items[i] + "'>");
						$('#field-' + i).append("<td class='col-left'>");
							$('#field-' + i + ' .col-left').append(resData.items[i]);
						$('#field-' + i).append('</td>');

						$('#field-' + i).append("<td class='col-right'>");
							$('#field-' + i + ' .col-right').append("<select id='select-pas-day'>"); 
								for (var s = 1; s < 32; s++)
								{
									$('#field-' + i + ' .col-right select#select-pas-day').append("<option value='" + s + "'>" + s + "</option>");
								}
							$('#field-' + i + ' .col-right').append("</select><select id='select-pas-month'>"); 
							 	for (var s = 1; s < 13; s++)
								{
									$('#field-' + i + ' .col-right select#select-pas-month').append("<option value='" + s + "'>" + s + "</option>");
								}
							$('#field-' + i + ' .col-right').append("</select><select id='select-pas-year'>"); 
								for (var s = 1; s < 6; s++)
								{
									$('#field-' + i + ' .col-right select#select-pas-year').append("<option value='" + (s + 2015) + "'>" + (s + 2015) + "</option>");
								}
							$('#field-' + i + ' .col-right').append("</select>");
						$('#field-' + i).append('</td>');
					$('#client-new-fields').append('</tr>');

					// <td class='col-right'>
					// 	<select id='select-pas-day'></select><select id='select-pas-month'></select><select id='select-pas-year'></select>
					// </td>
					countOfFields++;
				}
				else
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
				
			}
		},	
	});

	
	//client creation menu
	$('#client-menu-send').on('click', function() {

		var borndate = $('#select-client-year').find(":selected").val() + "-" + $('#select-client-month').find(":selected").val() + "-" + $('#select-client-day').find(":selected").val();
		var pasdate = $('#select-pas-day').find(":selected").val() + "-" + $('#select-pas-month').find(":selected").val() + "-" + $('#select-pas-year').find(":selected").val();

		var clData = new FormData();    
		clData.append('token', getCookie('sesToken'));
		clData.append('mail', $('#field-req-4').val());
		clData.append('name', $('#field-req-1').val());
		clData.append('surname', $('#field-req-2').val());
		clData.append('fathersname', $('#field-req-2-1').val());
		clData.append('phone', $('#field-req-3').val());
		clData.append('born', borndate);
		clData.append('passportActive', pasdate);

		var key = new Array();
		var value = new Array();
		// $.ajax({
		// 	type: 'GET',
		// 	url: apiPath + 'user/fields',
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
			if (i != 4)
			{
				if ($('textarea#field-' + i).val().length > 0)
				{
					key[j] = $('input#select-' + i).val();
					value[j] = $('textarea#field-' + i).val();
					j++;
				}
			}
				
			else 
				{
					key[j] = $('input#select-' + i).val();
					value[j] = pasdate;
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
			url: apiPath + 'user/createuser',
			data: clData,
			processData: false,
			contentType: false,
			//success: window.close(),	
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

//$('#table-client-list').on("click", "td.td-client-list", function() {

	// $('#select-pas-day').html(" ");
	// for (var i = 1; i < 32; i++)
	// {
	// 	$('#select-pas-day').append("<option value='" + i + "'>" + i + "</option>");
	// }

	$('#select-pas-day').load(function() {
		for (var i = 1; i < 32; i++)
		{
			$('#select-pas-day').append("<option value='" + i + "'>" + i + "</option>");
		}
	});

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



