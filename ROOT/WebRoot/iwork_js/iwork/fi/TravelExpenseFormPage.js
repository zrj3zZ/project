 
 
 $().ready(function() {
	 //根据费用类别
	 var val=$('input:radio[name="XH"]:checked');
	 setTypeParams(val);
 });

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
function setTypeParams(obj){
	//如果报销类型为型号项目，则型号项目必填
	if(obj.value=='型号费用'){
		$("#title_XH").show();
		$("#data_XH").show();
		 $("#XH").rules("remove");
         $("#XH").rules("add", {required: true, messages:{required: "请填写型号信息"} });
	}else{
		$("#title_XH").hide();
		$("#data_XH").hide();
		 $("#XH").rules("remove"); 
		 $("#XH").rules("add", {required:false});
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