
function doSearch(){
	var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
		return false;
	}
	var searchTxt = $("#searchTxt").val();
	if(searchTxt==""){
		alert("请输入查询关键字");
		return;
	}else{
		var url = "department_search.action?searchkey="+encodeURI(searchTxt);
		$('#editForm').attr('action',url); 
		$('#editForm').attr('target',"departmenFrame");
		$('#editForm').submit(); 
	}
}


function deleteCompany(id){
	art.dialog.confirm('确认删除组织?', function(r){
		if (r){										
			var pageurl = "company_delete.action";
			 $.post(pageurl,{id:id},function(msg){ 
				 if(msg=='success'){
					 location.reload();
				 }else{
					 alert('当前组织下仍包含未删除的部门,不允许删除!');
				 }
				 }); 
			return;
		}
	});
}
function confirm1(id){
			art.dialog.confirm('确认删除部门?', function(r){
				if (r){	
					var pageurl="department_delete.action";
					$.post(pageurl,{id:id},function(msg){ 
						/* if(msg=='success'){*/
							 location.reload();
						/* }else{
							 alert('当前组织下仍包含未删除的用户,不允许删除!');
						 }*/
						 }); 
				}
			}); 
		}
function addDept(){
	var zTree = $.fn.zTree.getZTreeObj("deptTree");
	var nodes = zTree.getSelectedNodes();
	var parentid = 0;
	var deptname = "";
	var companyId = 0;
	if(nodes.length>0){
		var type = nodes[0].nodeType;
		if(type=='company'){  
			parentid = "0";
			deptname = "";
			companyId = nodes[0].id;
		}else if(type=='dept'){  
			parentid = nodes[0].id;
			deptname = nodes[0].name;
		}else{ 
			alert('请在部门树中选择您要添加的部门位置');
			return;
		} 
			var pageUrl = "department_add.action?parentdeptid="+parentid+"&companyId="+nodes[0].companyId+"&parentdeptname="+encodeURI(deptname); 
			 art.dialog.open(pageUrl,{
				 	id:'companyWinDiv',
					title:'新建部门',   
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:650,
				    height:510,
				    close:function(){
				 		document.getElementById('departmenFrame').contentWindow.location.reload(true);
			 		}
				 });
		
	}else{
		alert('请在部门树中选择您要添加的部门位置');
		return;
	}
	
}
function editDept(id){
	var pageUrl = "department_edit.action?id="+id; 
	 art.dialog.open(pageUrl,{
		 	id:'editDeptDlg',
		 	title:'部门信息维护',  
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:650,
		    height:510,
		    close:function(){
		 		window.location.reload(true);
	 		}
		 });
}


function addcompany(){
	var zTree = $.fn.zTree.getZTreeObj("orgUserTree");
	var nodes = zTree.getSelectedNodes();
	var parentid = 0;
	if(nodes.length>0){
		var type = nodes[0].nodeType;
			parentid = nodes[0].id;
		
    	var pageUrl = "company_add.action?parentid="+parentid; 
    	art.dialog.open(pageUrl,{
		 	id:'addCompanyWinDiv',
		 	title:'新建组织单元',  
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:650,
		    height:510,
		    close:function(){
    		document.getElementById('orgUserFrame').contentWindow.location.reload(true);
    		}
		 });
	}else{
		alert('请在组织树中选择您要添加的分子公司');
	}
}
function editcompany(id){
	var pageUrl = "company_edit.action?id="+id; 
	art.dialog.open(pageUrl,{
	 	id:'editComWinDiv',
	 	title:'维护组织单元',   
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:650,
	    height:510
	 });
}