var ht = {"TITLE_ID":"title_HT","DATA_ID":"data_HT","ID_FIELD_NAME":"HTBH","DESC_FIELD_NAME":"HTMC"};  //合同信息标题ID
var gys = {"TITLE_ID":"title_GYS","DATA_ID":"data_GYS","ID_FIELD_NAME":"GYSBH","DESC_FIELD_NAME":"GYSMC"};  //供应商信息标题ID
var zjxm = {"TITLE_ID":"title_ZJXM","DATA_ID":"data_ZJXM","ID_FIELD_NAME":"ZJXMBH","DESC_FIELD_NAME":"ZJXMMC"};  //在建项目信息标题ID
var prject = {"TITLE_ID":"title_project","DATA_ID":"data_project","ID_FIELD_NAME":"WBS","DESC_FIELD_NAME":"WBSMS"};  //项目辅助核算信息标题ID
var zcxm = {"TITLE_ID":"title_ZCXM","DATA_ID":"data_ZCXM","ID_FIELD_NAME":"ZCXMBH","DESC_FIELD_NAME":"ZCXMMC"};  //项目辅助核算信息标题ID
var typeEmnu = ['COSTCENTER','PRODUCT','VANDER','CUSTOMER','CONTRACT','ZJPRO','ZICHOU'];
/**
 * 会议费用报销表单（非合同类）的触发操作
 * @modified by zhangqian
 */
$().ready(function() {
	 //根据费用类别
	// setsubFormType($("#FYLB").val());
	//初始表单支付相关显示状态
		initStatus();
		 loadXhBmInfo();
		showFZHSHtml($("#FYGSLX").val());
		clearFzhs();
 });
document.write("<script src='iwork_js/iwork/fi/zhifu_script.js'></script>");
document.write("<script src='iwork_js/iwork/fi/commonPrint.js'></script>");
document.write("<script src='iwork_js/iwork/fi/fiCommon.js'></script>");

//获取当前表单上借款人字段的值
function LoadCurrent(){
	return $("#BXRZH").val();
}
function CoutPersonInfo(){
	alert("请填写报销人信息");
}
function plusValue(){ 
	var hyje=$("#HYJE").val();
	var psfje=$("#PSFJE").val();
	var hyzje=Number(hyje)+Number(psfje);
	$("#HYZJE").val(hyzje);
	$("#SQBXJE").val(hyzje);
	$("#SJBXJE").val(hyzje);
	//changeCount();
	changeHyCount(hyzje)
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
function showXhBmInfo(obj){
	var type = obj.value;
	if(type=="XH"){
		$("#xh_tr").show();
		$("#bm_tr").hide();
		$("#BMYSKM").val(null);
		$("#BMYSJE").val(null);
		$("#BMYSMC").val(null);
		$("#BXYF").val(null);
		// 显示型号助理员
		$("#title_XHZL").show();
		$("#data_XHZL").show();
	}else{
		$("#xh_tr").hide();
		$("#bm_tr").show();
		$("#XHBH").val(null);
		$("#XHMC").val(null);
		$("#YDYSJE").val(null);
		$("#BXYF").val(null);
		// 隐藏型号助理员
		$("#title_XHZL").hide();
		$("#data_XHZL").hide();
		$("#XHZLBH").val(null);
		$("#XHZL").val(null);
	}
}

function loadXhBmInfo(){
	var type = $('input[name="BXLX"]:checked').val();
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
 
//设置合同辅助核算
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
		$("#BXJEDX").val(result);
		$("#labelBXJEDX").text(result);
		
	});
	
}


/**
 * 查看会议报销预算的详细信息
 * @param obj
 * @return
 */
function showxhbh(){ 	
	//获取报销类型
	var bxlx= $("input:radio:checked").val();
	//部门预算金额
	var bmysje = $("[name='BMYSJE']").val();
	//获取型号编号
	var xhbh=$("[name='XHBH']").val();
	//获取报销月份
	var bxyf = $("[name='BXYF']").val();
	// 获取部门预算科目
	var bmyskm = $("[name='BMYSKM']").val();
	//获取月度预算金额
	var ydysje=$("[name='YDYSJE']").val();
	var url = 'search_HyYusuan_information.action?&bxyf='+bxyf+'&bmysje='+bmysje+'&bmyskm='+bmyskm+'&bxlx='+bxlx+'&xhbh='+xhbh+'&ydysje='+ydysje;
	var target = "_blank";
	var win_width = window.screen.width;
	// alert(win_width);
	var page = window
			.open(
					url,
					target,
					'width=500,height=150,top=100,left=550,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	// page.location = url;

	
}


/**
 * 验证会议预算金额
 * @param obj
 * @return
 */
function YzHyYuSuan(obj){ 
	
	changeCount(obj)
	/*$("#SJBXJE").val(obj.value);
	//获取报销类型
	var bxlx= $("input:radio:checked").val();
	
	//部门预算金额
	var bmysje = $("#BMYSJE").val();
	//获取型号编号
	var xhbh=$("#XHBH").val();
	//获取申请报销金额
	var sqbxje=$("#SQBXJE").val();
	//获取报销月份
	var bxyf = $("#BXYF").val();
	// 获取部门预算科目
	var bmyskm = $("#BMYSKM").val();
	//获取月度预算金额
	var ydysje=$("#YDYSJE").val();
	
	var pageUrl = 'yanZheng_HyYuSuan.action';
	$.post(pageUrl, {
		sqbxje:sqbxje,
		bmyskm : bmyskm,
		bmysje : bmysje,
		bxlx : bxlx,
		bxyf : bxyf,
		xhbh : xhbh,
		ydysje : ydysje
	}, function(json) {
		var proInfo = JSON.parse(json);
		var yang = proInfo.flag;
		var item1 = String(yang);
		if (item1 == "false") {
			alert("预算金额不足，请追加预算！");
			$("#SQBXJE").val(null);
			$("#SJBXJE").val(null);
			$("#BXJEDX").val("");
			$("#labelBXJEDX").html("零元整");
		}
	});*/

	
}

/**
 * 实时获取会议金额大写
 * @param obj
 * @return
 */
function changeHyCount(count){
	//var count = $("#HYZJE").val();
	var pageUrl="getNumberRMB_action.action";
	$.post(pageUrl,{count:count}, function(result) {	
		//alert(result);
		$("#HYZJEDX").val(result);
		$("#labelHYZJEDX").text(result);
		$("#BXJEDX").val(result);
		$("#labelBXJEDX").text(result);
		
	});
	
}

