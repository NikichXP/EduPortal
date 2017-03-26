$(function(){
			
	//open product menu
	$('#menu-product-open').on("click", function() {
				
		$('#table-product-menu').html("<tbody></tbody>");
		$('#product-menu-text-block').html("<H2>Продукты</H2>");
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
	
	
	//cancel product creation
	$('#product-menu-dismiss').on('click', function() {
		window.close();
	});

	$('#select-city').html(' ');
	$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/util/v1/cityList',
		// data: clData,
		success: function(resData) {
			for (var i = 0; i < resData.items.length; i++)
			{
				$('#select-city').append("<option value='" + resData.items[i].id + "'>" + resData.items[i].cyrname + "</option>");
			}
		},	
	});


	//send
	$('#product-menu-send').on('click', function() {
				
  		
  		//var pageHref = location;
		var pageAd = location.toString();
		var result = pageAd.slice(pageAd.search(/token=/) + 6);

		var prData = {
  			title: $('#product-new-name').val(),
			price: $('#product-new-price').val(),
			description: $('#product-comment').val(),
  			cityid: $('#select-city option:selected').val(),
  			token: result,
  		};
		alert("Продукт создан");	
		$.ajax({
			type: 'GET',
			url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/order/v1/product/add',
			data: prData,
			success: window.close(),
		});	
	});	


});


