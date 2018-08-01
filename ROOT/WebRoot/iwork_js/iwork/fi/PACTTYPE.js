$().ready(function() {
	//判断当前合同类型,是否显示父合同ID
	showJhTable();
	
 });

$(function(){
	validationUpdate();
});
function validationUpdate(){ 
    //获取合同编号
	var itemno = $("#PACTNO").val();
    var pageUrl="iwork_contract_ivoicePerformUpdate.action";
    $.post(pageUrl,{itemno:itemno}, function(json) {
    	var proInfo = JSON.parse(json);
    	var ticketNum = proInfo.ticketNum;	
    	//获取合同总金额
    	var sumNum = proInfo.sumNum;		
    	var count=sumNum-ticketNum;	
    	var counts = String(count);
       	var sum = String(sumNum);
    	$("#BLANCEAMOUNT").val(counts);
    	
    });
}
/**
 * 汇率计算
 * @return
 */
function estimateCurrency(){
	var currency = $("#CURRENCY").val();
	if(currency==1){
		 $("#PARITIES").val("1");
	}else if(currency==2){
		 $("#PARITIES").val("6.2132");
	}else if(currency==3){
		 $("#PARITIES").val("9.1076");
	}else if(currency==4){
		 $("#PARITIES").val("0.8017");
	}else if(currency==5){
		 $("#PARITIES").val("6.5519");
	}
	var parities = $("#PARITIES").val();
	var pactsun = $("#PACTSUN").val();
	var sum = pactsun*parities;
	var uu=Math.floor(sum);
    $("#CONVERT").val(uu);
	$("#labelCONVERT").text(uu);
	
	var count = $("#CONVERT").val();
	var pageUrl="iwork_contract_getNumberRMB.action";
	$.post(pageUrl,{count:count}, function(result) {	
		$("#SAYTOTAL").val(result);
		$("#labelSAYTOTAL").text(result);
	});
}
/**
 * 主合同/父合同单选按钮
 * @return
 */
function showJhTable(){
	if($("#FATHERPACTNO").val()==''){
	
	}else{
		$("#PACTNATURE1").attr("checked",true);
	}
	var type = $('input[name="PACTNATURE"]:checked').val();
	if(type=="1"){
		$("#PACTNO1").hide();
		$("#PACTNO2").hide();
		}else{
			$("#PACTNO1").show();
			$("#PACTNO2").show();
			
		}
}
/**
 * 金额大写转换
 * @param obj
 * @return
 */
function changeCount(obj){  
	var count = obj.value;
	var pageUrl="iwork_contract_getNumberRMB.action";
	$.post(pageUrl,{count:count}, function(result) {	
		$("#SAYTOTAL").val(result);
		$("#labelSAYTOTAL").text(result);
	});
	var currncy = $("#CURRENCY").val();
	if(currncy==1){
		$("#CONVERT").val(count);
		$("#labelCONVERT").text(count);
	}else{
		estimateCurrency();
	}
}
//发票执行表单验证
function validation(){ 
	//获取页面dataid
	var dataid=$("#dataid").val();
	//获取发票金额
	var amount = $("#AMOUNT").val();
	//获取合同编号
	var itemno = $("#PACTNO").val();
	var pageUrl="iwork_contract_ivoicePerformValidation.action";
	$.post(pageUrl,{itemno:itemno,dataid:dataid}, function(json) {
		var proInfo = JSON.parse(json);
		//获取已执行的发票金额
		var ticketNum = proInfo.ticketNum;
		//获取合同总金额
		var sumNum = proInfo.sumNum;
		//获取当前所要编辑记录的发票金额
		var eventNum = proInfo.eventNum;
	if(dataid==0){		
		var count=sumNum-ticketNum-amount	
		var counts = String(count);
		var sum = String(sumNum);
		  if ( counts<0) {
			  alert("开票余额不足！");
			  $("#AMOUNT").val(null);
			  $("#BLANCEAMOUNT").val(sum);//开票余额重新复制
	        }else{
			 $("#BLANCEAMOUNT").val(counts);
		  }
	}else{			
			var count=sumNum-ticketNum+eventNum-amount;		
			var counts = String(count);
			  if ( counts<0) {
				alert("开票余额不足！");
				$("#AMOUNT").val(null);
			    //$("#BLANCEAMOUNT").val("");
				//$("#labelBLANCEAMOUNT").text("");
			  }else{
				$("#BLANCEAMOUNT").val(counts);
			   }			
		}
	});
}
//获取总金额、计划金额、执行金额、等
function getAccountsNum(){ 
	var formid = $("#formid").val();
	//alert(formid);
	var dataid=$("#dataid").val();
	var itemno = $("#PACTNO").val();
    var pageUrl="iwork_contract_getAccountsNum.action";
    $.post(pageUrl,{itemno:itemno,dataid:dataid,formid:formid}, function(json) {
    	var proInfo = JSON.parse(json);
    	var sumNum = proInfo.sumNum;//合同总金额
    	var planNum=proInfo.planNum;//计划收付款金额
    	var ticketPlanNum=proInfo.ticketPlanNum;//发票计划金额
    	var realNum=proInfo.realNum;//实际收付款金额
    	var ticketNum = proInfo.ticketNum;//发票执行金额	
    	var accountsNum = proInfo.accountsNum;//应收付款金额
    	var eventNum = proInfo.eventNum;	
    	var count=sumNum-ticketNum-planNum-ticketPlanNum-realNum;
    	var counts=sumNum-ticketNum-planNum-ticketPlanNum+eventNum-realNum;
    	var amount=$("#AMOUNT").val();
		    	if(dataid==0&&amount!=""){		    		
		    	     if(amount>count){		    	    	 
		    		    alert("当前金额不允许大于合同总金额");
		    		     $("#AMOUNT").val(null);		    		    
		    	      }  	    	   	    	   	     
		    	} else{  		    		
		    		  if(amount>counts){		    			    			  
		      		    alert("当前金额不允许大于合同总金额");
		      		     $("#AMOUNT").val(null);		      		     
		      	      }  	    	   	    	   	        		  
		    	 }  		    	
    		
    });	
}

