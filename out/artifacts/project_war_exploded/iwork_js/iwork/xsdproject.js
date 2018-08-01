$().ready(function() {
	setTimeout("setScore()",2000);
	var xmjd=$("#XMJD").val();
	/*if(xmjd!=""&&xmjd=="持续督导"){
		$("#XMJD").attr("disabled",true); 
	}*/
	var customerno = $("#CUSTOMERNO").val();
	$.getJSON("xsd_zqb_project_getCXDD.action",{customerno : customerno},function(data) {
		$.each(data.rows,function(idx, item) {
			var khfzr=item.KHFZR;
			var fhspr=item.FHSPR;
			$("#KHFZR").html(khfzr);
			$("#FHSPR").html(fhspr);
		});
	});
})



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