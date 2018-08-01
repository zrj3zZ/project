function moveUp(id){
	 var url = "function_moveup.action";
	    $.post(url,{id:id},function(data){
	    	if(data=="success"){
	    		window.location.reload();
	    	}
	    });
	
}
function moveDown(id){
	var url = "function_movedown.action";
    $.post(url,{id:id},function(data){
    	if(data=="success"){
    		window.location.reload();
    	} 
    });
	
}