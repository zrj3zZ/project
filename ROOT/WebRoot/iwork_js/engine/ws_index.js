$(function(){ 
			//加载导航树
			var setting = {
					async: {
						enable: true, 
						url:"web_service_showGroupJSON.action", 
						dataType:"json"
					},
					callback: {
						onClick:onClick 
					}
				}; 
			var treeObj = $.fn.zTree.init($("#grouptree"), setting);
		})
		//点击树视图事件

		function onClick(event, treeId, treeNode, clickFlag){
			var zTree = $.fn.zTree.getZTreeObj("grouptree");
			submitList(treeNode.id);
		}
		//提交显示列表
		function submitList(id){ 
			var url = "web_service_showlist.action?groupid="+id;
			$('form').attr('action',url); 
			$('form').attr('target',"listFrame");
			$('form').submit();
		}
		
		function addInterface(){ 
			var zTree = $.fn.zTree.getZTreeObj("grouptree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0;
			if(nodes.length>0){
				parentid = nodes[0].id;
				var type = nodes[0].type;
					var pageUrl = "web_service_add.action?groupid="+parentid; 
				    art.dialog.open(pageUrl,{
						id:'newWebservice', 
						title:'新建WebService管理',  
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:600,
					    height:400
					 });
			}else{
				alert('请在树视图中选择您要添加的目录');
				return;
			}
		}