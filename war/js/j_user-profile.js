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

	
	//get user's files
	// $.ajax({
	// 	type: 'GET',
	// 	url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/order/v1/allOrders',
	// 	data: {'token' : getCookie("sesToken")},
	// 	success: function(resData) { 
	// 		for (var i = 0; i < resData.items.length; i++)
	// 			checkFiles(resData.items[i]) 
	// 	},
	// });
	var userData = {
		'token' : getCookie("sesToken"), 
	}

	if (getCookie('clientID')) userData["clientid"] = getCookie('clientID');
		
	$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/getInfo',
		data: userData,
		success: function(resData) {
			$('#field-req-1').html(resData.name);
			$('#field-req-2').html(resData.surname);
			$('#field-req-2-1').html(resData.fathersname);
			$('#field-req-3').html(resData.phone);
			$('#field-req-4').html(resData.mail);
			
			//var bdate = resData.born.split('-');

			//$('#field-req-5').html(bdate[2] + "-" + bdate[1] + "-" + bdate[0]);
			$('#field-req-5').html(resData.born);

			// var dd1 = bdate.getDate();
		 //    var mm1 = bdate.getMonth() + 1;   
		 // 	var yyyy1 = bdate.getFullYear();
			// $('#field-req-5').html(dd1 + "." + mm1 + "." + yyyy1);

			// var pdate = new Date(resData.passportActive);	
			// var dd2 = pdate.getDate();
		 //    var mm2 = pdate.getMonth() + 1;   
		 // 	var yyyy2 = pdate.getFullYear();
			// $('#field-req-6').html(dd2 + "." + mm2 + "." + yyyy2);

			var k = 0;
			for (var i = 0; i < resData.simpleData.length; i++)
			{
				for (var j = 0; j < 30; j++)
				{
					if ($('tr#field-' + j + ' td.col-left').html() == resData.simpleData[i][0]) 
					{
						$('tr#field-' + j + ' td.col-right textarea').html(resData.simpleData[i][1]);
					}
				}
			}

			if (resData.final) 
			{
				$('textarea.t-a').attr('readonly','readonly');
			}

		},
	});	

	//client creation menu
	$('#client-menu-send').on('click', function() {

		var borndate = $('textarea#field-req-5').val();
		//var pasdate = $('#field-req-6').html();

		var clData = new FormData();    
		if (getCookie('clientID')) clData.append('id', getCookie('clientID'));
		else clData.append('id', getCookie('sesToken'));

		clData.append('token', getCookie('sesToken'));
		clData.append('mail', $('textarea#field-req-4').val());
		clData.append('name', $('textarea#field-req-1').val());
		clData.append('surname', $('textarea#field-req-2').val());
		clData.append('fathersname', $('textarea#field-req-2-1').val());
		clData.append('phone', $('textarea#field-req-3').val());
		clData.append('born', borndate);
		//clData.append('passportActive', '111');

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
				keys += 'Ʉ';
				values += 'Ʉ';
			}  
		}

		clData.append('keys', keys);
		clData.append('values', values);
			
		$.ajax({
			type: 'POST',
			url: 'https://eduportal-1277.appspot.com/_ah/api/user/v1/updateuser',
			data: clData,
			processData: false,
			contentType: false,
			success: window.close(),	
		});	

	});

	//product creation menu
	$('#client-menu-pass').on('click', function() {

		var url = "admin/changepass.jsp?token=" + getCookie("sesToken");
		var windowName = "Смена пароля";
		var windowSize = ["width=450, height=900"];
		window.open(url, windowName, windowSize);
		event.preventDefault();
	});

	if (getCookie("clientID")) if (getCookie("accessLevel") == "ADMIN") $('#client-menu-del').css('display', 'block');
	
});
// $(window).unload(function() {
//   		deleteCookie('clientID');
// 	});

function checkFiles(data)
{
	if (data.files == null) return 0;
	else return data.files.length;
};

