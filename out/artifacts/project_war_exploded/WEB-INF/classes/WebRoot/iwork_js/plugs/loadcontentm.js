function editContent(){
	var rows = $('#metadata_grid').datagrid('getSelections');
						if(rows.length==0){
							art.dialog.alert('警告请选择要修改的记录!');
						}else{				
					$('#contentsedit').dialog('open');
}
}