 $().ready(function() {
	 
	 //根据费用类别
	 setsubFormType($("#FYGS").val());
 });

function showInfo(obj){
	
	
}
function selectRadioAddress(fieldName,treeNode){
	if(fieldName=='BXRZH'){
		var userid = treeNode.userId;
		//设置报销人部门
		$("#BXBM").val(treeNode.deptname);
		//获取报销人费用类别
		$.getJSON("iwork_hr_legal_getUserCompanyInfo.action",{userid:userid}, function(result) {
			$.each(result, function(i, field){
				$("#SSGSBM").val(field.LEGAL_COMY_NO);
				$("#SSGSMC").val(field.LEGAL_COMY_NAME);
			});
		});
		//获取费用类别列表
		
	}
 }



function showbook(defaultField){  
	var url = "iwork_fi_subject_book.action?purview=true&defaultField="+defaultField;
	art.dialog.open(url,{
		title: '费用类别',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width: 350,
		height: 550
	 });
//	$.dialog.data("paramObj",obj);
}
function selectTreeNode(fieldName,treeNode){
	if(fieldName=='FYLBMC'){
		var userid = treeNode.userId;
		//设置报销人部门
		$("#FYLBNO").val(treeNode.FYLBNO);
		$("#FYLBMC").val(treeNode.FYLBMC);
		
		var yslx = treeNode.YSLX; 
		if(yslx!=""){
			var list = yslx.split("|"); 
			$("#YSLX").empty();
			$(list).each(function(index,element){
				//预算类型动态添加  
				 $("#YSLX").append("<option value='"+element+"'>"+element+"</option>");  //为Select追加一个Option(下拉项) 
			}); 
		}  
		//获取费用归属类型
		var fygs = treeNode.FYGSLX;
		$("#FYGS").val(fygs);
		setsubFormType(fygs);
		
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