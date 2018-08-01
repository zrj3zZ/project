
var api = art.dialog.open.api, W = api.opener;
$(function(){
		$(document).bind("contextmenu",function(e){
             // return false;   
        });
	}); 


function del(id){
	if(confirm("确认删除当前模型吗?")){
		var pageurl = "web_service_delete.action?id="+id;
		$.ajax({ 
            type:'POST',
            url:pageurl,
            success:function(msg){ 
            	  if(msg=="success"){
                  	alert("移除成功!");
                  	reload();
                  } 
                  else if(responseText=="error"){
                     alert("移除失败!");
                  } 
            }
        });
	}
	
}


/**
 * 调用示例
 * @param id
 */
function showRequestDemo(id){
	var pageUrl = 'web_service_demo_index.action?id='+id;
	 art.dialog.open(pageUrl,{
		 	id:'showRequestDemoDiv',
			title:'调用示例',  
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:700,
		    height:500
		 });
}

function showParamsDlg(id){
	var pageUrl = 'web_service_param_index.action?id='+id;
	art.dialog.open(pageUrl,{
	 	id:'winDiv',
	 	title:'修改WebServcie接口', 
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:'90%',
	    height:500
	 });
}

//
function showLog(id){
	var pageUrl = 'web_service_runtime_showlog.action?pid='+id;
	art.dialog.open(pageUrl,{
	 	id:'winDiv',
	 	title:'调用日志记录', 
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:750,
	    height:480
	 });
}


function reload(){
	this.location.reload();
}	