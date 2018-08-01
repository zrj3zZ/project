  function execMyCommand(form,mycmd){
	form.cmd.value=mycmd;
 	form.target="_self";
 	//save
 	if (mycmd=='Org_User_Save'){
 		if (form.userName.value==""){
 			alert("姓名不能为空！");
 			return false;
 		}
		if (!checkvalue("姓名",form.userName.value)){
			return false;
		} 
 		if (form.roleId.value==0){
 			alert("必须赋予帐户一个角色！");
 			return false;
 		}
 		if (form.uid.value==""){
 			alert("必须分配一个登录帐户，以便用登录系统！");
 			return false;
 		}
 		//add by dunan 2009-03-03
 		if (form.extend1.value==""){
 			alert("员工编号不能为空！");
 			return false;
 		}
 		//end
		if (!checkvalue("登录帐户",form.uid.value)){
			return false;
		} 
		if (!checkvalue("员工代码",form.userNo.value)){
			return false;
		} 
		if (!checkvalue("职等名称",form.positionNo.value)){
			return false;
		}
		if (!checkvalue("职等级次",form.positionLayer.value)){
			return false;
		}
		if (!checkvalue("职务名称",form.positionName.value)){
			return false;
		}
		if (!checkvalue("MSN帐户",form.msnId.value)){
			return false;
		} 		
 	}
 	//set pwd
 	else if (mycmd=='Org_User_Password_Init'){
    	if ( false==confirm("确认重新初始化用户登录口令吗？\n重新初始化的登录口令请参考system.xml中的user.default.password配置项！" ) ){
    		return false;
    	} 		
 	}else if(mycmd=='Org_User_Disable'){
    	if ( false==confirm("确认注销当前帐户吗？\n该帐户注销后系统保留全部有关数据，但该帐户将不再能够使用这些资源！" ) ){
    		return false;
    	} 	 		
 	}else if(mycmd=='Org_User_UnDisable'){
    	if ( false==confirm("确认激活当前帐户吗？\n该帐户激活后将重新被正常使用！" ) ){
    		return false;
    	} 	 		
 	}
 	disableAll(form);
 	form.submit();
 	return false; 	
 }

//add a user
 function execMyCommand2(form,mycmd){
	form.cmd.value=mycmd; 	
	form.user_id.value=0;
 	form.target="_self";
 	disableAll(form);
 	form.submit();
 	return false; 	
 } 
 
 function fingerWin(form,mycmd,userId){
	form.cmd.value=mycmd;
	form.userId.value=userId;	
	var newSystem=getNewTarget();
	window.open('../iwork_html/wait.htm',newSystem,getWindowMaxSize()+'location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=no,resizable=no');
	form.target =newSystem;
 	form.submit();
 	return false; 	
 }  