$(function(){
		$(document).bind("contextmenu",function(e){
              return false;   
        });
	}); 
	function del(id){
		art.dialog.confirm('确认删除当前模型吗？', function(topWin){
			var pageurl = "sysDem_remove.action?id="+id;
			$.ajax({  
	            type:'POST',
	            url:pageurl,
	            success:function(msg){ 
	            	  if(msg=="success"){
	                  	art.dialog.tips("移除成功!");
	                  	reload();
	                  } 
	                  else if(responseText=="error"){
	                     art.dialog.tips("移除失败!");
	                  } 
	            } 
	        });
		}, function(){
		});
		
	}
	//编辑基本信息
	function openBaseInfo(title,id){
		var pageUrl = "sysDem_loadmap.action?id="+id;
		art.dialog.open(pageUrl,{
			id:'addressDialog', 
			title:title,
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:'90%',
		    height:'90%'
		 });
	}	
  	function reload(){
  		location.reload();
  	}