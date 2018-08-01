$().ready(function() {
	$.ajaxSetup({ 
		async: false 
	});
// $("#subformTr_155").hide();
	setMeetType();
// setHYYAShowOrHide();
// setCHRYSubShowOrHide();
// setHeldMode();
	changeGuDongHysx();
	// setSubShowOrHide();
});
// 根据会议类型进行隐显
function setMeetType(){
	var meettype = $("input[name='MEETTYPE']:checked").val();
	if(meettype=='职工代表大会'){
		$("#itemTr_1233").hide();
		sethysxhide();
	}else if(meettype!='专业委员会'){
		sethysxshow();
		$("#itemTr_1233").hide();
	}else{
		sethysxshow();
		$("#itemTr_1233").show();
		$("#itemTr_1231").show();// 隐藏届次
		$("#huici").show();
		$("#HC").show();

	}
	 setMeetTitle();
}
// 选择股东大会是否为临时会议
function changeGuDongHysx(){
	var meettype = $("input[name='MEETTYPE']:checked").val();
	var hysx = $("input[name='HYSX']:checked").val();

	if(meettype=='股东大会'){
		$("#itemTr_1231").hide();// 隐藏届次
		$("#huici").hide();
		$("#JC").find("option:selected").val("");
		// $("#MEETNUMBER").find("option:selected").val("");
		if(hysx=='临时会议'){
			$("#huici").show();
			$("#HC").show();
		}
		sethysxshow();
	}else if(meettype=='职工代表大会'){
		$("#itemTr_1231").hide();// 隐藏届次
		$("#JC").find("option:selected").val("");
		$("#huici").show();
		$("#HC").show();
		sethysxhide();
	}else if(meettype!='专业委员会'){
		$("#huici").show();
		$("#HC").show();
		$("#itemTr_1231").show();// 隐藏届次
		sethysxshow();
	}
}
// 设置会议届次
function setMeetJC(){
	changeGuDongHysx();
	setMeetType();
	var customerno = $("#CUSTOMERNO").val();
	var hysx = $("input[name='HYSX']:checked").val();
	var year =$("#YEAR").find("option:selected").val();
	var meettype = $("input[name='MEETTYPE']:checked").val();
	var proType = $("#ZYWYH").find("option:selected").val();
//	$.ajaxSetup({ 
//		  async: false 
//		  }); 
	var pageUrl = "zqb_meeting_session.action";
	$.post(pageUrl,{customerno:customerno,meettype:meettype,grouptype:hysx,meetyear:year,meetpro:proType},function(data){
		var num = data;
		$("#JC").val(num);
		changeJc();
	});
}
// 设置会次次数
function setMeetHC(){
	var customerno = $("#CUSTOMERNO").val();
	var jc = $("#JC").val();
	var hysx = $("input[name='HYSX']:checked").val();
	var year = $("#YEAR").find("option:selected").val();
	var meettype = $("input[name='MEETTYPE']:checked").val();
	var proType = $("#ZYWYH").find("option:selected").val();
	var pageUrl = "zqb_meeting_getCount.action";
//	$.ajaxSetup({ 
//		  async: false 
//		  }); 
	$.post(pageUrl,{customerno:customerno,jc:jc,meettype:meettype,grouptype:hysx,meetyear:year,meetpro:proType},function(data){
		var num = data;
		$("#HC").val(num);
		changeMeetNum();
	});
}
function changeHc(){
	var customerno = $("#CUSTOMERNO").val();
	var jcNum = $("#JC").val();
	var meettype =  $("input[name='MEETTYPE']:checked").val();
	var hysx = $("input[name='HYSX']:checked").val();
	var year = $("#YEAR").find("option:selected").val();
//	$.ajaxSetup({ 
//		  async: false 
//		  }); 
	var hc = $("#HC").val();
	$.getJSON("zqb_meeting_gethc.action",{customerno:customerno,jc:jcNum,meettype:meettype,grouptype:hysx,hc:hc,meetyear:year},function(data){
		$.each(data,function(idx,item){
			if($("#HC").val()&&item.num>0){
				alert("提示:该会次已经存在!");
				$("#HC").val("");
			}
			setMeetTitle();
		});
	});
}
// 选择届次
function changeJc(){
	setMeetHC();// 设置会次
	setMeetTitle();// 设置会议标题
}
// 选则会议次数
function changeMeetNum(){
	validateHC();
	setMeetTitle();
}
// 判断会次是否重复
function validateHC(){
	var customerno = $("#CUSTOMERNO").val();
	var jc = $("#JC").val();
	var hysx = $("input[name='HYSX']:checked").val();
	var year = $("#YEAR").find("option:selected").val();
	var meettype = $("input[name='MEETTYPE']:checked").val();
	var proType = $("#ZYWYH").find("option:selected").val();
	var meetNum =$("#HC").val();
	$.ajaxSetup({ 
		  async: false 
		  }); 
	var pageUrl = "zqb_validate_number.action";
	$.post(pageUrl,
{customerno:customerno,jc:jc,meettype:meettype,grouptype:hysx,meetyear:year,meetpro:proType,hc:hc},function(data){
		if(data=='success'){
			if(meettype=='股东大会'){
				alert("存在完全相同会议，请选择其他年度！");
				$("#YEAR").val("");
			}else{
				alert("存在完全相同会议，请选择其他会议次数！");
				$("#HC").val("");
			}
		}
	});
}
// 生成会议标题
function setMeetTitle(){
	var meetname = '';
	// 年度
	var year = $("#YEAR").find("option:selected").val();
	if(year==undefined){
		year=$('input[name="YEAR"]').val();
	}
	if(year!=''){
		year = year+'年';
	}
//	// 届次
//	var jc = $("#JC").find("option:selected").val(); 
//	//alert("第一处"+jc);
//
//	if(jc!=''){
//		jc = $("#JC").find("option:selected").text(); 	
//	}
	var  jc = $("#JC").find("option:selected").text(); 
	if(jc=="-空-"){
		jc =""; 
	}
	// 会议属性
	var hysx = $("input[name='HYSX']:checked").val();
	if(hysx=='正式（年度）会议'){
		hysx = '会议';
	}else if(hysx=='临时会议'){
		hysx = '临时会议';
	}else{
		hysx='';
	}
	// 会议次数
	var meetNum = $("#HC").find("option:selected").val();
	if(meetNum!=''){
		meetNum =  $("#HC").find("option:selected").text(); 	 
	}
	var meetnum=("第"+meetNum +"次")=="第次"?"":("第"+meetNum +"次");

	// 会议类型
	var meettype =  $("input[name='MEETTYPE']:checked").val();
	if(typeof(meettype)=='undefined'){
		meettype = '';
	}else{
		if(meettype!=''){
			if(meettype=='专业委员会'){
				var proType = $("#ZYWYH").find("option:selected").val();
				if(typeof(proType)!='undefined' && proType!=''){
					meettype = "董事会"+proType;
				}else{
					meettype = '';
				}
			}
		}else{
			meettype='';
		}
	}
	if(meettype=='职工代表大会'){
		meetname = year+meetnum+meettype;
	}else if(meettype=='股东大会'){
		if(hysx=='会议'){
			meetname = year+'年度'+meettype;
		}else{
			meetname = year+meetnum+'临时'+meettype;
		}
	}else{
		meetname = jc+meettype+meetnum+hysx;
	}
	   
	document.getElementById("MEETNAME").value = meetname;
	$("#labelMEETNAME").text(meetname);
}
function sethysx(){
	changeGuDongHysx();
	changeJc();
}
//选择年度
function changeYear(){
	setMeetJC();
	setMeetHC();
	setMeetTitle();
}
function sethysxhide(){
	$('input:radio[name=HYSX]')[0].checked = true;
	$('#data_HYSX font:first').hide();
	$("#hysx").html("会次");
	$("#HYSX0").hide();// 隐藏会议属性
	$("#lbl_HYSX0").hide();// 隐藏会议属性
	$("#HYSX1").hide();// 隐藏会议属性
	$("#lbl_HYSX1").hide();// 隐藏会议属性
}
function sethysxshow(){
	$("#hysx").html("会议属性");
	$("#HYSX0").show();// 隐藏会议属性
	$("#lbl_HYSX0").show();// 隐藏会议属性
	$("#HYSX1").show();// 隐藏会议属性
	$("#lbl_HYSX1").show();// 隐藏会议属性
}
