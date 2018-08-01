$(function(){
		   
	//加载系统收藏夹列表
	$('#appkmgrid').datagrid({
		loadMsg: '数据加载中，请稍后...',
		idField:'id',
		url:'appkmList.action',
		fitColumns: true,//让列宽自动适应数据表格的宽度。
		striped: true,//交替显示行背
		singleSelect:true,
		nowrap:false,
		rownumbers:true,
		sortName: 'uploadtime',
		sortOrder: 'desc',
		columns:[[
			{field:'title',title:'标题名称',width:80},
			{field:'url',title:'链接url',width:80},
			//{field:'sequence',title:'排序',width:80,align:'center'}
		]],
		
		onBeforeLoad:function(){
			$(this).datagrid('rejectChanges');
		},
				
		onDblClickRow:function(){
			var row = $('#appkmgrid').datagrid('getSelected');
			document.forms[0].id.value=row.id==null?'':row.id;
			document.forms[0].title.value=row.title==null?'':row.title;
			document.forms[0].url.value=row.url==null?'':row.url;
			document.forms[0].sequence.value=row.sequence==null?'':row.sequence;
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
				
				}else{
					var url='appkmEdit.action';
					$('#iform').attr('action',url);
					$('#iform').submit();
					return false; 
				}
			}
		},
		
		{
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
	document.forms[0].id.value='';
	document.forms[0].title.value='';
 	document.forms[0].url.value='';
 	document.forms[0].sequence.value='';
	document.forms[0].type.value='add';
	$('#idialog').dialog('open'); 		
}

//删除	
function remove(){
	var row = $('#appkmgrid').datagrid('getSelected');
	if (row){
		var index = $('#appkmgrid').datagrid('getRowIndex', row);
		$('#appkmgrid').datagrid('deleteRow', index);	
		document.forms[0].id.value=row.id==null?'':row.id;
		document.forms[0].title.value=row.title==null?'':row.title;
		document.forms[0].url.value=row.url==null?'':row.url;
		document.forms[0].sequence.value=row.sequence==null?'':row.sequence;
		document.forms[0].type.value='delete';
		var url='appkmEdit.action';	
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

