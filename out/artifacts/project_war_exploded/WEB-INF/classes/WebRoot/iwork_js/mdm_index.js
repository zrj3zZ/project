		$(function(){ 
			//加载导航树
			var setting = {
					async: {
						enable: true, 
						url:"sysDem_Mdm_json.action",
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
				}else if(type=='dem'){
					openBaseInfo(nodes[0].uuid);
				} 
			}
		}
		//提交显示列表
		function openBaseInfo(uuid){  
			var url = "sysDem_DataList.action?demUUID="+uuid;
			
			//openWin(url)
			$('form').attr('action',url);
			$('form').attr('target',"demListFrame");
			$('form').submit();
		}
		function openWin(pageUrl){
			art.dialog.open(pageUrl,{
				id:'mdmBaseWinDiv',
				title:'新建数据管理模型',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:'90%',
			    height:'90%'
			 });
		}