$(function(){
		   
	//加载系统收藏夹列表
	$('#videogrid').datagrid({
		loadMsg: '数据加载中，请稍后...',
		idField:'id',
		url:'videoEdit.action',
		fitColumns: true,//让列宽自动适应数据表格的宽度。
		striped: true,//交替显示行背
		singleSelect:true,
		nowrap:false,
		rownumbers:true,
		sortName: 'uploadtime',
		sortOrder: 'desc',
		columns:[[
			{field:'title',title:'视频名称',width:80},
			{field:'videofile',title:'播放视频文件名',width:160},
			{field:'picfile',title:'预览图片文件名',width:80},
			{field:'uploader',title:'上传人',width:80},
			{field:'uploadtime',title:'上传时间',width:80,align:'center'},
			{field:'description',title:'视频描述',width:80}
		]],
		
		onBeforeLoad:function(){
			$(this).datagrid('rejectChanges');
		},
				
		onDblClickRow:function(){
			var row = $('#videogrid').datagrid('getSelected');
			document.forms[0].id.value=row.id==null?'':row.id;
			document.forms[0].title.value=row.title==null?'':row.title;
			document.forms[0].videofile.value=row.videofile==null?'':row.videofile;
			document.forms[0].picfile.value=row.picfile==null?'':row.picfile;
			document.forms[0].uploader.value=row.uploader==null?'':row.uploader;
			document.forms[0].uploadtime.value=row.uploadtime==null?'':row.uploadtime;
			document.forms[0].description.value=row.description==null?'':row.description;
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
					var url='videoEditAction.action';
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
 	document.forms[0].videofile.value='';
 	document.forms[0].picfile.value='';
	document.forms[0].uploader.value='';
	document.forms[0].uploadtime.value='';
	document.forms[0].description.value='';
	document.forms[0].type.value='add';
	$('#idialog').dialog('open'); 		
}

//删除	
function remove(){
	var row = $('#videogrid').datagrid('getSelected');
	if (row){
		var index = $('#videogrid').datagrid('getRowIndex', row);
		$('#videogrid').datagrid('deleteRow', index);	
		document.forms[0].id.value=row.id==null?'':row.id;
		document.forms[0].title.value=row.title==null?'':row.title;
		document.forms[0].videofile.value=row.videofile==null?'':row.videofile;
		document.forms[0].picfile.value=row.picfile==null?'':row.picfile;
		document.forms[0].uploader.value=row.uploader==null?'':row.uploader;
		document.forms[0].uploadtime.value=row.uploadtime==null?'':row.uploadtime;
		document.forms[0].description.value=row.description==null?'':row.description;
		document.forms[0].type.value='delete';
		var url='videoEditAction.action';	
	    document.forms[0].action=url;
	    document.forms[0].submit();
		return false; 
	}	
}


/*			
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
*/

