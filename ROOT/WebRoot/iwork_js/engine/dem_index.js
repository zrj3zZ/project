		$(function(){ 
			//加载导航树
			var setting = {
					async: {
						enable: true, 
						url:"sysDem_show_tree.action",
						dataType:"json" 
					},
					callback: {
						onClick:onClick
					} 
				}; 
			var treeObj = $.fn.zTree.init($("#dem_tree"), setting);
		})
		//点击树视图事件
 
		function onClick(event, treeId, treeNode, clickFlag){
			var zTree = $.fn.zTree.getZTreeObj("dem_tree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0;
			if(nodes.length>0){ 
				parentid = nodes[0].id;
				var type = nodes[0].type;
				if(type=='group'){ 
					zTree.expandNode(treeNode, true, null, null, true);
					submitList(parentid,0);
					 
				}else if(type=='dem'){
					openBaseInfo(nodes[0].name,parentid);
				} 
			}
		}
		
		//提交显示列表
		function submitList(id,type){  
			$("#list_showtype").val(id);
			var url = "sysDem_showList.action?groupid="+id+"&id="+id+"&showtype="+type;
			$('form').attr('action',url);
			$('form').attr('target',"demListFrame");
			$('form').submit();
		}
		function addDem(){
			var zTree = $.fn.zTree.getZTreeObj("dem_tree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0;
			if(nodes.length>0){
				parentid = nodes[0].id;
				var type = nodes[0].type;
				if(type=='group'){ 
					var pageUrl = "sysDem_add.action?groupid="+parentid;
					art.dialog.open(pageUrl,{
						id:'addressDialog', 
						title:"新增",
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:500,
					    height:350
					 });
				}else{
					alert('请在树视图中选择您要添加的目录');
					return ;
				}
			}else{
				alert('请在树视图中选择您要添加的目录');
				return ;
			}
		}
		
		//执行删除操作 
		function remove(){
			art.dialog.confirm('确认','确认删除?',function(result){  
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