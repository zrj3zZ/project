$(function(){
	$("#YSKZBH").val(parent.$("#YSKZBH").val());
});
/**
 * 
 * @param id 数据字典ID
 * @param rd 行数据
 * @return
 */
/*
 * 页面用json异步刷新数据
 * 
 */
function execDictionarySel(id,rd){
	var flag = false;
	var yskzbh = window.parent.document.getElementById('YSKZBH').value;

	var xmbh =rd.PROJ_CODE;//子表的项目编号	
	var instanceID=$("#instanceId").val();//流程实例id		
	var pageurl = "iwork_fi_plan_XMRCFormYanZheng.action";
	$.post(pageurl,{xmbh:xmbh,instanceId:instanceID},function(json){
			var res = JSON.parse(json);
			//alert(res.flag);
			if(res.flag){
				var pageurl = "iwork_fi_plan_XMRCForm.action";
				$.post(pageurl,{xmbh:xmbh,yskzbh:yskzbh},function(json){
					 var proInfo = JSON.parse(json);
				     $("#KYYE").val(proInfo.kyye);
				     $("#JZSYZC").val(proInfo.syyzc);
				     $("#JZSYYZC").val(proInfo.syyzc);
				     $("#NDZCCBZB").val(proInfo.dndzccbzb);	    		
				});	
				
			}else{
				alert("型号重复，请重新选择！");
				$("#XMBH").val(null);
				 $("#XMMC").val(null);
				 $("#KYYE").val(null);
			     $("#JZSYZC").val(null);
			     $("#JZSYYZC").val(null);
			     $("#NDZCCBZB").val(null);
			}
			
		 });
			
}
/*
 * 表单验证可用余额与本月预算
 */
function CompareAmount(){
	var kyye=$("#KYYE").val();//可用余额
	var byys=$("#BYYS").val();//本月预算
	if(isNaN(parseInt(kyye))){
		alert("没有可用余额，请选择项目!");
		$("#BYYS").val(null);
	}
	if(parseInt(byys)>parseInt(kyye)){
		alert("本月预算不得大于可用余额");
		$("#BYYS").val(null);
   }
}
