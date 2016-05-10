$(function(){
		
	var $resDiv = $('#div-right');
	var dateObj = new Date();
	
	var authSesToken = getCookie("sesToken");
	var authSesTO = getCookie("sesTO");
	
	if (authSesToken != null)
		if(authSesTO - dateObj < 0)
			window.location = "auth.html";
	
	
	$('#menu-showses').on('click', function(){
		$resDiv.append("TokenId = " + getCookie("sesToken") + "; tokenTO = " + getCookie("sesTO"));	
	});
	
	$('#menu-logout').on('click', function(){
		deleteCookie("sesToken");	
		deleteCookie("sesTO");	
		window.location = "auth.html";
	});
	
});
