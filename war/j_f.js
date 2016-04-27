$(function(){
	
	var $resDiv = $('#div-res');
	var $login = $('#login');
	var $pass = $('#pass');
	
	$('#div-send-button').on('click', function(){
		
		var authData = {
			login: $login.val(),
			pass: $pass.val(),
		};
		
		$.ajax({
			type: 'GET',
			url: 'https://beta-dot-eduportal-1277.appspot.com/_ah/api/user/v1/auth',
			data: authData,
			success: function(resData) {
				$resDiv.html('<p>sessionId=' + resData.sessionId + '</p>')
			},
			error: $resDiv.html('<p>Authorisation faild</p>')
		});		
			
	});
	
});
