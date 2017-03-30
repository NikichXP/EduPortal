$(function(){
		
	var sesTimeOut;
	var sID;
	
	var today = new Date();
	var dateObj = Date.parse(today);
	
	var authSesToken = getCookie("sesToken");
	var authSesTO = getCookie("sesTO");
	
	if (authSesToken != null)
		if(authSesTO - dateObj > 0)
			window.location = "workspace.html";

	$('#div-send-button').on('click', function(){		
		loginAjax();	
	});

	$(document).keypress(function(e){
	     	if (e.keyCode == 13) {
	     		loginAjax();			
			}
	    });	

	
});

function loginAjax() {
	
	var authData = {
		login: $('#login').val(),
		pass: $('#pass').val(),
	};
				
	$.ajax({
		type: 'GET',
		url: apiPath + 'user/auth',
		data: authData,
		success: function(resData) {
			sTO = resData.timeout;
			sID = resData.sessionId;
			setCookie("sesToken", sID);
			setCookie("mainToken", sID);
			setCookie("sesTO", sTO);
			setCookie("accessLevel", resData.accessLevel);
			window.location = "workspace.html";
		},
	});

};
