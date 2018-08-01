$(function(){

		   //加载系统收藏夹列表
			$('#sysfavgrid').datagrid({
			    loadMsg: '数据加载中，请稍后...',
			    idField:'id',
			    url:'confav.action',
				fitColumns: true,
				striped: true,
				singleSelect:true,
				nowrap:false,
				rownumbers:true,
				sortName: 'sys_index',
				sortOrder: 'desc',
				columns:[[
					{field:'sys_name',title:'系统名称',width:80},
					{field:'sys_url',title:'系统URL',width:160},
					{field:'sys_target',title:'提交目标',width:80},
					{field:'sys_memo',title:'备注',width:80},
					{field:'index',title:'排序',width:80,align:'center'}
				]],
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onDblClickRow:function(){
					var row = $('#sysfavgrid').datagrid('getSelected');
					document.forms[0].sys_id.value=row.id==null?'':row.id;
					document.forms[0].sys_name.value=row.sys_name==null?'':row.sys_name;
				    document.forms[0].sys_url.value=row.sys_url==null?'':row.sys_url;
				    $('#sys_target').combobox('setValue',row.sys_target==null?'':row.sys_target);
				    //document.forms[0].sys_target.value=row.sys_target==null?'':row.sys_target;
					document.forms[0].sys_memo.value=row.sys_memo==null?'':row.sys_memo;
					document.forms[0].sys_index.value=row.sys_index==null?'':row.sys_index;
					document.forms[0].type.value='edit';
					$('#idialog').dialog('open'); 
				}							
			});	 
			
			//加载编辑对话框   
	       $('#idialog').dialog({
	            title:'编辑',
	            modal:true,
				toolbar:[{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
					     if(!$('#iform').form('validate')){
					        
					     }
						 else{
						    var url='savesysfav.action';	
				            $('#iform').attr('action',url);
				            $('#iform').submit();
				 		    return false; 
						 }
					}
				},{
					text:'取消',
					iconCls:'icon-cancel',
					handler:function(){
						 $('#idialog').dialog('close');
					}
				}]

		    });
			
	        //隐藏 
	        $('#idialog').dialog('close');  	             
});		

//添加
function add(){
	document.forms[0].sys_id.value='';
	document.forms[0].sys_name.value='';
 	document.forms[0].sys_url.value='';
 	$('#sys_target').combobox('setValue','');
	//document.forms[0].sys_target.value='';
	document.forms[0].sys_memo.value='';
	document.forms[0].sys_index.value='';
	document.forms[0].type.value='add';
	$('#idialog').dialog('open'); 		
}

//删除	
function remove(){
	var row = $('#sysfavgrid').datagrid('getSelected');
	if (row){
		var index = $('#sysfavgrid').datagrid('getRowIndex', row);
		$('#sysfavgrid').datagrid('deleteRow', index);	
		document.forms[0].sys_id.value=row.id==null?'':row.id;
		document.forms[0].sys_name.value=row.sys_name==null?'':row.sys_name;
		document.forms[0].sys_url.value=row.sys_url==null?'':row.sys_url;
	 	$('#sys_target').combobox('setValue',row.sys_target==null?'':row.sys_target);
	   	//document.forms[1].sys_target.value=row.sys_target==null?'':row.sys_target;
		document.forms[0].sys_memo.value=row.sys_memo==null?'':row.sys_memo;
		document.forms[0].sys_index.value=row.sys_index==null?'':row.sys_index;
		document.forms[0].type.value='delete';
		var url='savesysfav.action';	
	    document.forms[0].action=url;
	    document.forms[0].submit();
		return false; 
	}	
}
			
//排序	
function move(sys_id,sys_index,isys_id,isys_index){
	document.forms[0].sys_id.value=sys_id;
	document.forms[0].sys_index.value=sys_index;
	document.forms[0].isys_id.value=isys_id;
	document.forms[0].isys_index.value=isys_index;
	var url='move.action';	
	document.forms[0].action=url;
	document.forms[0].submit();
	return false; 
}
/*
$.extend($.fn.validatebox.defaults.rules, {
    maxLength: {
        validator: function(value, param){
            var cArr = value.match(/[^\x00-\xff]/ig);
            var length = value.length + (cArr == null ? 0 : cArr.length);  
            return length <= param[0];
        },
        message: '输入值过长'
    },
  noSelect:{
        validator: function(value, param){
            return value!='请选择..';
        },
        message: '请选择'
    }
});
*/

