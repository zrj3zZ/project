var ht = {"TITLE_ID":"title_HT","DATA_ID":"data_HT","ID_FIELD_NAME":"HTBH","DESC_FIELD_NAME":"HTMC"};  //合同信息标题ID
var gys = {"TITLE_ID":"title_GYS","DATA_ID":"data_GYS","ID_FIELD_NAME":"GYSBH","DESC_FIELD_NAME":"GYSMC"};  //供应商信息标题ID
var zjxm = {"TITLE_ID":"title_ZJXM","DATA_ID":"data_ZJXM","ID_FIELD_NAME":"ZJXMBH","DESC_FIELD_NAME":"ZJXMMC"};  //在建项目信息标题ID
var prject = {"TITLE_ID":"title_project","DATA_ID":"data_project","ID_FIELD_NAME":"WBS","DESC_FIELD_NAME":"WBSMS"};  //项目辅助核算信息标题ID
var zcxm = {"TITLE_ID":"title_ZCXM","DATA_ID":"data_ZCXM","ID_FIELD_NAME":"ZCXMBH","DESC_FIELD_NAME":"ZCXMMC"};  //项目辅助核算信息标题ID
var typeEmnu = ['COSTCENTER','PRODUCT','VANDER','CUSTOMER','CONTRACT','ZJPRO','ZICHOU'];

/**
 * 差旅费用报销表单（非合同类）的触发操作
 * @modified by zhangqian
 */
$().ready(function() {
	 //根据费用类别
	// setsubFormType($("#FYLB").val());
	//初始表单支付相关显示状态
	initStatus();
		showFZHSHtml($("#FYGSLX").val());
	//	sumBXJE();
		$("#SJBXJE").val($("#SQBXJE").val());
		//changeCount($("[name='SQBXJE']").val());
		loadXhBmInfo();
		autoCalHzzje();
		autoAdd();
		clearFzhs();
 });
document.write("<script src='iwork_js/iwork/fi/zhifu_script.js'></script>");
document.write("<script src='iwork_js/iwork/fi/commonPrint.js'></script>");
document.write("<script src='iwork_js/iwork/fi/fiCommon.js'></script>");
//获取当前表单上借款人字段的值
function LoadCurrent(){
	return $("#BXRZH").val();
	
}

function autoCalHzzje(){
	var sjbxje = $("#HZBXJE").val()*1+$("#BZJE").val()*1;
	
	 $("#SJBXJE").val(sjbxje);
}

function showXhBmInfo(obj){
	//initStatus();
	var type = obj.value;
	if(type=="XH"){
		$("#xh_tr").show();
		$("#bm_tr").hide();
		$("#BMYSKM").val(null);
		$("#BMYSJE").val(null);
		$("#BMYSMC").val(null);
		// 显示型号助理员
		$("#title_XHZL").show();
		$("#data_XHZL").show();
	}else{
	//	initStatus();
		$("#xh_tr").hide();
		$("#bm_tr").show();
		$("#XHBH").val(null);
		$("#XHMC").val(null);
		$("#YDYSJE").val(null);
		// 隐藏型号助理员
		$("#title_XHZL").hide();
		$("#data_XHZL").hide();
		$("#XHZLBH").val(null);
		$("#XHZL").val(null);
	}
}

function loadXhBmInfo(){
	var type = $('input:radio:checked').val();
	if(type=="XH"){
		$("#xh_tr").show();
		$("#bm_tr").hide();
		$("#BMYSKM").val(null);
		$("#BMYSJE").val(null);
		$("#BMYSMC").val(null);
		// 显示型号助理员
		$("#title_XHZL").show();
		$("#data_XHZL").show();
	}else{
		initStatus();
		$("#xh_tr").hide();
		$("#bm_tr").show();
		$("#XHBH").val(null);
		$("#XHMC").val(null);
		$("#YDYSJE").val(null);
		// 隐藏型号助理员
		$("#title_XHZL").hide();
		$("#data_XHZL").hide();
		$("#XHZLBH").val(null);
		$("#XHZL").val(null);
	}
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
function selectTreeNode(fieldName,treeNode){ 
		//设置报销人部门
		$("#FYLBBH").val(treeNode.FYLBBH);  
		$("#FYLBMC").val(treeNode.name); 
		$("#FYGSLX").val(treeNode.FYGSLX);
		showFZHSHtml(treeNode.FYGSLX);
		clearFzhs();
 }
function clearFzhs(){
	var fzhsx =[ht,gys,prject,zjxm,zcxm];
	for(var j=0;j<fzhsx.length;j++){
		var item = $("#"+fzhsx[j].ID_FIELD_NAME);
		if(item.parent().css("display") === "none"){
			$.each(item.parent().find("input"),function(index,entry){
				$(entry).attr("_value","");
				$(entry).val("");
			});
		}
	}
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
				}else if(item=='ZICHOU'){ 
					buildFZHS(true,zcxm); 
				}
			});
			
			//获取费用类别是否是大型试验、售后保障类费用
			var fylbbh = $("#FYLBBH").val();
			if(fylbbh=="dxsy"){
				$("#itemTr_110").show();
				 $("#SYBZJE").rules("remove");
				 $("#SYBZJE").rules("add", {required: true});
			}else{
				$("#itemTr_110").hide();
				try{
					$("#SYBZJE").rules("remove");
					$("#SYBZJE").rules("add", {required: false});
					}catch(e){}
			}
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

function selectWBSNode(fieldName,treeNode){
	//设置WBS
	$("#WBS").val(treeNode.wbsno);
	$("#WBSMS").val(treeNode.name);
}
function sumBXJE(){
	if($("#SJBXJE").val()==""){$("#SJBXJE").val("0");}
	if($("#SYBZJE").val()==""){$("#SYBZJE").val("0");}
	if($("#SQBXJE").val()==""){$("#SQBXJE").val("0");}
	$("#SJBXJE").val(Number($("#SYBZJE").val())+Number($("#SQBXJE").val()));
}
/**
 * 实时获取大写金额
 * @param obj
 * @return
 */
function changeCount(obj){  
	$("#SJBXJE").val($("#SQBXJE").val());
	$("#HZBXJE").val($("#SQBXJE").val());
	// clfCheck();
	var count = obj;
	var pageUrl="getNumberRMB_action.action";
	$.post(pageUrl,{count:count}, function(result) {	
		//alert(result);
		$("#BXJEDX").val(result);
		$("#labelBXJEDX").text(result);
		
	});
	
}

function clfCheck(){	
	/*var ysje = $("#YDYSJE").val();	
	var bxje = $("#SQBXJE").val();
	var bxyf = $("#BXYF").val();
	var bxlx = $("input:radio:checked").val(); 
	var bmyskm = $("#BMYSKM").val();
	var xhbh = $("#XHBH").val();
	var bmysje = $("#BMYSJE").val();
	//alert(bmysje);
		var pageUrl="clfcheck.action";
		$.post(pageUrl,{bmysje:bmysje,xhbh:xhbh,bmyskm:bmyskm,bxlx:bxlx,ysje:ysje,bxje:bxje,bxyf:bxyf}, function(json) {	
			 var proInfo = JSON.parse(json);
			 var flag = proInfo.flag;
	      if(String(flag)=="false"){	    	  
	           alert("预算金额不足，请追加预算！");
	          $("#SQBXJE").val(null);	     
	          $("#SJBXJE").val(null);
	        //  $("[name='BXJEDX']").text(null);
	          $("#BXJEDX").val(null);
	  		  $("#labelBXJEDX").text('零元整');
	      }			
		});*/
	}

function showClf(){
//	var ysje = $("#YDYSJE").val();	
//	var bxje = $("#SQBXJE").val();
//	var bxyf = $("#BXYF").val();
//	var bxlx = $("input:radio:checked").val(); 
//	var bmyskm = $("#BMYSKM").val();
//	var xhbh = $("#XHBH").val();
//	var bmysje = $("#BMYSJE").val();
	var ysje = $("[name='YDYSJE']").val();	
	var bxje = $("[name='SQBXJE']").val();
	var bxyf = $("[name='BXYF']").val();
	var bxlx = $("input:radio:checked").val(); 
	var bmyskm = $("[name='BMYSKM']").val();
	var xhbh = $("[name='XHBH']").val();
	var bmysje = $("[name='BMYSJE']").val();
	if(bxlx=='XH'){
		if(ysje==0){
			alert("请选择预算金额！");
			return false;
		}
	}else{
		if(bmysje==0){
			alert("请选择预算金额！");
			return false;
		}
		
	}	
	var url = 'show_contact_clf.action?ysje='+ysje+'&bxje='+bxje+'&bxyf='+bxyf+'&bxlx='+bxlx+'&bmyskm='+bmyskm+'&xhbh='+xhbh+'&bmysje='+bmysje+'&dataId='+$('dataId').val();
	var target = "_blank";
	var win_width = window.screen.width;
	var page = window.open(url,target,'width=500,height=150,top=100,left=550,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');

}
//核准报销总金额=补助金额+核准报销金额
function autoAdd(){
	$("#SJBXJE").parent().append($("<a href='javascript:void(0)' onclick='autoSub()'></a>").attr("id","autoAdd").html("自动生成"));
	$("#HZBXJE").val($("#SQBXJE").val());
}
function autoSub(){
	var total = parseFloat($("#HZBXJE").val())+parseFloat($("#BZJE").val());
	$("#SJBXJE").val(total);
}