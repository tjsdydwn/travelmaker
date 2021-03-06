/**
 * 
 */

$().ready(function() {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");

	$.ajax({
		type : 'post',
		url : '/friend/getList',
		data : {
			'pg' : $('#pg').val()
		},
		dataType : 'json',
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		success : function(data) {
			console.log('success');
			// alert(data.responseText);
			console.log(JSON.stringify(data));

			$.each(data.list, function(index, items) {
				var is_finish = '';

				if (items.is_finish == 0) {
					is_finish = '가능';
				} else {
					is_finish = '마감';
				}

				$('<tr/>', {
					onclick : 'location.href="/friend/view/' + items.fno + '"',
					style : 'cursor: pointer;'
				}).append($('<td/>', {
					text : items.fno
				})).append($('<td/>', {
					text : items.id
				})).append($('<td/>', {
					text : items.title
				})).append($('<td/>', {
					text : items.date_start + '~' + items.date_end
				})).append($('<td/>', {
					text : is_finish
				})).appendTo($('#dataTable'));
			});

			$('.pagination').html(data.friendPaging.pagingHTML);
		},
		error : function(error) {
			console.log('fail');
			alert(error.responseText);
		}
	});

	$('#domestic').click(function() {
		location.href = '/friend/write?is_domestic=' + $(this).val();
	});
	$('#overseas').click(function() {
		location.href = '/friend/write?is_domestic=' + $(this).val();
	});
});