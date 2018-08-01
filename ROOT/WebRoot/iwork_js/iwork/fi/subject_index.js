		$(function(){ 
			//加载导航树
			var setting = {
					async: {
						enable: true, 
						url:"iwork_fi_subject_Json.action",
						dataType:"json" 
					},
					callback: {
						onClick:onClick
					} 
				}; 
			var treeObj = $.fn.zTree.init($("#subject_tree"), setting);
		})
		//点击树视图事件
 
		function onClick(event, treeId, treeNode, clickFlag){
			var categoryid = treeNode.FYLBBH;
			var url;
			if(typeof(categoryid)=="undefined"){
				 url = "iwork_fi_subject_group_list.action?groupid="+treeNode.FZBH;
			}else{
				 url = "iwork_fi_subject_group_list.action?groupid="+treeNode.FZBH+"&categoryid="+categoryid;
			}
			
			$('#editForm').attr('action',url); 
			$('#editForm').attr('target',"groupFrame");
			$('#editForm').submit(); 
		}
		//执行删除操作 
		function remove(){
				 $.messager.confirm('确认','确认删除?',function(result){  
				 	if(result){
	                	var row = $('#metadata_grid').datagrid('getSelected');
						if (row){
							var index = $('#metadata_grid').datagrid('getRowIndex', row);
							$('#metadata_grid').datagrid('deleteRow',index);
	                    	document.forms[0].action="sysEngineMetadata!remove.action?id="+row.ID;
			 				document.forms[0].submit();
			 			}
                    }
            })
		}