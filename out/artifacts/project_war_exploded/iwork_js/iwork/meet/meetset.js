$().ready(function() {
	setMeetType();
})

function setMeetType(){
	var meettype = $("input[name='MEETTYPE']:checked").val();
	if(meettype!='专业委员会'){
		$("#itemTr_1233").hide();
	}else{
		$("#itemTr_1233").show();
	}
	if(meettype!='股东大会'){
		$("#itemTr_1231").show();
	}else{
		$("#itemTr_1231").hide();
	}
	setMeetTitle();
}
function changeJc(){
	$("#jcinfo").text("提示:届次为系统自动记录，手工修改有可能导致数据异常");
	var customerno = $("#CUSTOMERNO").val();
	//会议属性;正式|临时
	var hysx = $("input[name='HYSX']:checked").val();
	var meettype = $("input[name='MEETTYPE']:checked").val();
	var jc = $("#JC").val();
	if(jc!=''&&meettype!=''&&hysx!=''){ 
		$.getJSON("zqb_meeting_getJC.action",{customerno:customerno,meettype:meettype,hysx:hysx,jc:jc},function(data){ 
			 $.each(data,function(idx,item){
				 $("#HC").val(item.num);
				jc="第"+item.cn+"届";
				jcNum = item.num;
			 });
		 }); 
	}else{ 
		$.getJSON("zqb_meeting_getJC.action",{customerno:customerno,meettype:meettype},function(data){ 
			 $.each(data,function(idx,item){
				$("#JC").val(item.num); 
			 });
		 }); 
	}
	setMeetTitle();
}

function setMeetJC(){
	var body='';
	$("#JC").val("");
	$("#jcinfo").text("");
	var meettype = $("input[name='MEETTYPE']:checked").val();
	
	if(meettype=='专业委员会'){
		body = $("#ZYWYH").val();
		$("#itemTr_1233").show();
		$("#itemTr_1231").show();
	}else if(meettype=='股东大会'){
		body = $("#GDDH").val();
		$("#itemTr_1233").hide();
		$("#itemTr_1231").hide();
	}
	else{
		body = meettype;
		$("#itemTr_1233").hide();
		$("#itemTr_1231").show();
	}
	var customerno = $("#CUSTOMERNO").val();
	//会议属性;正式|临时
	var hysx = $("input[name='HYSX']:checked").val();
	if(body!=""){
		$.getJSON("zqb_meeting_getJC.action",{customerno:customerno,meettype:meettype},function(data){
			 $.each(data,function(idx,item){
				$("#JC").val(item.num);
				jc="第"+item.cn+"届";
				jcNum = item.num;
			 });
		 }); 
	}
	setMeetTitle();
}
function setMeetTitle(){
	var jc = $("#JC").find("option:selected").text(); 
	var jcNum = $("#JC").find("option:selected").val(); 
	
	var body='';
	var meettype = $("input[name='MEETTYPE']:checked").val();
	if(meettype=='专业委员会'){
		body = $("#ZYWYH").val();
	}else{
		body = meettype;
	}
	var customerno = $("#CUSTOMERNO").val();
	//会议属性;正式|临时
	var hysx = $("input[name='HYSX']:checked").val();
	if(body!=""&&jc==''&&hysx==''){
		setMeetJC();
	}
	//会议次数
//	if(typeof(jc) !="undefined"&&typeof(body) !="undefined"&&typeof(hysx)!="undefined"){
//		$.getJSON("zqb_meeting_getCount.action",{customerno:customerno,jc:jcNum,meettype:meettype,grouptype:hysx},function(data){
//			$.each(data,function(idx,item){
//				if($("#HC").val()&&$("#HC").val()<=item.num){
//					alert("提示:该会次已经存在，系统自动生成会次");
//					//$("#HC").val(item.num);
//				}
				if(meettype=='股东大会'){
					if(hysx=='正式会议'){
						$("#itemTr_1233").hide();
						$("#itemTr_1231").hide();
						$("#HC").attr("disabled","disabled");
						var meetname = $("#YEAR").val()+"年度股东大会";
						hysx = "会议";
					}else{
						$("#HC").removeAttr("disabled");
						var meetname = $("#YEAR").val()+"年第"+$("#HC").find('option:selected').text()+"次临时股东大会";
					}
					
				}else{
					if(hysx=='正式会议'){
						hysx = "会议";
					} 
					$("#HC").removeAttr("disabled");
					var meetname = ((jc=="-空-"?"":jc)+(typeof(body)=='undefined'?"":body)+"第"+
					   ($("#HC").find('option:selected').text()=="-空-"?"":$("#HC").find('option:selected').text()) 
					   +"次"+(typeof(hysx)=='undefined'?"":hysx))=="第次"?"":((jc=="-空-"?"":jc)+(typeof(body)=='undefined'?"":body)+"第"+
							   ($("#HC").find('option:selected').text()=="-空-"?"":$("#HC").find('option:selected').text()) 
							   +"次"+(typeof(hysx)=='undefined'?"":hysx));
				}
				
				   
				document.getElementById("MEETNAME").value = meetname;
				$("#labelMEETNAME").text(meetname);
		//	});
	//	}); 
	//}
}
function changeHc(){
	var customerno = $("#CUSTOMERNO").val();
	var jcNum = $("#JC").find("option:selected").val(); 
	var meettype = $("input[name='MEETTYPE']:checked").val();
	var hysx = $("input[name='HYSX']:checked").val();
	var hc = $("#HC").val();
	$.getJSON("zqb_meeting_getCount.action?hc="+hc,{customerno:customerno,jc:jcNum,meettype:meettype,grouptype:hysx},function(data){
		$.each(data,function(idx,item){
			if($("#HC").val()&&item.num>0){
				alert("提示:该会次已经存在!");
			}
			setMeetTitle();
		});
	});
	
}
function checkPlanTime(){
	var plantime = $("#PLANTIME").val();
	if(new Date(plantime).getTime()<new Date().getTime()){
		if(!window.confirm("会议计划开始时间小于当前时间，请确认")){
			$("#PLANTIME").val("");
		}
	}
}


