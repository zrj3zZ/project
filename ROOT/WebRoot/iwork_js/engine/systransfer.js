/**
 * 
 */
 $(function(){ 
			//加载导航树
			var setting = {
					async: {
						enable: true, 
						url:"systransfer_showjson.action",
						dataType:"json"
					},
					callback: {
						onClick:selectTransfer 
					}
				}; 
			var treeObj = $.fn.zTree.init($("#transferTree"), setting);
			$(document).bind("contextmenu",function(e){
	              return false;   
	        });
	}); 
	function selectTransfer(event, treeId, treeNode, clickFlag){
		if(treeNode.isParent){
			var zTree = $.fn.zTree.getZTreeObj("transferTree");
	 		zTree.expandNode(treeNode, true, null, null, true);
		}else{
			if(treeNode.type!='group'){
				var url = "systransfer_showPage.action?spaceId="+treeNode.id;
				$('form').attr('action',url);
				$('form').attr('target',"transferFrame");
				$('form').submit(); 
			}
		}
	}
	function doImpModule(){
 		var url='sysImp_doExecute.action';
		    $.post(url,{},function(data){
		    
		    });
 	}
 	
 	function uploadModule(){
 		var pageUrl = "sysImp_uploadModule.action"; 
 		art.dialog.open(pageUrl,{
			id:'sysImp_uploadModule',
			title:'上传模型库', 
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		   width:450,
	        height:380,
	        close:function (){
	             window.location.reload();
	        }
		 });
		 
 	}
	function reload(){
		this.location.reload();
	}
	function reloadTree(){
		var zTree = $.fn.zTree.getZTreeObj("transferTree");
			zTree.reAsyncChildNodes(null,"refresh",true);
	}