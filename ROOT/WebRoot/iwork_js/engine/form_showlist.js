/**
 * 
 */
$(function(){
		$(document).bind("contextmenu",function(e){
             // return false;   
        });
	}); 
function del(id){
	if(confirm("确认删除当前模型吗?")){
		var pageurl = "sysEngineIForm!remove.action?id="+id;
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

function openFormDesigner(title,formid,formType){
	var pageUrl = 'sysEngineIformDesginer_open.action?formid='+formid+'&formType='+formType;
	var dlgTitle=""
	if(formType==2){
		dlgTitle = '移动端表单样式及布局设计['+title+']';
	}else{
		dlgTitle = '表单样式及布局设计['+title+']';
	} 
	art.dialog.open(pageUrl,{
		id:'metadataWinDiv',
		title:dlgTitle,
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:'90%',
	    height:'90%'
	 });
}
//
function openFormMap(title,formid){
	var pageUrl = 'sysEngineIFormMap!showframe.action?formid='+formid;
	art.dialog.open(pageUrl,{
		id:'formMapWinDiv',
		title:'表单域设置['+title+']', 
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:'90%',
	    height:'90%'
	 });
	
}
function openBaseInfo(title,id){
	var pageUrl = 'sysEngineIForm_edit.action?id='+id;
	art.dialog.open(pageUrl,{
		id:'iformBaseWinDiv',
		title:'表单模型['+title+']', 
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:520,
	    height:350,  
	    close:function(){
			location.reload(true);
		}
	 });
}	
	function reload(){
		this.location.reload();
	}	