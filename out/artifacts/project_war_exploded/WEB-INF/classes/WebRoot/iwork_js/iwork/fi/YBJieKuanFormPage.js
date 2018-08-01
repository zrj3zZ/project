 var ht = {"TITLE_ID":"title_HT","DATA_ID":"data_HT","ID_FIELD_NAME":"HTBH","DESC_FIELD_NAME":"HTMC"};  //合同信息标题ID 
var gys = {"TITLE_ID":"title_GYS","DATA_ID":"data_GYS","ID_FIELD_NAME":"GYSBH","DESC_FIELD_NAME":"GYSMC"};  //供应商信息标题ID
var zjxm = {"TITLE_ID":"title_ZJXM","DATA_ID":"data_ZJXM","ID_FIELD_NAME":"ZJXMBH","DESC_FIELD_NAME":"ZJXMMC"};  //在建项目信息标题ID
var prject = {"TITLE_ID":"title_project","DATA_ID":"data_project","ID_FIELD_NAME":"WBS","DESC_FIELD_NAME":"WBSMS"};  //项目辅助核算信息标题ID
var typeEmnu = ['COSTCENTER','PRODUCT','VANDER','CUSTOMER','CONTRACT','ZJPRO','ZICHOU'];

/**
 * 合同借款表单的触发操作
 * @author zhangqian
 */
$().ready(function() {
	
//初始表单支付相关显示状态
	initStatus();
	showFZHSHtml($("#FYGSLX").val());
	setInterval(function(){window.onbeforeunload=null},0);
	window.onbeforeunload = false;
 });
document.write("<script src='iwork_js/iwork/fi/zhifu_hk_script.js'></script>");
document.write("<script src='iwork_js/iwork/fi/commonPrint.js'></script>");

//获取当前表单上借款人字段的值
function LoadCurrent(){
	return $("#JKR").val();
}
function CoutPersonInfo(){
	alert("请填写借款人信息");
}
function selectTreeNode2(fieldName,treeNode){
		//设置借款人部门
		$("#JKKM").val(treeNode.DATAID);
		$("#JKKMMS").val(treeNode.name);
 }

function selectTreeNode(fieldName,treeNode){ 
	$("#FYLBBH").val(treeNode.FYLBBH);  
	$("#FYLBMC").val(treeNode.name); 
	$("#FYGSLX").val(treeNode.FYGSLX);
	showFZHSHtml(treeNode.FYGSLX);
} 

function showFZHSHtml(gygslx){
	if(gygslx!=null){
		initFZHSHtml();
		var types = gygslx.split(",");
		$.each(types,function(idx,item){
			if(item=='CONTRACT'){
				buildFZHS(true,ht);//合同设置
			}else if(item=='VANDER'){
				buildFZHS(true,gys);//合同设置
			}else if(item=='PRODUCT'){
				buildFZHS(true,prject); 
			}else if(item=='ZJPRO'){
				buildFZHS(true,zjxm); 
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
		$("#DXJE").val(result);
		$("#labelDXJE").text(result);
		
	});
	
}