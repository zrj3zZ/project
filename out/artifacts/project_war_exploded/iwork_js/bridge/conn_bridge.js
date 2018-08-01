
var mainFormValidator;
$().ready(function() {
	mainFormValidator =  $("#editForm").validate({
		debug:false,
		errorPlacement: function (error, element) { //指定错误信息位置
			if (element.is(':radio') || element.is(':checkbox')) {
				var eid = element.attr('name');
				 error.appendTo(element.parent());
			} else {
			error.insertAfter(element);
		}
	} 
});
 mainFormValidator.resetForm();
});
function showParamsDlg(id){
	var pageUrl = 'conn_bridge_param_index.action?id='+id;
	art.dialog.open(pageUrl,{
		id:'connWinDiv',
		title:'修改连接',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:'90%',
	    height:'90%',
	    close:function(){
	 		location.reload();
	    }
	 });
}	
function doSubmit(){
	var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
		 art.dialog.tips("表单验证失败，请确认表单是否填写完整！");
			return;
		}
	var options = {
		error:errorFunc,
		success:successFunc 
	};
	$('#editForm').ajaxSubmit(options);
}
errorFunc=function(){
    art.dialog.tips("保存失败！");
 }
successFunc=function(responseText, statusText, xhr, $form){
        if(responseText=="success"){
     	   art.dialog.tips("保存成功",2);
     	   cancel();
        }
        else if(responseText=="error"){
           art.dialog.tips("保存失败！");
        } 
 }

//关闭窗口
function cancel(){
	api.close();
}


function del(id){
	if(confirm("确认删除当前连接吗?")){
		var pageurl = "conn_bridge_delete.action?id="+id;
		$.ajax({ 
            type:'POST',
            url:pageurl,
            success:function(msg){ 
            	  if(msg=="success"){
                  	alert("移除成功!");
                  	location.reload();
                  } 
                  else if(responseText=="error"){
                     alert("移除失败!");
                  } 
            }
        });
	}
	
}


