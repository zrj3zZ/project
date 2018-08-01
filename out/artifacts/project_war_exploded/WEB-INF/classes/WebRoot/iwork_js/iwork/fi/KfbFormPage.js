$().ready(function() {
	 
	
		initView(); 
 });

document.write("<script src='iwork_js/iwork/fi/commonPrint.js'></script>");
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
		$("#FPJEDX").val(result);
		$("#labelFPJEDX").text(result);
		
	});
	
}
//设置WBS信息
function selectWBSNode(fieldName,treeNode){
	//设置WBS
	$("#WBS").val(treeNode.wbsno);
	$("#WBSMS").val(treeNode.name);
}
function hideRq(obj){  
	
	var type = obj.value;
	if(type=='是'){
		$("#yjskrqtr").hide();
		$("#yjskrqtr1").hide();
	}else{
		
		$("#yjskrqtr").show();
		$("#yjskrqtr1").show();
	}
	
}

function initView(){  
	var type = $('input[name="SFYSK"]:checked').val();
	//var type = obj.value;
	if(type=='是'){
		$("#yjskrqtr").hide();
		$("#yjskrqtr1").hide();
	}else{
		$("#yjskrqtr1").show();
		$("#yjskrqtr").show();
	}
	
}