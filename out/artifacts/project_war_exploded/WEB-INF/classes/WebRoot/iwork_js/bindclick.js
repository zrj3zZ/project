$(function(){
	$(document).bind('keyup', function(event) {
		if (event.keyCode == "13") {
			//回车执行查询
			$('#search').click();
		}
	})
});