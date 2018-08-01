$(function(){ 
			//加载导航树
			var status = 1;
			var setting = {
					async: {
						enable: true, 
						url:"processDeploy_showtree.action",
						dataType:"json"
					},
					callback: { 
						onClick:onClick
					}
				}; 
			var treeObj = $.fn.zTree.init($("#processtree"), setting);
		  $("#view").click(function(){ 
					$("#view").toggleClass("view2");
					var parentid = $("#list_showtype").val();
					if(status==0){
						submitList(parentid,status);
						status = 1;
					}else{
						submitList(parentid,status);
						status = 0; 
					}
		  		});
			})
			//查询回车事件
			function enterKey(){
				if (window.event.keyCode==13){
					 doSearch();
					return false;
				}
			} 
			function doSearch(){ 
				  		  var searchTxt = $("#searchTxt").val(); 
				  		   $("#search_btn").toggleClass("search_btn_onclick");
						  if(searchTxt==''){
						  		alert('请填写查询条件');
						  		return false;
						  }else{ 
						   $("#searchkey").val(searchTxt); 
						  	var url = "processDeploy_search.action?showtype="+status;
							$('form').attr('action',url);
							$('form').attr('target',"processFrame");
							$('form').submit();  
						  }
		    }
			//点击树视图事件
			function onClick(){
				var zTree = $.fn.zTree.getZTreeObj("processtree");
				var nodes = zTree.getSelectedNodes();
				var parentid = 0;
				if(nodes.length>0){
					parentid = nodes[0].id;
					var type = nodes[0].type;
					if(type=='group'){
						zTree.expandNode(nodes[0], true, null, null, true);
						var url = "processDeploy_showList.action?groupId="+parentid;
						$('form').attr('action',url);
						$('form').attr('target',"processFrame");
		               	$("#editForm").submit();
						
					}else if(type=='process'){
						var actDefId = nodes[0].actDefId; 
						var url = "processDeploy_showList.action?actProcId="+actDefId;
						$('form').attr('action',url);
						$('form').attr('target',"processFrame");
		               	$("#editForm").submit();
					}
				}
			}
			
			function addProcess(){
				var zTree = $.fn.zTree.getZTreeObj("processtree");
				var nodes = zTree.getSelectedNodes();
				var parentid = 0;
				if(nodes.length>0){
					parentid = nodes[0].id; 
				}else{
					alert("请选择您要添加的流程分组目录");
					return;
				}
				var pageUrl = "processDeploy_process_add.action?groupId="+parentid;
				 art.dialog.open(pageUrl,{
					 id:"newProcessDg",
						title:'新建流程模型',
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:450,
					    height:400,
					    close:function(){
					 		window.location.reload(true);
				 		}
				});
				
			}
			//执行删除操作
			function remove(){
				art.dialog.confirm('确认删除?',function(result){  
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