
function initStatus(){
	 //根据费用类别
	// setsubFormType($("#FYLB").val());
	//根据结算方式零余额复选框初始状态判断是否对其对应的文本框进行灰化处理
	if($("#JSFSLYE-0").attr("checked")){
		 try{
				$("#LYE").attr("readonly","");
				$("#LYE").attr("style","background:#fff;;width:85px");
				
				$("#LYE").rules("remove");
		        $("#LYE").rules("add", {required: true, messages:{required: "请填写零余额金额"} });
		 }catch(e){
			 
		 }

	}
	else{
		 try{
				$("#LYE").attr("readonly","readonly"); 
				$("#LYE").attr("style","background:#efefef;width:85px");
				
				$("#LYE").val("");
				$("#LYE").rules("remove"); 
				$("#LYE").rules("add", {required:false});
		 }catch(e){
			 
		 }

	}
	
	//根据结算方式汇款复选框初始状态判断是否对其对应的文本框进行灰化处理
	 if($("#JSFSHK-0").attr("checked")){
		 try{
				$("#HKJE").attr("readonly","");
				$("#HKJE").attr("style","background:#fff;;width:85px");
				$("#HKJE").rules("remove");
		        $("#HKJE").rules("add", {required: true, messages:{required: "请填写汇款金额"} });
		 }catch(e){
			 
		 }

	 }
	 else{
		 try{
				$("#HKJE").val("");
				$("#HKJE").attr("readonly","readonly"); 
				$("#HKJE").attr("style","background:#efefef;width:85px");
				$("#HKJE").rules("remove"); 
				$("#HKJE").rules("add", {required:false});
		 }catch(e){
			 
		 }

	 }
	 //根据结算方式现金复选框初始状态判断是否对其对应的文本框进行灰化处理
	if($("#JSFSXJ-0").attr("checked")){
		 try{
				$("#XJJE").attr("readonly","");
				$("#XJJE").attr("style","background:#fff;;width:85px");
				
				$("#XJJE").rules("remove");
		        $("#XJJE").rules("add", {required: true, messages:{required: "请填写现金金额"} });
		 }catch(e){
			 
		 }

	}
	else{
		 try{
				$("#XJJE").attr("readonly","readonly"); 
				$("#XJJE").attr("style","background:#efefef;width:85px");
				
				$("#XJJE").val("");
				$("#XJJE").rules("remove"); 
				$("#XJJE").rules("add", {required:false});
		 }catch(e){
			 
		 }

	}
	//根据结算方式支票复选框初始状态判断是否对其对应的文本框进行灰化处理
	if($("#JSFSZP-0").attr("checked")){
		 try{
			 	$("#ZPJE").attr("readonly","");
			 	$("#ZPJE").attr("style","background:#fff;;width:85px");
			 	
				$("#ZPJE").rules("remove");
		        $("#ZPJE").rules("add", {required: true, messages:{required: "请填写支票金额"} });
		 }catch(e){
			 
		 }

	}
	else{
		 try{
			 	$("#ZPJE").attr("readonly","readonly"); 
			 	$("#ZPJE").attr("style","background:#efefef;width:85px");
			 	
			 	$("#ZPJE").val("");
				$("#ZPJE").rules("remove"); 
				$("#ZPJE").rules("add", {required:false});
		 }catch(e){
			 
		 }

	}
	 //根据结算方式POS初始状态判断是否对其对应的文本框进行灰化处理
	if($("#JSFSPOS-0").attr("checked")){
		 try{
			 	$("#POSJE").attr("readonly","");
			 	$("#POSJE").attr("style","background:#fff;;width:85px");
			 	
				$("#POSJE").rules("remove");
		        $("#POSJE").rules("add", {required: true, messages:{required: "请填写POS金额"} }); 
		 }catch(e){
			 
		 }

	}
	else{
		 try{
				$("#POSJE").val("");
				$("#POSJE").attr("readonly","readonly"); 
				$("#POSJE").attr("style","background:#efefef;width:85px");
				$("#POSJE").rules("remove"); 
				$("#POSJE").rules("add", {required:false});
		 }catch(e){
			 
		 }

	}
	 //根据结算方式汇票复选框初始状态判断是否对其对应的文本框进行灰化处理
	if($("#JSFSHP-0").attr("checked")){
		 try{
				$("#HPJE").attr("readonly","");
			 	$("#HPJE").attr("style","background:#fff;;width:85px");
			 	
				$("#HPJE").rules("remove");
		        $("#HPJE").rules("add", {required: true, messages:{required: "请填写汇票金额"} });
		 }catch(e){
			 
		 }

	}
	else{
		 try{
				$("#HPJE").val("");
				$("#HPJE").attr("readonly","readonly"); 
				$("#HPJE").attr("style","background:#efefef;width:85px");
				$("#HPJE").rules("remove"); 
				$("#HPJE").rules("add", {required:false});
		 }catch(e){
			 
		 }
	 	

	} 
	
	//根据结算方式复选框初始状态判断是否对其对应的文本框进行灰化处理
	if($("#JSFSGZ-0").attr("checked")){
		 try{
				$("#GZJE").attr("readonly","");
				$("#GZJE").attr("style","background:#fff;;width:85px");
				
				$("#GZJE").rules("remove");
				$("#GZJE").rules("add", {required: true, messages:{required: "请填写挂账金额"} });
		 }catch(e){
			 
		 }

	}
	else{ 
		 try{
				$("#GZJE").val("");
				$("#GZJE").attr("readonly","readonly"); 
				$("#GZJE").attr("style","background:#efefef;width:85px");
				$("#GZJE").rules("remove");  
				$("#GZJE").rules("add", {required:false});
		 }catch(e){
			 
		 }
		

	} 
	
	var xjje = $('input[name="XJJE"]').val();
	var zpje = $('input[name="ZPJE"]').val();
	var hpje = $('input[name="HPJE"]').val();
	var hkje = $('input[name="HKJE"]').val();
	var posje = $('input[name="POSJE"]').val();
	//结算方式汇款与POS复选框只要有一个被勾选，则显示收款人，开户行，收款账号,汇款备注信息；否则则隐藏
	if($("#JSFSPOS-0").attr("checked")||$("#JSFSHK-0").attr("checked") || $("#JSFSHP-0").attr("checked") || (hpje!="" && hpje!=0) || (posje!="" && posje!=0) || ((hkje!="" && hkje!=0))){
		$("#title_SKR").show();
		$("#title_SKZH").show();
		$("#title_KHX").show();
		$("#title_HKBZ").show();
		$("#data_SKR").show();
		$("#data_SKZH").show();
		$("#data_KHX").show();
		$("#data_HKBZ").show();
	}
	else{
		$("#title_SKR").hide();
		$("#title_SKZH").hide();
		$("#title_KHX").hide();
		$("#title_HKBZ").hide();
		$("#data_SKR").hide();
		$("#data_SKZH").hide();
		$("#data_KHX").hide();
		$("#data_HKBZ").hide();
	}
	if($("#JSFSZP-0").attr("checked") || (zpje!="" && zpje!=0)){
		$("#title_XZ").show();
		$("#data_XZ").show();
		$("#title_ZPSKF").show();
		$("#data_ZPSKF").show();
	}else{
		$("#title_ZPSKF").hide();
		$("#data_ZPSKF").hide();
		$("#title_XZ").hide();
		$("#data_XZ").hide();
	}
 }

// 根据退回金额判断退回支付方式隐显示
function onchangeThje(obj){
	if(obj.value==''||obj.value=='0'){
		$("#title_THZFFS").hide();
		$("#data_THZFFS").hide();
		$("#THZFFS0").rules("remove"); 
		$("#THZFFS0").rules("add", {required:false});
	}else{
		$("#title_THZFFS").show();
		$("#data_THZFFS").show();
		$("#THZFFS0").rules("remove");
        $("#THZFFS0").rules("add", {required: true, messages:{required: "请选择退回支付方式"} });
	}
}

// 根据冲销借款单判断退回金额是否可输入
function onchangeJkdbh(obj){
	if(obj.value==''){
		$("#THJE").attr("readonly","readonly");
		$("#THJE").attr("style","background:#efefef;width:85px");
		$("#title_THZFFS").hide();
		$("#data_THZFFS").hide();
	}else{
		var thje = $("#THJE").val();
		if(thje==''||thje=='0'){
			$("#title_THZFFS").hide();
			$("#data_THZFFS").hide();
			$("#THZFFS0").rules("remove");
			$("#THZFFS0").rules("add", {required:false});
		}else{
			$("#title_THZFFS").show();
			$("#data_THZFFS").show();
			$("#THZFFS0").rules("remove");
	        $("#THZFFS0").rules("add", {required: true, messages:{required: "请选择退回支付方式"} });
		}
	}
}

//点击结算方式现金复选框，若选中则可以对其关联的文本框进行编辑，若不选中则进行灰化处理，并清空其关联的文本框
function OnclickJSFSXJ(obj){
	if(obj.checked){
		$("#XJJE").attr("readonly","");
		$("#XJJE").attr("style","background:#fff;;width:85px");
		$("#XJJE").rules("remove");
        $("#XJJE").rules("add", {required: true, messages:{required: "请填写现金金额"} });
	} 
	else{
		$("#XJJE").val("");
		$("#XJJE").attr("readonly","readonly"); 
		$("#XJJE").attr("style","background:#efefef;width:85px");
		$("#XJJE").rules("remove"); 
		$("#XJJE").rules("add", {required:false});
	}
}
//点击结算方式支票复选框，若选中则可以对其关联的文本框进行编辑，若不选中则进行灰化处理，并清空其关联的文本框
function OnclickJSFSZP(obj){
	if(obj.checked){
		$("#ZPJE").attr("readonly","");
		$("#ZPJE").attr("style","background:#fff;;width:85px");
		$("#ZPJE").rules("remove");
        $("#ZPJE").rules("add", {required: true, messages:{required: "请填写支票金额"} });
	}
	else{
		$("#ZPJE").val("");
		$("#ZPJE").attr("readonly","readonly"); 
		$("#ZPJE").attr("style","background:#efefef;width:85px");
		$("#ZPJE").rules("remove"); 
		$("#ZPJE").rules("add", {required:false});
	}
	if(obj.checked){
		$("#title_XZ").show();
		$("#data_XZ").show();
		$("#title_ZPSKF").show();
		$("#data_ZPSKF").show();
	}else{
		$("#title_ZPSKF").hide();
		$("#data_ZPSKF").hide();
		$("#title_XZ").hide();
		$("#data_XZ").hide();
	}
}
//点击结算方式汇票复选框，若选中则可以对其关联的文本框进行编辑，若不选中则进行灰化处理，并清空其关联的文本框
function OnclickJSFSHP(obj){
	if(obj.checked || $("#JSFSHK-0").attr("checked")||$("#JSFSPOS-0").attr("checked")){
		//结算方式汇票与POS复选框只要有一个被勾选，显示收款人，开户行，收款账号信息
		$("#title_SKR").show();
		$("#title_SKZH").show();
		$("#title_KHX").show();
		$("#data_SKR").show();
		$("#data_SKZH").show();
		$("#data_KHX").show();
	}
	else{
		//如果没被选中，隐藏收款人，开户行，收款账号信息
		$("#title_SKR").hide();
		$("#title_SKZH").hide();
		$("#title_KHX").hide();
		$("#data_SKR").hide();
		$("#data_SKZH").hide();
		$("#data_KHX").hide();
	}
	if(obj.checked){
		//选中则可以对其关联的文本框进行编辑
        $("#HPJE").attr("readonly","");
		$("#HPJE").attr("style","background:#fff;;width:85px");
		$("#HPJE").rules("remove");
        $("#HPJE").rules("add", {required: true, messages:{required: "请填写汇票金额"} });
	}
	else if($("#JSFSPOS-0").attr("checked")){
		//如果结算POS复选框仍然为选中状态，仅对汇票金额文本框进行灰化
		$("#HPJE").val("");
		$("#HPJE").attr("readonly","readonly"); 
		$("#HPJE").attr("style","background:#efefef;width:85px");
		$("#HPJE").rules("remove"); 
		$("#HPJE").rules("add", {required:false});
	}
	else if($("#JSFSHK-0").attr("checked")){
		//如果结算POS复选框仍然为选中状态，仅对汇票金额文本框进行灰化
		$("#HPJE").val("");
		$("#HPJE").attr("readonly","readonly"); 
		$("#HPJE").attr("style","background:#efefef;width:85px");
		$("#HPJE").rules("remove"); 
		$("#HPJE").rules("add", {required:false});
	}
    else{
    	//不选中则进行灰化处理，并清空其关联的文本框
		$("#KHX").val("");
		$("#SKZH").val("");
		$("#SKR").val("");
		$("#HPJE").val("");
		$("#HPJE").attr("readonly","readonly"); 
		$("#HPJE").attr("style","background:#efefef;width:85px");
		$("#HPJE").rules("remove"); 
		$("#HPJE").rules("add", {required:false});
	}
	
	/*
	if(obj.checked){
		$("#HPJE").attr("readonly","");
		$("#HPJE").attr("style","background:#fff;;width:85px");
		$("#HPJE").rules("remove");
        $("#HPJE").rules("add", {required: true, messages:{required: "请填写汇票金额"} });
	}
	else{
		$("#HPJE").val("");
		$("#HPJE").attr("readonly","readonly"); 
		$("#HPJE").attr("style","background:#efefef;width:85px");
		$("#HPJE").rules("remove"); 
		$("#HPJE").rules("add", {required:false});
	}*/
}
//点击结算方式挂账复选框，若选中则可以对其关联的文本框进行编辑，若不选中则进行灰化处理，并清空其关联的文本框
function OnclickJSFSGZ(obj){
	if(obj.checked){
		$("#GZJE").attr("readonly","");
		$("#GZJE").attr("style","background:#fff;;width:85px");
		$("#GZJE").rules("remove");
		$("#GZJE").rules("add", {required: true, messages:{required: "请填写挂账金额"} });
	}
	else{
		$("#GZJE").val("");
		$("#GZJE").attr("readonly","readonly"); 
		$("#GZJE").attr("style","background:#efefef;width:85px");
		$("#GZJE").rules("remove");  
		$("#GZJE").rules("add", {required:false});
	}
}
//点击结算方式POS复选框
function OnclickJSFSPOS(obj){
	if(obj.checked||$("#JSFSHK-0").attr("checked") || $("#JSFSHP-0").attr("checked")){
		//结算方式汇款与POS复选框只要有一个被勾选，显示收款人，开户行，收款账号信息
		$("#title_SKR").show();
		$("#title_SKZH").show();
		$("#title_KHX").show();
		$("#data_SKR").show();
		$("#data_SKZH").show();
		$("#data_KHX").show();
	}
	else{
		//如果没被选中，隐藏收款人，开户行，收款账号信息
		$("#title_SKR").hide();
		$("#title_SKZH").hide();
		$("#title_KHX").hide();
		$("#data_SKR").hide();
		$("#data_SKZH").hide();
		$("#data_KHX").hide();
	}
	if(obj.checked){
		//如果POS为选中状态，自动获取银行信息，其对应的文本框可以编辑
		$("#POSJE").attr("readonly","");
		$("#POSJE").attr("style","background:#fff;;width:85px");
		$("#POSJE").rules("remove");
        $("#POSJE").rules("add", {required: true, messages:{required: "请填写POS金额"} });
		var jkr = LoadCurrent(); 
		if(jkr==""){
			CoutPersonInfo();//输出("请填写借款人或者报销人信息")提示
			return;
		}
		var pageUrl = "sanbu_fi_getBankInfo.action";
		$.getJSON(pageUrl,{jkr:jkr}, function(result) {
			$.each(result, function(i, field){ 
				$("#KHX").val(field.BANK);
				$("#SKZH").val(field.YXZH);
				$("#SKR").val(field.USERNAME);
			});
		});
	}
	else{
		//不选中则进行灰化处理，并清空其关联的文本框
		$("#KHX").val("");
		$("#SKZH").val("");
		$("#SKR").val("");
		$("#POSJE").val("");
		$("#POSJE").attr("readonly","readonly"); 
		$("#POSJE").attr("style","background:#efefef;width:85px");
		$("#POSJE").rules("remove"); 
		$("#POSJE").rules("add", {required:false});
	}

}
//点击结算方式汇款复选框
function OnclickJSFSHK(obj){
	if(obj.checked||$("#JSFSPOS-0").attr("checked")||$("#JSFSHP-0").attr("checked")){
		//结算方式汇款与POS复选框只要有一个被勾选，显示收款人，开户行，收款账号信息
		$("#title_SKR").show();
		$("#title_SKZH").show();
		$("#title_KHX").show();
		$("#data_SKR").show();
		$("#data_SKZH").show();
		$("#data_KHX").show();
	}
	else{
		//如果没被选中，隐藏收款人，开户行，收款账号信息
		$("#title_SKR").hide();
		$("#title_SKZH").hide();
		$("#title_KHX").hide();
		$("#data_SKR").hide();
		$("#data_SKZH").hide();
		$("#data_KHX").hide();
	}
	if(obj.checked){
		//选中则可以对其关联的文本框进行编辑
		$("#HKJE").attr("readonly","");
		$("#HKJE").attr("style","background:#fff;;width:85px");
		$("#HKJE").rules("remove");
        $("#HKJE").rules("add", {required: true, messages:{required: "请填写汇款金额"} });
	}
	else if($("#JSFSPOS-0").attr("checked")){
		//如果结算POS复选框仍然为选中状态，仅对汇款金额文本框进行灰化
		$("#HKJE").val("");
		$("#HKJE").attr("readonly","readonly"); 
		$("#HKJE").attr("style","background:#efefef;width:85px");
		$("#HKJE").rules("remove"); 
		$("#HKJE").rules("add", {required:false});
	}
	else if($("#JSFSHP-0").attr("checked")){
		//如果结算POS复选框仍然为选中状态，仅对汇款金额文本框进行灰化
		$("#HKJE").val("");
		$("#HKJE").attr("readonly","readonly"); 
		$("#HKJE").attr("style","background:#efefef;width:85px");
		$("#HKJE").rules("remove"); 
		$("#HKJE").rules("add", {required:false});
	}
    else{
    	//不选中则进行灰化处理，并清空其关联的文本框
		$("#KHX").val("");
		$("#SKZH").val("");
		$("#SKR").val("");
		$("#HKJE").val("");
		$("#HKJE").attr("readonly","readonly"); 
		$("#HKJE").attr("style","background:#efefef;width:85px");
		$("#HKJE").rules("remove"); 
		$("#HKJE").rules("add", {required:false});
	}
}


//点击结算方式零余额复选框，若选中则可以对其关联的文本框进行编辑，若不选中则进行灰化处理，并清空其关联的文本框
function OnclickJSFSLYE(obj){
	if(obj.checked){
		$("#LYE").attr("readonly","");
		$("#LYE").attr("style","background:#fff;;width:85px");
		$("#LYE").rules("remove");
        $("#LYE").rules("add", {required: true, messages:{required: "请填写请填写零余额"} });
	} 
	else{
		$("#LYE").val("");
		$("#LYE").attr("readonly","readonly"); 
		$("#LYE").attr("style","background:#efefef;width:85px");
		$("#LYE").rules("remove"); 
		$("#LYE").rules("add", {required:false});
	}
}