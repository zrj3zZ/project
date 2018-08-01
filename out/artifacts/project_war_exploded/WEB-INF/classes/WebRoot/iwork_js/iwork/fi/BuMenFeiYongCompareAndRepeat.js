
//数据字典表的重写方法
function execDictionarySel(id,rd){
	var ysmc=rd.DESCRIPTION;
	var ndyse=rd.ZAMOU;
	var ysbh=rd.BPC_ACCOUNT;
	$("#YSMC").val(rd.DESCRIPTION);
	$("#NDYSE").val(rd.ZAMOU);
	$("#YSBH").val(rd.BPC_ACCOUNT);
	var yskzbh = parent.$("#YSKZBH").val();
	
	var instanceId = $("#instanceId").val();
	
	var pageurl = "sanbu_plan_bmfy_buMenFeiYongRepeat.action";
	$.getJSON(pageurl,{instanceId:instanceId,ysbh:ysbh},function(json){
		var flag=json.flag;
		if(flag){
			var pageurl = "sanbu_plan_bmfy_buMenFeiYongContrast.action";
			$.getJSON(pageurl,{yskzbh:yskzbh,ysbh:ysbh,ndyse:ndyse},function(json){
				$("#KYYE").val(json.KYYE);				
			});
		}else{
			alert("费用项重复，请重新选择！");
			$("#YSMC").val("");
			$("#NDYSE").val("");
			$("#YSBH").val("");
			$("#KYYE").val("");
		}
	});
	
	
	
	
}
//对部门费用子表的可用余额的比较
function CompareAmount(){
	
	var kyye = $("#KYYE").val();
	var byys = $("#BYYS").val();
	if(isNaN(parseInt(kyye))){
		alert("没有可用余额，请选择预算!");
		$("#BYYS").val(null);
	}
	if(parseInt(byys)>parseInt(kyye)){
		alert("可用余额不能小于本月预算,请重新填写");
		$("#BYYS").val("");
	}
}
//对部门费用子表的预算部门名称进行不可重复的判断
function repeat(id,rd){
	var instanceId = $("#instanceId").val();
	var ysbh=rd.BPC_ACCOUNT;
	var pageurl = "sanbu_plan_bmfy_buMenFeiYongRepeat.action";
	$.getJSON(pageurl,{instanceId:instanceId,ysbh:ysbh},function(json){
		var flag=json.flag;
		if(flag){
			
		}else{
			alert("请选择未添加的预算单名称");
			$("#YSMC").val("");
			$("#NDYSE").val("");
			$("#YSBH").val("");
			$("#KYYE").val("");
		}
	});
}

