$().ready(function() {
	loadSubForm();
	//$('#HTRMBJE').val($('#HTJE').val()*$('#JSHL').val());
 });
document.write("<script src='iwork_js/iwork/fi/commonPrint.js'></script>");
function selectMultiWBSNode(defaultField,treeNode){
	var taskName = treeNode.PROJ_NAME;
	var taskNo = treeNode.PROJ_CODE;
	$("#TASKNAME").val(taskName);
	$("#TASKNO").val(taskNo);
	$("#XHBH").val(treeNode.TOP_ID);
	$("#XHMS").val(treeNode.TOP_NAME);
}
function hideCompoent(obj){
	var type = obj.value;
	if(type=="WORD"){
		$("#d_word").show();
		$("#d_pdf").hide();
		
	}else{
		$("#d_word").hide();
		$("#d_pdf").show();
	}
	
}

function showJhTable(obj){
	
	var type = obj.value;
	
	if(type=="jg"){
		$("#d_jhtable").show();
		$("#JGZBZD").show();
		
		$("#JGZBZD3").show();
		}else{
			$("#d_jhtable").hide();
			//$("#JGZBZD1").hide();
			//$("#JGZBZD2").hide();
			$("#JGZBZD3").hide();
		}
	if(type=="xinghao"){
		$("#d_jhtable").show();
		$("#d_xhtable").show();
	}else{
		$("#d_xhtable").hide();
	}
	if(type=="qt"){
		$("#QTHTYS").show();
	}else{
		$("#QTHTYS").hide();
	}
	loadSubForm();
	
}

function showJginfo(obj){
	var type = obj.value;
	if(type=="yes"){
		$("#JGZBZD1").show();
		$("#JGZBZD2").show();
	}else{
		$("#JGZBZD1").hide();
		$("#JGZBZD2").hide();
	}
	
}
//是否开口合同，选择是，合同金额不允许填写
function doContractSum(obj){
	if(obj.value==1){
		$("#HTJE").val("");
		$("#HTJE").attr("readonly","readonly");
		$("#HTJE").attr("style","background:#efefef;width:85px");
		$("#HTJE").rules("remove");
		$("#HTJE").rules("add", {required:false});
		$("#labelHTJEDX").text("");
		$("#HTJEDX").val("");
		$("#HTRMBJE").val("");
		
    } 
	else{
		$("#HTJE").attr("readonly",""); 
		$("#HTJE").attr("style","background:#fff;;width:85px");
		$("#HTJE").rules("remove"); 
		$("#HTJE").rules("add", {required: true, messages:{required: "请填写合同金额"} });
		
	}
}
function loadSubForm(){
	var type = $('input[name="HTGK"]:checked').val();
	var contenttype = $('input[name="HTZWLX"]:checked').val();
	var isZb = $('input[name="SFZTBXM"]:checked').val();
	var isOpenContract = $('input[name="SFWKKHT"]:checked').val();
	if(type=="jg"){
		$("#d_jhtable").show();
		if(isZb=="yes"){
			$("#JGZBZD1").show();
			$("#JGZBZD2").show();
		}else{
			$("#JGZBZD1").hide();
			$("#JGZBZD2").hide();
		}
		$("#JGZBZD").show();
		$("#JGZBZD3").show();
		}else{
			$("#d_jhtable").hide();
			
			$("#JGZBZD3").hide();
		}
	if(type=="xinghao"){
		$("#d_xhtable").show();
		$("#d_jhtable").show();
	}else{
		$("#d_xhtable").hide();
	}
	if(type=="qt"){
		$("#QTHTYS").show();
		$("#d_xhtable").hide();
		$("#d_jhtable").hide();
	}else{
		$("#QTHTYS").hide();
	}
	
	
	if(contenttype=="WORD"){
		$("#d_word").show();
		$("#d_pdf").hide();
		
	}else{
		$("#d_word").hide();
		$("#d_pdf").show();
	}
	if(isOpenContract == 1){
		$("#HTJE").val("");
		$("#HTJE").attr("readonly","readonly");
		$("#HTJE").attr("style","background:#efefef;width:85px");
		$("#HTJE").rules("remove");
		$("#HTJE").rules("add", {required:false});
		$("#labelHTJEDX").text("");
		$("#HTJEDX").val("");
		$("#HTRMBJE").val("");
    } 
	else{
		$("#HTJE").attr("readonly",""); 
		$("#HTJE").attr("style","background:#fff;;width:85px");
		$("#HTJE").rules("remove"); 
		$("#HTJE").rules("add", {required: true, messages:{required: "请填写合同金额"} });
	}
}

function changeCount(obj){  
	
	var count = obj.value;
	var pageUrl="getNumberRMB_action.action";
	$.post(pageUrl,{count:count}, function(result) {	
		//alert(result);
		$("#HTJEDX").val(result);
		$("#labelHTJEDX").text(result);
		
	});
	$('#HTRMBJE').val($('#HTJE').val()*$('#JSHL').val());
}

//点击币种单选按钮
function OnclickBZ(obj){
	if($("input[@type=radio][name=BZ][checked]").val()=="人民币"){
		$("#JSHL").attr("readonly","readonly"); 
		$("#JSHL").attr("style","background:#efefef;width:200px");
		$("#JSHL").val('1');
		var HTRMBJE = $("#JSHL").val()*$("#HTJE").val()
		$("#HTRMBJE").val(HTRMBJE);
	}else{
		$("#JSHL").attr("readonly","");
		$("#JSHL").attr("style","background:#fff;;width:200px");
	}
}

$().ready(function() {
	// 初始化币种及汇率关系
	if($("input[@type=radio][name=BZ][checked]").val()=="人民币"){
		$("#JSHL").attr("readonly","readonly"); 
		$("#JSHL").attr("style","background:#efefef;width:200px");
	}else{
		$("#JSHL").attr("readonly","");
		$("#JSHL").attr("style","background:#fff;;width:200px");
	}
});