/**
 * 
 */
$(function(){
		$(document).bind("contextmenu",function(e){
              //return false;   
        });
	});
	function refresh() {
		window.location.href='../sysProcessDefinition!displayProcess.action?processId=' + document.uploadNewForm.sysProcDefId.value;
	}
	function openProcessDef(title,prcDefId,processkey,actdefId){
		var pageUrl = 'sysFlowDef_index.action?prcDefId='+prcDefId+'&actdefId='+actdefId;
		 art.dialog.open(pageUrl,{
			 id:'openProcessDefWinDiv',
				title:'流程模型设计['+title+']',  
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:'95%',
			    height:'95%'
			 });
	}	
	function openStepDef(title,prcKey,prcDefId,actdefId){
		var pageUrl = 'sysFlowDef_stepFrame.action?prcDefId='+prcDefId+'&actDefId='+actdefId;
		 art.dialog.open(pageUrl,{
			 id:'openProcessDefWinDiv',
			 title:'节点模型设计['+title+']',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:'95%',
			    height:'95%'
			 });
	}	
	function copyProcess(prcDefId,prcKey,actdefId){ 
		if(confirm("确认复制当前流程模型吗？")){
				var pageurl = "../processDeploy_process_copy.action?actProcId="+actdefId+"&proDefId="+prcDefId+"&processKey="+prcKey;
				$.ajax({ 
		            type:'POST',
		            url:pageurl,
		            success:function(msg){ 
		            	  if(msg=="nopuriew"){
		                  	art.dialog.tips("权限不足！");
		                  } 
		                  else if(msg=="error"){
		                     art.dialog.tips("复制失败！");
		                  }else{
		                  	art.dialog.tips(msg);
		                  	reload();
		                  }
		            }
		        });
			}
	}
	//加载流程图
	function showProcessImage(title,prcDefId,prcKey,actdefId){
		var pageUrl = 'processDeploy_openProcessImage.action?proDefId='+prcDefId+'&actProcId='+actdefId;
		 art.dialog.open(pageUrl,{
			 id:'processImageWinDiv',
			 title:'流程图['+title+']',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:'99%',
			    height:'99%'
			 });
	}	
	function showProcessDesigner(title,prcDefId,prcKey,actdefId){
		var pageUrl = 'processDeploy_show_designer.action?processKey='+prcKey+'&id='+prcDefId+'&actProcId='+actdefId;
		 art.dialog.open(pageUrl,{
			 	id:'openProcessDesignerWinDiv',
				title:'流程设计器['+title+']',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:'99%',
			    height:'99%'
			 });
	}

	function changeStatus(id,status){
				var msg = "";
				if(status==1){
					msg = "你确认将当前流程模型修改为发布状态吗？";
				}else{
					msg = "你确认取消当前流程模型的发布状态吗？";
				}
				if(confirm(msg)){
				    var url = "../processDeploy_change_status.action?id="+id+"&status="+status;
					$('#editForm').attr('action',url);
					 var options = {
						error:errorFunc,
						success:successFunc 
					   };
					$('#editForm').ajaxSubmit(options);
				}
       		}
			 function errorFunc(){
				 art.dialog.tips("状态修改失败!");
     		 }
	      	function successFunc(responseText, statusText, xhr, $form){
	           if(responseText=="error"&&responseText!=""){ 
	        	   art.dialog.tips("状态修改失败!");
	           } 
	           else if(responseText=="success"){
	        	  // $.dialog.tips("状态修改成功!",2);
	        	   art.dialog.tips("状态修改成功!");
	              setTimeout("reload()",500);
	              
	           }else if(responseText=="unperview"){
	        	   //$.dialog.tips("您的权限不足，不能进行流程发布!",2);
	        	   art.dialog.tips("您的权限不足，不能进行流程发布!");
	           }else if(responseText=="unComplete"){
	        	  // $.dialog.tips("当前流程模型未设置完毕，不能进行流程发布!",2);
	        	   art.dialog.tips("当前流程模型未设置完毕，不能进行流程发布!");
	           }  
	}
	      	//执行删除操作
	 function del(proid,iscascade){
		 if(confirm("确认删除当前流程模型吗？")){
				this. doDel(proid,iscascade);
		}
	 }
	 
	 function doDel(proid,iscascade){
	 	var pageurl = "processDeploy_remove.action?id="+proid+"&iscascade="+iscascade;
				$.ajax({ 
		            type:'POST',
		            url:pageurl,
		            success:function(msg){ 
		            	  if(msg=="success"){ 
		                  	art.dialog.tips("移除成功！");
		                  	reload();
		                  }else if(msg=="isrun"){
		                     art.dialog.tips("‘运行中’的流程模型无法移除，请先停用后移除流程！");
		                  }else if(msg=="nopurview"){
		                     art.dialog.tips("权限不足");
		                  }else if(msg=="isCascade"){
		                  	if(confirm("当前流程在系统中有未流转结束的实例，是否执行强制级联删除？")){
		                  	art.dialog.tips(proid);
		                  		doDel(proid,1);
		                  	}
		                  }
		                  else if(msg=="error"){
		                     art.dialog.tips("移除失败！");
		                  } 
		            }
		        });
	 }
	 
	      	function reload(){
	      		this.location.reload();
	      	}