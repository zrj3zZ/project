 function execMyCommand(form,mycmd){
 	form.cmd.value=mycmd;
 	if(mycmd=='Org_SecurityGroup_AppModel_Save'){
    	if ( false==confirm("确认要将选择的模块授权给当前组吗？该组隶属的所有人都拥有这些模块操作操作权限。" ) ){
    		return false;
    	}  		
 		form.securityList.value=selectChecked(form);
 	}
 	form.target="_self";
 	form.submit(); 	
 	disableAll(form);
 }
 
 function AutoSelectList(form,checkflag){
	for (var i=0; i < form.elements.length; i++ ){
		form.elements[i].checked=checkflag;
	}
}

 function changeCheck(form,rootCheck,selectCheck,checkName){
 	var itemLength=checkName.length;
	for (var i=0; i < form.elements.length; i++ ){
		if(form.elements[i].id.substring(0,itemLength)==checkName && form.elements[i].id.length>itemLength){
			form.elements[i].checked=selectCheck.checked;		
		}
	}
	if(selectCheck.checked){
		rootCheck.checked="checked";
	}
}


 function spyCheck(form,rootCheck,parentCheck,selectCheck){
 	if(selectCheck.checked){
 		rootCheck.checked="checked";
 		parentCheck.checked="checked";
 	}
}


function selectChecked(form){
	var x="";
	for ( var i=0; i < form.elements.length; i++ ){
		if (( true==form.elements[i].checked) && (form.elements[i].type == 'checkbox' )) x=x +" "+ form.elements[i].value;
	}
	return x;
}