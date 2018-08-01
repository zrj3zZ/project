
//弹出上传头像窗口
function add_image(){
		var userid = $('#userid').val();
		if($('#isMOdelNull').html()=='null'){
			alert("请您先保存用户信息后再修改头像!");
		}else{
			var pageUrl = "syspersion_photo.action?userid="+userid;
			art.dialog.open(pageUrl,{ 
	        id:'dg_addImage',  
	        title:'上传头像',
	        iconTitle:false, 
	        width:500,
	        heigh:500,
	         close:function(){
	        	window.location.reload();
	        	}
			});
		}
}
//删除头像
function delete_image(){
 var userid = $('#userid').val();
 var isExist = '<s:property value="isUserImageExists"/>';
  var msg = "您确定要删除吗?";
  if(isExist==0){
  	  alert("此用户还未上传照片!");
  }else{
  	  if (confirm(msg) == true) {
	    window.location.href="syspersion_photo_deleteImage.action?userid="+userid;
	  }else {
	   	return false;
	  }
  }
}
function initpwd(){
	var userid = $('#userid').val();
		var pageUrl = "user_pwd_showpage.action?userid="+userid; 
		art.dialog.open(pageUrl,{
			id:'dg_initPWD', 
			cover : true,
			title : '登录账户初始化',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 300,
			top:-500,
			cache : false,
			lock : false,
			height : 240,
			iconTitle : false,
			extendDrag : true,
			autoSize : false
		});
}
//返回
function goBack(){
	var departmentid=$('#user_save_model_departmentid').val();
	window.location.href='user_list.action?departmentid='+departmentid;
}
function SetCwinHeight(obj){
	  var cwin=obj;
	  if (document.getElementById)
	  {
		if (cwin && !window.opera)
		{
		  if (cwin.contentDocument && cwin.contentDocument.body.offsetHeight)
			cwin.height = cwin.contentDocument.body.offsetHeight; 
		  else if(cwin.Document && cwin.Document.body.scrollHeight)
			cwin.height = cwin.Document.body.scrollHeight;
		}
	  }
}