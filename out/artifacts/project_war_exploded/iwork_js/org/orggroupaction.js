//添加团队
function addGroup() {
	document.forms[0].group_id.value = '';
	document.forms[0].group_name.value = '';
	document.forms[0].group_charge.value = '';
	$('#group_state').combobox('setValue', '开启');
	$('#begindate').datebox('setValue', '');
	$('#enddate').datebox('setValue', '');
	document.forms[0].group_memo.value = '';
	document.forms[0].type.value = 'add';
	$('#groupdialog').dialog('open');
}

//删除团队
function remove() {
	//根据团队表
	var row = $('#groupgrid').datagrid('getSelected');
	if (row) {
		$.messager.confirm('确认', '确认删除?',
				function(result) {
					if (result) {
						var index = $('#groupgrid').datagrid('getRowIndex', row);
						$('#groupgrid').datagrid('deleteRow', index);
						document.forms[0].group_id.value = row.id == null ? '' : row.id;
						//	  document.forms[0].group_name.value=row.groupname==null?'':row.groupname;
				//    document.forms[0].group_charge.value=row.groupcharge==null?'':row.groupcharge;
				//    $('#group_state').combobox('setValue',row.groupstate==null?'':row.groupstate);
				//    $('#begindate').datebox('setValue',row.begindate==null?'':row.begindate);
				//    $('#enddate').datebox('setValue',row.enddate==null?'':row.enddate);
				//	  document.forms[0].group_memo.value=row.groupmemo==null?'':row.groupmemo;	
				document.forms[0].type.value = 'remove';
				var url = 'saveGroup.action';
				document.forms[0].action = url;
				document.forms[0].submit();
				return false;
			}
		})
	}
	//根据团队树
	var node = $('#grouptree').tree('getSelected');
	if (node) {
		if (node.attributes.type == "group") {
			var pageUrl = "queryGroupItems.action";
			var gid = node.id==null?'':node.id;
			$.post(pageUrl,{gid:gid},function(data){
				if(data=='true'){
					alert("团队下存在成员，请勿删除!");
					return;
				}else{
					$.messager.confirm('确认', '确认删除?', function(result) {
						if (result) {
							document.forms[0].group_id.value = node.id == null ? '' : node.id;
							document.forms[0].type.value = 'remove';
							var url = 'saveGroup.action';
							document.forms[0].action = url;
							document.forms[0].submit();
							return false;
						}
					})
				}
			});
		}
	}
}
//保存
function save() {
	if (document.forms[0].group_name.value == "") {
		alert('系统提示:请填写团队名称!');
		return;
	}
	if (document.forms[0].group_charge.value == "") {
		alert('系统提示:请填写团队负责人!');
		return;
	}
	if ($('#begindate').datebox('getValue') == "") {
		alert('系统提示:请填写开始时间!');
		return;
	}
	if ($('#enddate').datebox('getValue') == "") {
		alert('系统提示:请填写结束时间!');
		return;
	}
	if (length2(document.forms[0].group_name.value) > 64) {
		alert('系统提示:团队名称过长!');
		return;
	}
	if (length2(document.forms[0].group_charge.value) > 64) {
		alert('系统提示:团队负责人过长!');
		return;
	}
	if (length2(document.forms[0].group_memo.value) > 500) {
		alert('系统提示:备注过长!');
		return;
	}
	var url = 'saveGroup.action';
	document.forms[0].action = url;
	document.forms[0].submit();
	return;
}
//取消
function cancel() {
	$('#groupdialog').dialog('close');
}