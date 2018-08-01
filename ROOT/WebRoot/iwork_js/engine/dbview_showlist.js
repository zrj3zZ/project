$(function(){
		$(document).bind("contextmenu",function(e){
             // return false;   
        });
	}); 
	function del(id){
		if(confirm("确认删除当前视图吗？")){
			var pageurl = "sysEngineDbView_remove.action?id="+id;
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

	function openBaseInfo(title,id){
		var pageUrl = 'sysEngineDbView_edit.action?id='+id;
		 art.dialog.open(pageUrl,{
			 	id:'metadataBaseWinDiv',
		    	title:'视图模型['+title+']',  
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:650,
			    height:500
			 });
	}	
  	function reload(){
  		this.location.reload();
  	}