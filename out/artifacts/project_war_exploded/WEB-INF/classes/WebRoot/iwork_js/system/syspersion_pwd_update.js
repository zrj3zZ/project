function doSubmit(){
/*	var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
		return;
	}*/
	if(frmMain.txtNewPwd.value==''||frmMain.txtConfirmPwd.value==''||frmMain.txtOldPwd.value==''){
		art.dialog.tips("您输入的信息不完整，请重输！",2);
		//alert('');
		return;
	}
	if(frmMain.txtNewPwd.value!=frmMain.txtConfirmPwd.value){
		art.dialog.tips("两次输入的密码不一样，请重输！",2);
		return;
	}
	 var formdata = $('#frmMain').serialize();
	 if(frmMain.txtNewPwd.value.indexOf("&")>=0||frmMain.txtNewPwd.value.indexOf("<")>=0||frmMain.txtNewPwd.value.indexOf(">")>=0){
		 art.dialog.tips("1、新密码不能带&,<,>字符");
	 }else{
	    $.post('syspersion_exec_pwd_update.action',formdata,function(data){
	    	art.dialog.tips(data);
	    	if(data=='密码修改成功！'){
	    		setTimeout('reloadPage()',10000);
	    	}
	    });
	 }
}

function reloadPage(){
	window.location.reload(true);
}