$(function(){
	$('#portletid').combotree({   
	 onBeforeSelect:function(node){
        if(node.attributes.type=='group'){
            $.messager.confirm('系统提示','所属栏目分类名称不能选择，请重新选择!',function(result){  
			    if(result){
	              $('#portletid').combotree('showPanel');
	              return false;
	            }
		 	})
		 	return false;
        }
        }	
	 });
});

//发布		    
function Add(){
     if($('#portletid').combotree('getValue')==""){
        $.messager.alert('系统提示','所属栏目不允许为空!','info'); 
		  return false;     
     }
	 if(document.forms[0].infotitle.value==""){
	  $.messager.alert('系统提示','标题不允许为空!','info'); 
		  return false;
	 }		 
	 if(document.forms[0].brieftitle.value==""){
	  $.messager.alert('系统提示','显示标题不允许为空!','info'); 
		  return false;
	 }
	   if(document.forms[0].source.value==""){
	  $.messager.alert('系统提示','来源不允许为空!','info'); 
		  return false;
	 }
	  if(document.forms[0].prepicture.value==""){
	  $.messager.alert('系统提示','导读图片不允许为空!','info'); 
		  return false;
	 }
	  if(document.forms[0].precontent.value==""){
	  $.messager.alert('系统提示','导读内容不允许为空!','info'); 
		  return false;
	 }
	 if(document.forms[0].markred.checked){
	   document.forms[0].markred.value=1;
	 }
	 if(document.forms[0].markbold.checked){
	   document.forms[0].markbold.value=1;
	 }
	 if(document.forms[0].marktop.checked){
	   document.forms[0].marktop.value=1;
	 }
	 document.forms[0].status.value=0;
	 var url='cmsInfoAdd.action';	
     document.forms[0].action=url;
     document.forms[0].target="hidden_frame";
     document.forms[0].submit();
     parent.opener.window.location.reload();
	 return false; 
}				
				
//暂存				
function Save(){
     if($('#portletid').combotree('getValue')==""){
        $.messager.alert('系统提示','所属栏目不允许为空!','info'); 
		  return false;     
     }
      if(document.forms[0].infotitle.value==""){
	  $.messager.alert('系统提示','标题不允许为空!','info'); 
		  return false;
	 }		 
	 if(document.forms[0].brieftitle.value==""){
	  $.messager.alert('系统提示','显示标题不允许为空!','info'); 
		  return false;
	 }
	   if(document.forms[0].source.value==""){
	  $.messager.alert('系统提示','来源不允许为空!','info'); 
		  return false;
	 }
	  if(document.forms[0].prepicture.value==""){
	  $.messager.alert('系统提示','导读图片不允许为空!','info'); 
		  return false;
	 }
	  if(document.forms[0].precontent.value==""){
	  $.messager.alert('系统提示','导读内容不允许为空!','info'); 
		  return false;
	 }
	 if(document.forms[0].markred.checked){
	   document.forms[0].markred.value=1;
	 }
	 if(document.forms[0].markbold.checked){
	   document.forms[0].markbold.value=1;
	 }
	 if(document.forms[0].marktop.checked){
	   document.forms[0].marktop.value=1;
	 }
	 document.forms[0].status.value=1;
	 var url='cmsInfoSave.action';	
     document.forms[0].action=url;
     document.forms[0].target="hidden_frame";
     document.forms[0].submit();
     parent.opener.window.location.reload();
	 return false; 
}
					
//保存退出					
function SaveUp(){
     if($('#portletid').combotree('getValue')==""){
        $.messager.alert('系统提示','所属栏目不允许为空!','info'); 
		  return false;     
     }
	  if(document.forms[0].infotitle.value==""){
	  $.messager.alert('系统提示','标题不允许为空!','info'); 
		  return false;
	 }		 
	 if(document.forms[0].brieftitle.value==""){
	  $.messager.alert('系统提示','显示标题不允许为空!','info'); 
		  return false;
	 }
	  if(document.forms[0].source.value==""){
	  $.messager.alert('系统提示','来源不允许为空!','info'); 
		  return false;
	 }
	  if(document.forms[0].prepicture.value==""){
	  $.messager.alert('系统提示','导读图片不允许为空!','info'); 
		  return false;
	 }
	  if(document.forms[0].precontent.value==""){
	  $.messager.alert('系统提示','导读内容不允许为空!','info'); 
		  return false;
	 }
	  if(document.forms[0].markred.checked){
	   document.forms[0].markred.value=1;
	 }
	 if(document.forms[0].markbold.checked){
	   document.forms[0].markbold.value=1;
	 }
	 if(document.forms[0].marktop.checked){
	   document.forms[0].marktop.value=1;
	 }
	 document.forms[0].status.value=1;
	 var url='cmsInfoAdd.action';	
     document.forms[0].action=url;
     document.forms[0].target="hidden_frame";
     document.forms[0].submit();
     parent.opener.window.location.reload();
	 return false; 
}
					
//作废			
function Remove(){
  $.messager.confirm('系统提示','确定作废？',function(r){
    if (r){       	   
	    var id=document.forms[0].infoid.value; 
	    if(id==null||id==""){
	    window.close();	
	    }else{
	    window.close();	
	    var url='cmsInfoRemove.action';	
	    document.forms[0].action=url;
	    document.forms[0].submit();    
	    }
	    parent.opener.window.location.reload();			
	  }	
  });	
}	