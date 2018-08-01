//删除	
function remove(){
	var selectedIds = $("#syserrorloggrid").jqGrid('getGridParam','selarrrow');
	if (selectedIds){
		 $.messager.confirm('确认','确认删除?',function(result){
		 	if(result){
				var id = selectedIds;
				window.location.href='sys_error_log_delete.action?id=' + id;
		 	}
		 })
	}	
}

//查询
function query() {
	var url='sys_error_log.action';
	document.forms[0].action=url;
    document.forms[0].isDoSearch.value='true';
    document.forms[0].operateDateStart.value=$dp.$('start').value;
	document.forms[0].operateDateEnd.value=$dp.$('end').value;
	document.forms[0].operateUser.value=$('#user').val();
	document.forms[0].functionName.value=$('#function').val();
    document.forms[0].submit();
    return false;
}