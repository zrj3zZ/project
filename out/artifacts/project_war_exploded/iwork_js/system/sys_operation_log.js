//删除	
function remove(){
	var selectedIds = $("#sysoperateloggrid").jqGrid('getGridParam','selarrrow');
	if (selectedIds){
		 $.messager.confirm('确认','确认删除?',function(result){
		 	if(result){
				var id = selectedIds;
				window.location.href='sys_operate_log_delete.action?id=' + id;
		 	}
		 })
	}	
}

//查询
function query() {
	document.forms[0].operateDateStart.value=$dp.$('start').value;
	document.forms[0].operateDateEnd.value=$dp.$('end').value;
	document.forms[0].isDoSearch.value='true';
	document.forms[0].operateUser.value=$('#user').val();
	document.forms[0].operateType.value=$('#type').val();
	document.forms[0].operateTable.value=$('#table').val();
	document.forms[0].functionName.value=$('#function').val();
   	document.forms[0].submit();
   	return false;
}