$(function(){ 
			//加载导航树
			var setting = {
					async: {
						enable: true, 
						url:"sysEngineIForm_showJSON.action",
						dataType:"json" 
					},
					callback: {
						onClick:onClick 
					}
				};  
		var treeObj = $.fn.zTree.init($("#formtree"), setting);
		})
		//点击树视图事件

		function onClick(event, treeId, treeNode, clickFlag){
			var zTree = $.fn.zTree.getZTreeObj("formtree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0; 
			if(nodes.length>0){ 
				parentid = nodes[0].id;
				var type = nodes[0].type;
				if(type=='group'){
					zTree.expandNode(treeNode, true, null, null, true);
					submitList(parentid,0);
				}else if(type=='iform'){
				//	var url = 'sysEngineMetadataMap!index.action?metadataid='+parentid;
					openFormMap(nodes[0].name,parentid);
				}
			}
		}
		//提交显示列表
		function submitList(id,type){ 
			$("#list_showtype").val(id);
			var url = "sysEngineIForm_showList.action?groupid="+id+"&showtype="+type;
			$('form').attr('action',url); 
			$('form').attr('target',"iformListFrame");
			$('form').submit();
		}
		function addForm(){ 
			var zTree = $.fn.zTree.getZTreeObj("formtree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0;
			if(nodes.length>0){
				parentid = nodes[0].id;
				var type = nodes[0].type;
				if(type=='group'){  
					var pageUrl = "sysEngineIForm_add.action?groupid="+parentid; 
					art.dialog.open(pageUrl,{
						id:'iformBaseWinDiv',
						title:'新建表单模型',  
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:650,
					    height:'90%'
					 });
				}else{
					alert('请在树视图中选择您要添加的目录');
					return;
				}
			}else{
				alert('请在树视图中选择您要添加的目录');
				return;
			}
		}