$(function(){
		$(document).bind("contextmenu",function(e){
              return false;   
        });
	}); 
	function del(id){
		if(confirm("确认删除当前模型吗？")){
			var pageurl = "sysDem_remove.action?id="+id;
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
	//编辑基本信息
	function openBaseInfo(title,id){
		var pageUrl = "sysDem_loadmap.action?id="+id;
		art.dialog.open(pageUrl,{
			id:'dmeBaseWinDiv',
			cover:true,
			title:title,  
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999', 
			rang:true,
			width:900,
			cache:false, 
			lock: true, 
			esc: true,
			height:550,  
			iconTitle:false, 
			extendDrag:true,
			autoSize:true,
		}); 
	}	
  	function reload(){
  		parent.location.reload();
  	}