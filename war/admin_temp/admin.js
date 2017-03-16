$(function() {
	var tokenJson = {
		id : $('#prodid')
	};
	var product = {};

	$('#prodSubmit').on('click', function() {
		// alert('good!');
	});
	// ajax({
	// type: 'GET',
	// url: 'localhost:8888/_ah/api/order/v1/getproductbyid',
	// data: tokenJson,
	// success: function(resData) {
	// alert(JSON.stringify(resData));
	// // $('#client-new-fields').html(' ');
	// // for (var i = 0; i < resData.items.length; i++)
	// // {
	// // $('#client-new-fields').append("<tr id='field-" + i + "'><input
	// type='hidden' id='select-" + i + "' value='" + resData.items[i] + "'>");
	// // $('#field-' + i).append("<td class='col-left'>");
	// // $('#field-' + i + ' .col-left').append(resData.items[i]);
	// // $('#field-' + i).append('</td>');

	// // $('#field-' + i).append("<td class='col-right'>");
	// // $('#field-' + i + ' .col-right').append("<textarea class='t-a'
	// rows='2' cols='40' id='field-" + i + "'></textarea>");
	// // $('#field-' + i).append('</td>');
	// // $('#client-new-fields').append('</tr>');
	// // countOfFields++;
	// // }
	// },
	// fail: function(resData) {
	// alert(JSON.stringify(resData));
	// }
	// });
});
