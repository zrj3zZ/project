
/**
 * 弹出提示消息
 * @param msg
 */
function showTip(msg){
	if($("#deviceType").val()=='iosMobile'){
		bridgeShowAlert(msg);
	}else{
		alert(msg);
	}
}
/**
 * 加载新的URL，并切换至下一个activity
 * @param title
 * @param url
 */
function loadframeURL(title,url){
	if($("#deviceType").val()=='iosMobile'){
		bridgeForwardWithObject(title,url);
	}else{
		window.FrameLink.loadLink(title,url); 
	}
	 
}     
/**
 * 返回至待办列表
 */
function backTodolist(){ 
	if($("#deviceType").val()=='iosMobile'){
		bridgeBackToList();
	}else{
		window.jumpTools.backTasklist(); 
	}
}
/**
 * 返回上一页activity
 */
function backPrevious(){
	if($("#deviceType").val()=='iosMobile'){
		bridgeBack();
	}else{ 
		alert("返回");
		window.jumpTools.backPrevious(); 
	}
}