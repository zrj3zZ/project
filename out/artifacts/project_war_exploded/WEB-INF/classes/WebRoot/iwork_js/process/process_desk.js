
function openNoticePage(title,actDefId,instanceId,excutionId,taskId,dataid){
	//var url = 'loadNoticeFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId+'&dataid='+dataid;
	var url = "process_desk_notice.action";
	this.location = url;
//	var target = "_self"; 
//	var win_width = window.screen.width-50;
//	var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	//page.location=url;
//	page.document.title = title;  
//	$("#notice_"+taskId).hide(); 
}
function openSignsPage(title,actDefId,instanceId,excutionId,taskId){
	var url = 'loadSignsPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId;
	var target = "iform_"+taskId;
	var win_width = window.screen.width-50;
	var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	//page.location=url;ssss
	page.document.title = title; 
}
function openTaskPage(title,actDefId,instanceId,excutionId,taskId){
	var url = 'loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId;
	var target = "iform_"+taskId;
	var win_width = window.screen.width-50;
	var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	//page.location=url;
	page.document.title = title; 
}
function claim(taskId,userId){
	document.frmMain.taskId.value = taskId;
	document.frmMain.userId.value = userId;
	  $.post('processManagement_claimTask.action',$("#frmMain").serialize(),function(data)
            {
		    	if(data=='success'){
		    		art.dialog.tips("任务领取成功",5);
		    		 reloadPage(); 
		    	}else{
		    		art.dialog.tips("任务领取失败(错误编号:ERROR-00001)",5)
		    	}
		  });  
}
//关闭任务
function closeTask(taskId){  
	art.dialog.confirm('你确定要关闭当前任务吗？', function () {
	  $.post('processManagement_closeTask.action',{taskId:taskId},function(msg)
            {
		    	if(msg=='success'){
		    		art.dialog.tips("当前任务已关闭",5);
		    		 reloadPage(); 
		    	}else{
		    		art.dialog.tips("当前任务关闭失败(错误编号:ERROR-00001)",5)
		    	} 
		  }); 
	});
}
//任务休眠
function sleepTask(taskId){
	var pageUrl = "processManagement_showSleepPage.action?taskId="+taskId;
	art.dialog.open(pageUrl,{
		id:'addressDialog', 
		title:'流程休眠', 
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:500,
	    height:300
	 });
} 
//激活休眠任务
function activeTask(taskId){
	  $.post('processManagement_activeTask.action',{taskId:taskId},function(data)
            { 
		    	if(data=='success'){
		    		art.dialog.tips("激活成功",5); 
		    		 reloadPage(); 
		    	}else{
		    		art.dialog.tips("激活任务失败(错误编号:ERROR-00001)",5)
		    	}
		    	showSysTips();
		  }); 
}
//激活休眠任务
function setTaskNotice(tasid,dataid){
	$.post('process_desk_notice_read.action',{dataid:dataid},function(data)
			{ 
		if(data=='success'){
			art.dialog.tips("抄送通知已转到通知传阅列表",5); 
		//	$("#notice_"+tasid).slideToggle("slow"); 
				$("#notice_"+tasid).animate({height: 'toggle', opacity: 'toggle'});
		}else{ 
			art.dialog.tips("权限不足",5)
		}
	}); 
}

//删除任务
function deleteTask(taskId){
	art.dialog.confirm('你确定要删除当前任务吗？', function () {
		$.post('processManagement_deleteTask.action',{taskId:taskId},function(data){
			    	if(data=='success'){
			    		art.dialog.tips("已删除",1);
			    		  reloadPage(); 
			    	}else{
			    		art.dialog.tips("当前流程已处于流转中，无法进行删除",1);
			    	}
			    	showSysTips();
			  }); 
	}, function () {
	    
	});
}
function showTaskCount(){
	 $.post('processManagement_showTaskNoticeJson.action',{},function(data){
		 var dataObj=eval("("+data+")");
		 var sumNum = 0;
		 $.each(dataObj,function(idx,item){
			 if(idx=="taskNum"){
				 if(item!=0){
					 $("#taskNum").html("("+item+")"); 
					 sumNum+=item; 
				 }else{
					 $("#taskNum").html("");
				 }
			 }
			 if(idx=="noticeNum"){
				 if(item!=0){
					 $("#NoticeNum").html("("+item+")");
					 sumNum+=item;
				 }else{
					 $("#NoticeNum").html("");
				 }
			 }
		 }) 
		$("#workflow", window.parent.document).html("("+sumNum+")"); 
	 }); 
}
function reloadWorkList(){
	this.location.reload();
	parent.showTips('workflow');
}

function showInfo(taskId){
	if($("#more"+taskId).hasClass("more")){
		$("#more"+taskId).removeClass("more"); 
		$("#more"+taskId).addClass("js");
				$.post('process_desk_getsummary.action',{taskId:taskId},function(msg)
				 { 
					$("#content"+taskId).find(".summary_main").html(msg);
				 }); 
	}else{
		$("#more"+taskId).removeClass("js"); 
		$("#more"+taskId).addClass("more");
	}
	$("#content"+taskId).animate({height: 'toggle', opacity: 'toggle'});
}
function claim(taskId,userId){
	document.frmMain.taskId.value = taskId;
	document.frmMain.userId.value = userId;
	$.post('processManagement_claimTask.action',$("#frmMain").serialize(),function(data){
	    	if(data=='success'){
	    		 art.dialog.tips("任务领取成功",5);
	    		 reloadPage();
	    	}else{
	    		art.dialog.tips("任务领取失败(错误编号:ERROR-00001)",5)
	    	}
	  });  
}
function reloadPage(){
	    window.location.reload();
	}