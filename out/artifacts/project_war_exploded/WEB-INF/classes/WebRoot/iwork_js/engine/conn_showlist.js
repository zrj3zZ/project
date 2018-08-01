var api = art.dialog.open.api, W = api.opener;
$(function(){
		$(document).bind("contextmenu",function(e){
             // return false;   
        });
	}); 


function manualOpera(id){
	var pageurl = "conn_runtime_doexecute.action";
	 $.post(pageurl,{pid:id},function(msg){ 
	    	if(msg=='success'){
	    		 alert('执行成功');
	    	}else{
	    		alert(msg);
	    	} 
		 }); 
}

function del(id){
	if(confirm("确认删除当前模型吗?")){
		var pageurl = "conn_design_delete.action?id="+id;
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

function showParamsDlg(id){
	var pageUrl = 'conn_design_param_index.action?id='+id;
	art.dialog.open(pageUrl,{
		id:'connWinDiv',
		title:'修改集成接口',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:'90%',
	    height:'90%'
	 });
}
//
function edit(id){ 
	var pageUrl = 'conn_runctime_showlog.action?pid='+id;
	art.dialog.open(pageUrl,{
		id:'connBaseWinDiv',
		title:'调用日志记录',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:'90%',
	    height:'90%'
	 });
}
	function reload(){
		this.location.reload();
	}	