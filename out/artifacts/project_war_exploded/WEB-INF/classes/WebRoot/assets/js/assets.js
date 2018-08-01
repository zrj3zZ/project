$().ready(function() {
	changeHyCount();
 });
/**
 * 子表保存时回调页面，刷新主表单
 * @param subKey
 * @return
 */
function saveSubFormItemTrigger(subKey){
	var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
		return false;
	}else{
		singleSave();
	}
	window.location.reload();
}

function subformSaveAfterEvent(){
	window.location.reload();
	return true;
}


/**
 * 子表删除后回调事件
 */
function deleteSubFormItemBeforeEvent(strArr){
	
	window.location.reload();
	return true;
}


/**
 * 实时获取大写
 * @param obj
 * @return
 */
function changeHyCount(){
	var count = $("input[name=AMOUNT]").val();//$("#AMOUNT").val();
	var pageUrl="iwork_contract_getNumberRMB.action";
	$.post(pageUrl,{count:count}, function(result) {	
		$("#AMOUNTBIG").val(result);
		$("#labelAMOUNTBIG").text(result);
		
	});
	
}

function sumPrice(){
	//获取数量
	var lysl=parseFloat($("#QUANTITY").val());
	//获取单价
	var price=parseFloat($("#PRICE").val());
	//计算出合计金额并且赋值
	var jehj=Math.round(lysl*price*10000)/10000;
	if(!isNaN(jehj)&&!isNaN(price)){
		$("#AMOUNT").val(jehj);
	  // setSubFormField("AMOUNT",jehj);
	}
}