$().ready(function() {
	$.ajax({
		type: 'POST',
		url: 'shenpi.action',
		dataType: 'text',
		success: function(data){
			if("0"==data){
				$('#itemTr_1817').css('display','none');
			}
		}
	});
});
