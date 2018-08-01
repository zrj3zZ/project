$(function() {
	try {
		var api = frameElement.api, W = api.opener;
		/*$("#NOTICENO").blur(function() {
			var pageUrl = "zqb_validationAnnouncement.action";
			var noticeno = $("#NOTICENO").val();
			var instanceid = $("#INSTANCEID").val();
			var khbh = $("#KHBH").val();
			$.ajax({
			    url : pageUrl,  
			    async : false,
			    type : "POST",  
			    data: {noticeno:noticeno,khbh:khbh,instanceid:instanceid},
			    success : function(data) {
			    	if (data == 'success') {
			    		alert("公告编号已存在");
			    	return;
			    	}
			    }  
			});
		});*/
	} catch (e) {
	}
});
function getItsArticles(){
	var khbh=$("#KHBH").val();
	var url = 'zqb_itsArticles_index.action?khbh='+khbh;
	var target = "_blank";
	var win_width = window.screen.width;
	var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=no');
	page.location = url;
	return;
}