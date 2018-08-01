$().ready(function() {
	 
		initStatus();
		 
 });
document.write("<script src='iwork_js/iwork/fi/zhifu_script.js'></script>");


/**
 * 实时获取大写金额
 * @param obj
 * @return
 */
function changeCount(obj){  
	
	var count = obj.value;
	var pageUrl="getNumberRMB_action.action";
	$.post(pageUrl,{count:count}, function(result) {	
		//alert(result);
		$("#DXJE").val(result);
		$("#labelDXJE").text(result);
		
	});
	
}