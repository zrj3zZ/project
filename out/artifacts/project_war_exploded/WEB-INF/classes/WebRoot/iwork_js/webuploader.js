//点击附件上传按钮调用的方法，用于弹出上传附件窗口
	function webUploadifyDialog(dialogId,fieldName,divId,sizeLimit,multi,fileExt,fileDesc){
		if(dialogId==null||dialogId==""||fieldName==null||fieldName==""||divId==null||divId==""){
			alert('参数不正确');
			return false;
		}
		var pageUrl = 'showWebUploaderPage.action?test=1';
		if(fieldName!=null||fieldName!=""){
			pageUrl+=('&parentColId='+fieldName);
		}
		if(divId!=null||divId!=""){
			pageUrl+=('&parentDivId='+divId);
		}
		if(sizeLimit!=null||sizeLimit!=""){
			pageUrl+=('&sizeLimit='+sizeLimit);
		}
		if(multi!=null||multi!=""){
			pageUrl+=('&multi='+multi);
		}
		if(fileExt!=null||fileExt!=""){
			pageUrl+=('&fileExt='+fileExt);
		}
		if(fileDesc!=null||fileDesc!=""){
			pageUrl+=('&fileDesc='+fileDesc);
		}
		art.dialog.open(pageUrl,{
			id:dialogId,
			title: '上传附件',
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:650,
			height:510
		}); 
		return ;
	}