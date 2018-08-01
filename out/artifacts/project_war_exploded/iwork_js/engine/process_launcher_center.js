var process_launch_center_sort_index = 'process_launch_center_sort_index';
	
	function startProcess(actProcDefId, title){ 
		/*
		if(actProcDefId=='FYBXLC:1:71604'){
			var pageurl = "url:iwork_fi_newflow_dlg.action?actDefId="+actProcDefId;
			parent.openWin(title,400,800,pageurl,this.location,'createCenter'); 
			return;
		}*/ 
		var url = "processRuntimeStartInstance.action?actDefId=" + actProcDefId;
		addOperateLog(actProcDefId, title);
		openDialog(title,url); 
	} 
	
	//添加和编辑窗口
	function openDialog(title,url){
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	//	page.location = url;
	}
	
	//增加一条操作记录
	function addOperateLog(actProcDefId, title){
		var url = "sysOperateLogaddOperateLogAction.action";
	    $.post(url,{
	    	logType:"PROCESS_LAUNCH_CENTER_OPERATE_LOG",
	    	loginfo:actProcDefId,
	    	memo:title
	    },function(data){
	    	showaddLogResponse(data);
	    });
	}
	
	function showaddLogResponse(response) {
	    var rText = response;
	    if(rText != "success"){
	    	alert("保存操作记录失败");
	    }
	}
	//恢复默认布局
	function backToDefault(){
		var msg = "确认恢复默认布局吗?";
  		if (confirm(msg) == true) {
  			var saveStrType = jQuery("#saveStrType").val();
  			var cookieString = "";
  	        $.post("save_report_center_save_str.action",
  	        	{reportCenterSaveStr:cookieString,saveStrType:saveStrType},
  	        	function(result){
  	        		location.reload();
  	        });
  		} else {
   			return false;
  		}
 	}
 	
 	function showDefaultResponse(response){
 		var rText = response.responseText;
	    if(rText=='default'){
	    	alert("已经是默认布局");
	    }else if(rText=='error'){
	   	 	alert("保存操作记录失败");
	    }else{
	    	window.location.reload();
	    }
 	}
 	
 	//我发起的流程
 	function mylaunchProcess(){
 		window.location.href="processLaunchCenter!recentlaunch.action";
 	}
 	//最近发起的流程
	function recentLog(){
		window.location.href = "sysOperateLogIndexAction.action?logType=PROCESS_LAUNCH_CENTER_OPERATE_LOG";
	}
	function movePortlet(groupid,widgetid,orderlist){
		var orderlist = iNettuts.showOrderlist();
		alert(orderlist);
		if(orderlist!=""){ 
				$.post('processLaunchCenter_setLayout.action',{orderlist:orderlist},function(response){
 						if(response!='success'){  
 							alert('移动错误，请稍候重试');
 						}
				});
		}
	}