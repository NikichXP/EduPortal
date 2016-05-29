$(function(){
			
	var countOfFields = 0;		
	

	
	var userData = {
		'token' : getCookie("sesToken"), 
		'id' : getCookie('productID'),
	}

	$.ajax({
		type: 'GET',
		url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/order/v1/getproductbyid',
		data: userData,
		success: function(resData) {
			$('#field-req-1').html(resData.title);
			$('#field-req-2').html(resData.description);
			$('#field-req-3').html(resData.city.name);
			$('#field-req-4').html(resData.city.country.name);
			$('#field-req-6').html(resData.defaultPrice);

			var time = resData.start + " - " + resData.end;

			$('#field-req-5').html(time);
		},
	});
		
	

	
});
$(window).unload(function() {
  		deleteCookie('productID');
	});

function checkFiles(data)
{
	if (data.files == null) return 0;
	else return data.files.length;
};

