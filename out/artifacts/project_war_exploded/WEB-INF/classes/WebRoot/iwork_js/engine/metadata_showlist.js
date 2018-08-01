$(function(){
		$(document).bind("contextmenu",function(e){
              return false;   
        });
	}); 
	function del(id){
		if(confirm("确认删除当前模型吗？")){
			var pageurl = "sysEngineMetadata_remove.action?id="+id;
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
	function openMeataDataMap(title,metadataId){
		var pageUrl = 'sysEngineMetadataMap!index.action?metadataid='+metadataId;
		art.dialog.open(pageUrl,{
			id:'metadataWinDiv',
			title:'存储模型['+title+']',  
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:'90%',
		    height:'90%'
		 });
	}
	function openBaseInfo(title,id){
		var pageUrl = 'sysEngineMetadataMap_frame.action?id='+id;
		art.dialog.open(pageUrl,{
			id:'metadataWinDiv',
			title:'存储模型['+title+']',  
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