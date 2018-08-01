/**
 * 
 */
 var selectList = new Array();
 $(function(){ 
			//加载导航树
			var setting = {
					async: {
						enable: true, 
						url:"sysExp_showJson.action",
						dataType:"json",
						autoParam:["id","type"]
					}, 
					callback: {
						onClick:selectModule, 
						onDblClick:expandNode
					}
				}; 
			var treeObj = $.fn.zTree.init($("#transferTree"), setting);
			$(document).bind("contextmenu",function(e){
	              return false;   
	        });
	}); 
	function reload(){
		this.location.reload();
	}
	
	function expandNode(event, treeId, treeNode, clickFlag){
			if(treeNode.isParent){
					var zTree = $.fn.zTree.getZTreeObj("transferTree");
			 		zTree.expandNode(treeNode, true, null, null, true);
			}
	}
	function selectModule(){
			var zTree = $.fn.zTree.getZTreeObj("transferTree");
			var nodes = zTree.getSelectedNodes();
			var treeNode;
				if(nodes.length>0){
					treeNode =nodes[0];
				}
				if(treeNode.isParent){
					var zTree = $.fn.zTree.getZTreeObj("transferTree");
			 		zTree.expandNode(treeNode, true, null, null, true);
				}else{ 
					 addList(treeNode);
				}
    } 
    
    function addList(treeNode){
    		if(selectList.length>0){
    			var flag = false;
    			for(var i=0;i<selectList.length;i++){
    				if(treeNode.id==selectList[i].id){
    					flag = true;
    					break;
    				}
    			}
    			if(!flag){
    				$("#selectModule").append("<option value='"+treeNode.typeId+"|"+treeNode.id+"'>【"+treeNode.typeName+"】"+treeNode.name+"</option>");  
	    			selectList.push(treeNode);
    			}else{
    				 art.dialog.tips(treeNode.typeName+"已添加",1);
    			}
    		}else{
	    		$("#selectModule").append("<option value='"+treeNode.typeId+"|"+treeNode.id+"'>【"+treeNode.typeName+"】"+treeNode.name+"</option>");  
	    		selectList.push(treeNode);
    		}
    		
	}
	function deleteItem(){
		if($("#selectModule option:selected").length>0){
　　　　　　　　$("#selectModule option:selected").each(function(){
　　　　　　　　　　　　　$(this).remove();
					//移出列表
					for(var i=0;i<selectList.length;i++){ 
						var selectVal = $(this).val();
						var idVar = "";
						if(selectVal.indexOf("|")>0){
							idVar = selectVal.substring(selectVal.indexOf("|")+1);
						}
						if(selectList[i].id==idVar){
							selectList.splice(i, 1);
						}
					}
　　　　　　　　})
　　　　　}else{
　　　　　　　　alert("请选择要删除的分包！");
　　　　　}
	}
	
	//全部展开
		function expandAll() {
			var zTree = $.fn.zTree.getZTreeObj("transferTree");
				zTree.expandAll(true);
		}
		//全部折叠
		function unExpandAll() {
			var zTree = $.fn.zTree.getZTreeObj("transferTree");
			zTree.expandAll(false);
		}