$(function (){
	$.ajaxSetup({
        async: false
    });
	var projectno=$("#PROJECTNO").val();
	$.getJSON("getZkAndNhDate.action",{projectno:projectno},function(data){
		if(data.isDwPj==1){
			$.each(data.rows,function(idx, item) {
				$("#zkdate").html(item.ZKDATE);
				$("#nhdate").html(item.NHDATE);
			});
		}
		if(data.IsZkOrNh){
			$("#data_A01").hide();
			$("#title_A01").hide();
		}else{
			$("#data_A01").show();
			$("#title_A01").show();
		}
	}); 
});
function setScore(){
	var score = 0;
	var a01 = $("input[name='A01']:checked").val();
	var a02 = $("input[name='A02']:checked").val();
	var a03 = $("input[name='A03']:checked").val();
	var a04 = $("input[name='A04']:checked").val();
	var a05 = $("input[name='A05']:checked").val();
	var a06 = $("input[name='A06']:checked").val();
	var a07 = $("input[name='A07']:checked").val();
	var a08 = $("input[name='A08']:checked").val();
	if(a01=="否"){score+=20;}
	if(a02=="是"){score+=20;}
	if(a03=="否"){score+=10;}
	if(a04=="否"){score+=10;}
	if(a05=="否"){score+=10;}
	if(a06=="是"){score+=5;}
	if(a07=="否"){score+=10;}
	if(a08=="是"){score+=5;}
	//获取最新一年净利润评分
	var yj = $("#YJZXYNJLR").val();
	if(yj>0){
		score+=5;
	} 
	//获取主要财务数据评分 
	var obj = $("#subformSUBFORM_ZYCWSJ_1").getRowData(); 
	//alert(obj.JLR);
	var jlr=0;
    jQuery(obj).each(function(){
    	if(jlr==0){
    		 jlr = this.JLR;
    	}
    }); 
    
    if(jlr>0){
    	score+=5;
    }
	//设置分数值
    $("#labelFXPGFS").html(score);
    $("#FXPGFS").val(score);
    updateScore(score);
}
function updateScore(score){
	var pageUrl = "zqb_project_updateScore.action";
	var instanceId = document.getElementById('instanceId').value;
	 $.post(pageUrl,{instanceid:instanceId,score:score},function(msg){ 
	    	
		 }); 
	
}
function check(score){
	  if(parseFloat($("#SSJE").val())>parseFloat($("#HTJE").val())){
		  alert("实收金额应不大于合同金额，请重新输入！");
		  $("#SSJE").val("");
		  
	  }
	
}
function checkRQ(){
	if($("#ENDDATE").val()<$("#STARTDATE").val()){
		  alert("项目结束时间不能早于开始时间，请重新输入！");
		  $("#ENDDATE").val("");
		  return;
		  
	  }
}
function pjSubCheck(){
	$.ajaxSetup({
        async: false
    });
	var flag=false;
	var instanceId = $("#instanceId").val();
	var pageUrl = "zqbPjSubCheck.action";
	$.post(pageUrl,{instanceid:instanceId},function(msg){
		if(msg==""){
			flag=true;
		}else{
			flag=false;
		}
	});
	return flag;
}

function pjLcSubCheck(){
	$.ajaxSetup({
        async: false
    });
	var flag=false;
	var projectno = $("#PROJECTNO").val();
	var pageUrl = "zqbPjLcSubCheck.action";
	$.post(pageUrl,{projectno:projectno},function(msg){
		if(msg==""){
			flag=true;
		}else{
			flag=false;
		}
	});
	return flag;
}

function isSetCommmit(){
	if($("#instanceId").val()!=0&&pjSubCheck()){
		if(confirm("分配部门和承揽人信息需提交部门负责人审批确认，是否现在提交？")){
			$("#SFBMFZRSP").val("1");
		}else{
			$("#SFBMFZRSP").val("0");
		}
	}
	return true;
}
function setCommit() {
	if($("#instanceId").val()!=0&&pjSubCheck()){
		if(confirm("分配部门和承揽人信息需提交部门负责人审批确认，是否现在提交？")){
			var url = "";
			var instanceid = $("#instanceId").val();
			$.ajax({
				url : 'zqbPjSubgetContent.action',
				async : false,
				type : "POST",
				data : {
					instanceid : instanceid
				},
				dataType : "json",
				success : function(data) {
					var executionId = data.executionId;
					var taskid = data.taskid;
					var actDefId = data.actDefId;
					var instanceId = data.instanceId;
					url = 'url:loadProcessFormPage.action?actDefId=' + actDefId
							+ '&instanceId=' + instanceId + '&excutionId='
							+ executionId + '&taskId=' + taskid;
				}
			});
			var win_width = window.screen.width - 200;
			$.dialog({
				title : '部门负责人审批',
				loadingText : '正在加载中,请稍后...',
				bgcolor : '#999',
				rang : true,
				width : win_width,
				cache : false,
				lock : true,
				height : 750,
				iconTitle : false,
				extendDrag : true,
				autoSize : false,
				content : url,
				close : function() {
					window.location.reload();
				}
			});
		}
	}
}

function setLcCommit() {
	if(pjLcSubCheck()){
		if(confirm("分配部门和承揽人信息需提交部门负责人审批确认，是否现在提交？")){
			var url = "";
			var projectno = $("#PROJECTNO").val();
			$.ajax({
				url : 'zqbPjLcSubgetContent.action',
				async : false,
				type : "POST",
				data : {
					projectno : projectno
				},
				dataType : "json",
				success : function(data) {
					var executionId = data.executionId;
					var taskid = data.taskid;
					var actDefId = data.actDefId;
					var instanceId = data.instanceId;
					url = 'url:loadProcessFormPage.action?actDefId=' + actDefId
							+ '&instanceId=' + instanceId + '&excutionId='
							+ executionId + '&taskId=' + taskid;
				}
			});
			var win_width = window.screen.width - 200;
			$.dialog({
				title : '部门负责人审批',
				loadingText : '正在加载中,请稍后...',
				bgcolor : '#999',
				rang : true,
				width : win_width,
				cache : false,
				lock : true,
				height : 750,
				iconTitle : false,
				extendDrag : true,
				autoSize : false,
				content : url,
				close : function() {
					window.location.reload();
				}
			});
		}
	}
}

function setReadOnly() {
	if($("#instanceId").val()!=0&&$("#LCBS").val()!=""){
		$(".ke-zeroborder a").hide();
		var values, index;
		values = $("#iformMain").serializeArray();
		for (index = 0; index < values.length; ++index) {
			if(values[index]!=null){
				$(".ke-zeroborder select").attr("disabled","disabled");
				$("#"+values[index].name).attr("readonly", "readonly");
				document.getElementById(values[index].name).onfocus=null;
				document.getElementById(values[index].name).onclick=null;
			}
		}
	}
}

function syncData(){
	$.ajaxSetup({
        async: false
    });
	var flag=false;
	var projectno = $("#PROJECTNO").val();
	var pageUrl = "zqbProjectSyncData.action";
	$.post(pageUrl,{projectno:projectno});
}

function checkxmmc(data){
	alert(data);
	
	
}








