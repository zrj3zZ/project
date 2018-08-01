$(function(){
    $("#PACTNO").val(parent.$("#PACTNO").val());
    $("#labelPACTNO").html(parent.$("#PACTNO").val());
    $("#PACTNAME").val(parent.$("#PACTTITLE").val());
    $("#labelPACTNAME").html(parent.$("#PACTTITLE").val());
});

/**
 * 合同执行计划审批表判断款项金额
 */
function getFundAmount(){
	
	var instanceId = $("#instanceId").val();
	var pactsun = parent.$("#PACTSUN").val();//基本合同总金额
	var amount1 = $("#AMOUNT").val();
	 var pageUrl="iwork_contract_getFundAmount.action";
	    $.post(pageUrl,{instanceId:instanceId}, function(json) {
	    	var proInfo = JSON.parse(json);
	    	var amount = proInfo.AMOUNT;
	    	var yue=pactsun-amount;
	    	if(amount1>yue){
	    		alert("余额额不允许大于总金额");
	    		$("#AMOUNT").val("");
	    	}
	    	
	    });
}