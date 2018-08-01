
var ht = {"TITLE_ID":"title_HT","DATA_ID":"data_HT","ID_FIELD_NAME":"HTBH","DESC_FIELD_NAME":"HTMC"};  //合同信息标题ID
var gys = {"TITLE_ID":"title_GYS","DATA_ID":"data_GYS","ID_FIELD_NAME":"GYSBH","DESC_FIELD_NAME":"GYSMC"};  //供应商信息标题ID
var zjxm = {"TITLE_ID":"title_ZJXM","DATA_ID":"data_ZJXM","ID_FIELD_NAME":"ZJXMBH","DESC_FIELD_NAME":"ZJXMMC"};  //在建项目信息标题ID
var prject = {"TITLE_ID":"title_project","DATA_ID":"data_project","ID_FIELD_NAME":"WBS","DESC_FIELD_NAME":"WBSMS"};  //项目辅助核算信息标题ID
var zcxm = {"TITLE_ID":"title_ZCXM","DATA_ID":"data_ZCXM","ID_FIELD_NAME":"ZCXMBH","DESC_FIELD_NAME":"ZCXMMC"};  //项目辅助核算信息标题ID
var typeEmnu = ['COSTCENTER','PRODUCT','VANDER','CUSTOMER','CONTRACT','ZJPRO','ZICHOU'];
  
/**
* 合同报销表单的触发操作
* @modified by zhangqian
*/
$().ready(function() {
	 //根据费用类别
	// setsubFormType($("#FYLB").val());
	//初始表单支付相关显示状态
		initStatus();
		showFZHSHtml($("#FYGSLX").val());
		
 });

document.write("<script src='iwork_js/iwork/fi/zhifu_script.js'></script>");
document.write("<script src='iwork_js/iwork/fi/commonPrint.js'></script>");
document.write("<script src='iwork_js/iwork/fi/fiCommon.js'></script>");
//选择数据字典后，调用此方法
function execDictionarySel(){
	checkBXJE(); 
}

//
function checkBXJE(){
	//changeCount(obj);
	//获取报销金额
	var bxje = $("#SQBXJE").val();
	$('#SJBXJE').val(bxje);  //给实际报销金额赋值
	var htbh = $("#HTBH").val();
	if(htbh!=''&&bxje!=''&&bxje!=0){
		//合同尾款判断  
		var pageUrl = "show_contact_checkExcInfo.action"; 
		$.getJSON(pageUrl,{htbh:htbh,bxje:bxje}, function(result) {
			//alert(result.ISEND);
			if(result.ISEND){
				$("#itemTr_113").show();
			}else{
				$("#itemTr_113").hide();
			}
		});
	}
}


//获取当前表单上借款人字段的值
function LoadCurrent(){
	return $("#BXRZH").val();
}
function CoutPersonInfo(){
	alert("请填写报销人信息");
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
function selectTreeNode(fieldName,treeNode){ //回调函数
		
		$("#FYLBBH").val(treeNode.FYLBBH);//传入结点的值  
		$("#FYLBMC").val(treeNode.name); 
		$("#FYGSLX").val(treeNode.FYGSLX);
		//alert(treeNode.FYGSLX);
		showFZHSHtml(treeNode.FYGSLX);
		logInitPageElementValue();
 }

 function showFZHSHtml(gygslx){
	// alert(gygslx);
 	if(gygslx!=null){
			initFZHSHtml();
			var types = gygslx.split(",");//将类型拆分
			$.each(types,function(idx,item){//遍历每个类型，并设置回调函数
				if(item=='CONTRACT'){
					buildFZHS(true,ht);//合同设置
				}else if(item=='VANDER'){
					buildFZHS(true,gys);//合同设置
				}else if(item=='PRODUCT'){
					buildFZHS(true,prject); 
				}else if(item=='ZJPRO'){
					buildFZHS(true,zjxm); 
				}else if(item=='ZICHOU'){ 
					buildFZHS(true,zcxm); 
				}
			});
		}
 }
 
 function initFZHSHtml(){
 		$.each(typeEmnu,function(idx,item){
				if(item=='CONTRACT'){
					buildFZHS(false,ht);//合同设置
				}else if(item=='VANDER'){
					buildFZHS(false,gys);//合同设置
				}else if(item=='PRODUCT'){
					buildFZHS(false,prject); 
				}else if(item=='ZJPRO'){ 
					buildFZHS(false,zjxm); 
				}else if(item=='ZICHOU'){ 
					buildFZHS(false,zcxm); 
				}
			});
 }
 
 //设置辅助核算
 function buildFZHS(flag,arr){ 
		if(flag){
			$("#"+arr.TITLE_ID).show();
			$("#"+arr.DATA_ID).show();
			 try{
			 $("#"+arr.DESC_FIELD_NAME).rules("remove");
	     $("#"+arr.DESC_FIELD_NAME).rules("add", {required: true});
	     }catch(e){}
		}else{
		 
			$("#"+arr.TITLE_ID).hide();
			$("#"+arr.DATA_ID).hide();
			try{
			$("#"+arr.DESC_FIELD_NAME).rules("remove");
			$("#"+arr.DESC_FIELD_NAME).rules("add", {required: false});
			}catch(e){}
		}
	}
function showJkDirectory(fieldName){
	var url = "iwork_fi_jk_list.action?jkdlist="+$("#"+fieldName).val()+"&defaultField="+fieldName;
	art.dialog.open(url,{   
		id:"subjectBookDialog",  
		title: '借款冲销',
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
		width: 450,
		height: 550
	}); 
	
	
}
 
 //设置WBS信息
function selectWBSNode(fieldName,treeNode){
	//设置WBS
	$("#WBS").val(treeNode.wbsno);
	$("#WBSMS").val(treeNode.name);
}

//设置预算信息
function setBudgetInfo(obj){
	//获取预算行ID
	var subformDataId = obj.value;
	var pageUtil = "sanbu_fi_getBudget_XHHT_JSON.action"; 
	$.getJSON(pageUrl,{subdataid:subformDataId}, function(result) {
		$.each(result, function(i, field){
			
		});
	});
	
}

function changeCount(obj){  
	
	var count = obj.value;
	var pageUrl="getNumberRMB_action.action";
	$.post(pageUrl,{count:count}, function(result) {	
		//alert(result);
		$("#BXJEDX").val(result);
		$("#labelBXJEDX").text(result);
		
	});
	
}


function createFlow(){
	//alert("11");
	var actProcDefId = "HTLXWBQRLC:1:128804";
	var title = "合同履行完毕确认单";
	var HTBH = $("#HTBH").val();
	var HTMC = $("#HTMC").val();
	var url = "processRuntimeStartInstance.action?actDefId=" + actProcDefId +"&HTBH="+HTBH+"&HTMC="+HTMC;
	//addOperateLog(actProcDefId, title);
	openDialog(title,url); 
}


//添加和编辑窗口
function openDialog(title,url){
	var target = "_blank";
	var win_width = window.screen.width;
	var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
}

function showht(){
	
	var htid = $("#HTBH").val(); 
	var url = 'xhht_loadHtInfo_action.action?htid='+htid;
	var target = "_blank";

	var win_width = window.screen.width;
	//alert(win_width);
	var page = window.open(url,target,'width='+win_width+',height=800,top=10,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	//page.location = url;
}
