  /**
 * 合同差旅报销表单的触发操作
 * @modified by zhangqian
 */
$().ready(function() {
	 //根据费用类别
	// setsubFormType($("#FYLB").val());
	//初始表单支付相关显示状态
		initStatus();
 });
document.write("<script src='iwork_js/iwork/fi/zhifu_script.js'></script>");

//获取当前表单上借款人字段的值
function LoadCurrent(){
	return $("#BXRZH").val();
}
function CoutPersonInfo(){
	alert("请填写报销人信息");
}
function showInfo(obj){
	
	
}
function selectRadioAddress(fieldName,treeNode){
	if(fieldName=='BXRZH'){
		var userid = treeNode.userId;
		$("#BXDWBH").val(treeNode.deptId);
		$("#BXDWMC").val(treeNode.deptname); 
//		$.getJSON("iwork_hr_legal_getUserCompanyInfo.action",{userid:userid}, function(result) {
//			$.each(result, function(i, field){
//				alert('');
//				//$("#BXDWBH").val(field.LEGAL_COMY_NO);
//				//$("#BXDWMC").val(field.LEGAL_COMY_NAME);
//			});
//		});
		//获取费用类别列表
	}
 }


function showFISubjectDlg(defaultField){   
	var url = "iwork_fi_subject_list.action?purview=true&defaultField="+defaultField;
	art.dialog.open(url,{   
		id:"subjectBookDialog",  
		title: '费用科目',
		cover:true,
		loadingText:'正在加载中,请稍后...',
		bgcolor:'#999', 
		rang:true,
		cache:true,
		lock: true,
		iconTitle:false,
		extendDrag:true,
		autoSize:true, 
		resize:true,
		pading: 0,
		width: 350,
		height: 550
	}); 
//	$.dialog.data("paramObj",obj);
} 
function selectTreeNode(fieldName,treeNode){
	if(fieldName=='FYLB'){
		var userid = treeNode.userId;
		//设置报销人部门
		$("#FYLBBM").val(treeNode.FYLBBH);
		$("#FYLB").val(treeNode.FYLBMC);
	}
 }
function setsubFormType(fygs){
	//获取费用归属类型
	if(fygs=="COSTCENTER"){
		$("#subTableDiv121").show(); //成本中心 费用明细子表
		$("#subformTr_121").show(); //成本中心 费用明细子表
		$("#subTableDiv122").show(); //交通费明细
		$("#subformTr_122").show(); //交通费明细
		$("#subTableDiv123").show(); //招待费费明细
		$("#subTableDiv123").show(); //招待费费明细
		//隐藏产品费用明细子表
		$("#subformTr_120").hide();
		$("#subTableDiv120").hide();
		
		//显示成本中心标题
		$("#title_JSFSBM2").show();
		$("#CBZXMC").show();
		//
	}else if(fygs=="PRODUCT"){
		$("#subTableDiv120").show();
		$("subformTr_120").show();
		$("#subTableDiv121").hide(); //成本中心 费用明细子表
		$("#subformTr_121").hide(); //成本中心 费用明细子表
		$("#subTableDiv122").hide(); //交通费明细
		$("#subformTr_122").hide(); //交通费明细
		$("#subTableDiv123").hide(); //招待费费明细
		$("#subformTr_123").hide(); //招待费费明细
		//隐藏成本中心标题
		$("#title_JSFSBM2").hide();
		$("#CBZXMC").hide();
		
	}
}


function selectWBSNode(fieldName,treeNode){
	//设置WBS
	$("#WBS").val(treeNode.wbsno);
	$("#WBSMS").val(treeNode.name);
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