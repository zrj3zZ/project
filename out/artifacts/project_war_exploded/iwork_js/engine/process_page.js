  $(document).ready(function(){
           $(document).bind("contextmenu",function(e){
           	//	return false;   
           });
  });
 		function addCCUsers(){ 
				var ccUsers = $("#ccUsers").val();
				var pageUrl = "processRuntimeCC.action?ccUsers="+encodeURI(ccUsers);
				art.dialog.open(pageUrl,{
					id:'ccDialog',
					title:"添加抄送",
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:510
				 });
		}
 		
		//设置意见附件列表
		function setOpinionFileList(html){
			$('#DIVATTACHMENT').append(html);
			 $.post('process_opin_addAttch.action',$("#ifromMain").serialize(),function(data){
			    	if(data=='1'){
			    		 window.parent.$.dialog.list["dg_addAuditOpinion"].close();
			    	}
		    });
		}
		
		//设置抄送列表
		function setCCList(str){
			 $("#ccUsers").val(str);
		}
//添加审核意见
		function addAuditOpinion(){
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val(); 
			    var pageUrl=encodeURI('process_opin_showFrame.action?actDefId='+actDefId+'&proDefId='+prcDefId+'&actStepId='+actStepDefId+'&actStepName=审核意见&instanceid='+instanceId+'&taskid='+taskId+'&excutionid='+excutionId);
				art.dialog.data('opinionContent', $("#opinion").val());
			    art.dialog.open(pageUrl,{
		    		id: "dg_addAuditOpinion" ,
		    		title:"办理意见", 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:600,
				    height:410,
				    close: function () {
			    		$.post('process_opin_addAttch.action',$("#ifromMain").serialize(),function(data){
					    });
						var bValue = art.dialog.data('bValue');// 读取B页面的数据
						if (bValue !== undefined) $("#opinion").val(bValue);
					}
				 });	 
		}
		//添加审核附件
		function addAttach(fieldName,divId){
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val(); 
			    var pageUrl='process_opin_attach.action?parentColId='+fieldName+'&parentDivId='+divId;
			    
			    art.dialog.open(pageUrl,{
		    		id: "dg_addAuditOpinion" ,
		    		title:"意见附件", 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:600,
				    height:410,
				    close: function () {
				    	 $.post('process_opin_addAttch.action',$("#ifromMain").serialize(),function(data){
					    });
						var bValue = art.dialog.data('bValue');// 读取B页面的数据
						if (bValue !== undefined) $("#opinion").val(bValue);
					}
				 });	 
		}
		
	       //调用父页面脚本，关闭办理窗口
        function _close(){
        	api.close();
       }