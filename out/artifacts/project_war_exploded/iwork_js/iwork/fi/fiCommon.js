
/**
 * 子表保存时回调页面，刷新主表单
 * @param subKey
 * @return
 */
function saveSubFormItemTrigger(subKey){
	var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
		return false;
	}else{
		singleSave();
	}
	window.location.reload();
}

function subformSaveAfterEvent(){
	window.location.reload();
	return true;
}

/**
 * 借款组件选择器
 * @param fieldName
 * @return
 */
function showJkDirectory(fieldName){
	var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
		return false;
	}else{
		singleSave();
	}
	
	var bxrZh = $("#SQRZH").val();
	//alert(bxrZh);
	var url = "iwork_fi_jk_list.action?jkdlist="+$("#"+fieldName).val()+"&defaultField="+fieldName+"&userid="+bxrZh;
	
	
	art.dialog.open(url,{   
		id:"subjectBookDialog",  
		title: '借款冲销',
		cover:true,
		loadingText:'正在加载中,请稍后...',
		bgcolor:'#999', 
		rang:true,
		cache:true,
		lock: true,
		iconTitle:false,
		extendDrag:true,
		autoSize:true, 
		resize:true,
		pading: 0,
		width: 450,
		height: 550
	}); 
		
}

/**
 * 子表删除后回调事件
 */
function deleteSubFormItemBeforeEvent(strArr){
	
	window.location.reload();
	return true;
}

/**
 * 切换报销人时，清空当前单据借款单内容，及解锁借款单信息
 * @return
 */
function unLockJkdAfterBxrChanged(){
	
	var instanceId = $("#instanceId").val();
	var jdkNo = $("#JKDBH").val();
	var actDefId = $("#actDefId").val();
	
	if(jdkNo==null || jdkNo==""){
		return ;
	}
	var pageUrl = "doUnlockAfterBxrChanged.action";
	//doUnlockAfterBxrChanged
	$.post(pageUrl,{instanceId:instanceId,jkdbh:jdkNo,actDefId:actDefId}, function(result) {	
		$("#JKDBH").val("");
		$("#CXJE").val("");
	});
	
}
