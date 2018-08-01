 $().ready(function() {
	 
	 alert("页面初始化");
 });
 
   function loadBankInfo(obj){
	   
	   alert("脚本测试");
   }
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
   		$("#BXJEDX").val(result);
   		$("#labelBXJEDX").text(result);
   		
   	});
   	
   }