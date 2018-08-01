var api = frameElement.api, W = api.opener;
var mainFormValidator;
$(document).ready(function(){
	mainFormValidator =  $("#editForm").validate({
		//出错时增加的标签
        errorPlacement: function (error, element) {
                error.appendTo(element.parent());    //将错误信息添加当前元素的父结点后面               
        }
   }); 
    mainFormValidator.resetForm();
 });
		
//执行保存
//表单提交
function save(){ 
	var valid = mainFormValidator.form(); //执行校验操作 
	if(!valid){
			return false;
	}
    var options = {
		error:errorFunc,
		success:successFunc 
	   }; 
	$('#editForm').ajaxSubmit(options);
}
  errorFunc=function(){
       alert("保存失败！");
  }
  successFunc=function(responseText, statusText, xhr, $form){
       if(responseText!="0"){
    	   alert("保存成功!");
    	   cancel();
       }
	   else if(responseText=="error"){
		   alert("保存失败！");
	   } 
  }
  
//关闭窗口
function cancel(){
	api.close();
}