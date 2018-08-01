	$(document).bind("contextmenu",function(){return false;});
	$(function() {
		$('.left-nav a').click(function(ev) {
			$('.left-nav a.selected').removeClass('selected');
			$(this).addClass('selected');
		//	parent.syspersion_top.setTile($(this).html());
		});
	});