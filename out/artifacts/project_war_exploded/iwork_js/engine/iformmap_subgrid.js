		$(document).ready(function(){
			var lastIndex;
			$('#subTable_grid').datagrid({
				idField:'ID', 
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				}
				,
				onDblClickRow:function(rowIndex){
					var row = $('#subTable_grid').datagrid('getSelected');
					var viewdate =new Date();
					var time = viewdate.getTime();
					var pageUrl = "sysEngineSubform!subformedit.action?subformid="+row.ID;
					 art.dialog.open(pageUrl,{
						 id:'formMapIndexWinDiv',
						 title:'行项目子表维护['+row.TITLE+']',
						 lock:true,
						 background: '#999', // 背景色
					     opacity: 0.87,	// 透明度
					     width:380,
					     height:330
					 });
				}
			});
		});
		
		//添加子表
		function addSubForm(formid){
			var pageUrl = 'sysEngineSubform_add.action?formid='+formid;
			 art.dialog.open(pageUrl,{
				 id:'subformWinDiv',
				 title:'新增子表模型',  
				 lock:true,
				 background: '#999', // 背景色
			     opacity: 0.87,	// 透明度
			     width:550,
			     height:480
			 });
		}
		//编辑子表
		function editSubForm(formid,id){
			var pageUrl = 'sysEngineSubform_edit.action?formid='+formid+"&id="+id;
			 art.dialog.open(pageUrl,{
				 id:'subformWinDiv',
				 title:'更新子表模型',  
				 lock:true,
				 background: '#999', // 背景色
			     opacity: 0.87,	// 透明度
			     width:550,
			     height:480
			 });
			
		}

		//删除子表行项目 
		function del(subformid){
			if(confirm("确认删除当前行项目模型吗？")){
				var formid = $('#formid').val();
            	document.forms["editForm"].action="sysEngineSubform_remove.action?id="+subformid;
				document.forms["editForm"].submit();
             }
		}
		
