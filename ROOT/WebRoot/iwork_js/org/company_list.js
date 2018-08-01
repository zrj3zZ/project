	    function addcompany(){
	    	var zTree = $.fn.zTree.getZTreeObj("companyTree");
	    	var nodes = zTree.getSelectedNodes();
	    	var parentid = 0;
	    	if(nodes.length>0){
	    		var type = nodes[0].nodeType;
	    			parentid = nodes[0].id;
		    	var pageUrl = "company_add.action?parentid="+parentid; 
		    	 art.dialog.open(pageUrl,{
					 	id:'companyWinDiv',
					 	title:'新建组织单元',     
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:650,
					    height:510
					 });
	    	}else{
	    		alert('请在组织树中选择您要添加的分子公司');
	    	}
		}
	    function editcompany(id){
	    	var pageUrl = "company_edit.action?id="+id; 
	    	 art.dialog.open(pageUrl,{
				 	id:'companyWinDiv',
				 	title:'维护组织单元',      
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:650,
				    height:510
				 });
		}
	    function doSearch(){
	    	if(document.all.searchValue.value=="")
	    	{	
	    		alert("请输入查询关键字!");
	    	}else{
	    		window.location.href="sysindex.action?queryName="+document.all.searchName.value+"&&queryValue="+document.all.searchValue.value;
	    	}
	    }
		function confirm1(id){
			art.dialog.confirm('确认删除?', function(r){
				if (r){										
					location.href="company_delete.action?id="+id;
					return false;
				}
			});
		}
		window.onload=delAlert;
		function delAlert(){
			var delMsg = '<s:property value="delInfo"  escape="false"/>';
			if(delMsg == "1"){
				return false;
			}else if(delMsg == "2"){
				$.messager.alert('系统提示','您删除的公司由于还存在部门不能强制删除!','error');
			}else{
				return false;
			}
		}