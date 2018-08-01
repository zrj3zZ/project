
//发布		    
function doDeploy(){
	editor1.sync();//同步html编辑器  
	var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
			return;
		}
	 var options = {
				error:errorFunc,
				success:successFunc 
			   }; 
			$('#editForm').ajaxSubmit(options);
	 return false;  
}
errorFunc=function(){
    alert("发布失败！"); 
 } 
 successFunc=function(responseText, statusText, xhr, $form){
        if(responseText=="success"){
            alert("发布成功!");
            window.close(); 
        }
        else if(responseText=="error"){
           alert("发布失败!");
        } 
 }							
//作废			
function closewin(){
	 window.close(); //自动关闭本窗口
}	 