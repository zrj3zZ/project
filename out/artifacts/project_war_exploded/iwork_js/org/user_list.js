
function deleteDept(id){
	 art.dialog.confirm('确认删除部门?', function(){
					var pageurl = "department_delete.action";
					 $.post(pageurl,{id:id},function(msg){ 
							 if(msg=='success'){
								 location.reload();
							 }else{
								 alert('当前用户包含兼任信息,不能删除!');
							 }
						 }); 
					return;
			});
		}
function deleteUser(id){
	 art.dialog.confirm('确认是否删除当前用户', function(){
					var pageurl = "user_delete.action";
					 $.post(pageurl,{id:id},function(msg){ 
						 if(msg=='success'){
							 location.reload(); 
						 }else{
							 alert('当前用户包含兼任信息,不能删除!');
						 }
						 }); 
					return;
			}); 
		}
function removeUserMap(id){
	 art.dialog.confirm('确认删除兼任用户?', function(r){
		if (r){							
			var pageurl = "usermap_delete.action";
			 $.post(pageurl,{id:id},function(msg){ 
					 location.reload();
				 }); 
			return;
		}
	});
}
//查询回车事件
function enterKey(){
	if (window.event.keyCode==13){
		 doSearch();
		return;
	}
}
function doSearch(){
	var searchTxt = $("#searchTxt").val();
	if(searchTxt==""){
		alert("请输入查询关键字");
		return;
	}else{ 
		searchTxt = searchTxt.toUpperCase()
		var url = "user_search.action?searchkey="+encodeURI(searchTxt);
		$('#editForm').attr('action',url); 
		$('#editForm').attr('target',"orgUserFrame");
		$('#editForm').submit(); 
	}
}
function addUser(){
	var zTree = $.fn.zTree.getZTreeObj("orgUserTree");
	var nodes = zTree.getSelectedNodes();
	var parentid = 0;
	if(nodes.length>0){
		var type = nodes[0].nodeType;
		if(type=='company'){  
			parentid = "0";
			art.dialog.tips('组织下不允许直接建立账号，请选择您要建立账户的部门');
			return;
		}else if(type=='dept'){  
			parentid = nodes[0].id;
		}else{
			art.dialog.tips('请选择左侧的组织结构树中要添加的位置');
			return;
		}
			var pageUrl = "user_add.action?departmentid="+parentid; 
			 art.dialog.open(pageUrl,{
			    	id:'orguserWinDiv',
			    	title:'新建用户',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:900,
				    height:600,
					close:function(){
				 		document.getElementById('orgUserFrame').contentWindow.location.reload(true);
					}
				 });
		
	}else{
		art.dialog.tips('请在部门树中选择您要添加的部门位置');
		return;
	}
	
}
function editUser(deptid,userid){ 
	var pageUrl = "user_edit.action?departmentid="+encodeURI(deptid)+"&userid="+encodeURI(userid); 
	art.dialog.open(pageUrl,{
    	id:'orguserWinDiv',
    	title:'用户信息维护',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:900,
	    height:600, 
		close:function(){
	        location.reload();
		}
	 });
}

function addDept(){
	var zTree = $.fn.zTree.getZTreeObj("orgUserTree");
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
			art.dialog.tips('请在部门树中选择您要添加的部门位置');
			return;
		} 
			var pageUrl = "department_add.action?parentdeptid="+parentid+"&companyId="+nodes[0].companyId+"&parentdeptname="+encodeURI(deptname);
			art.dialog.open(pageUrl,{
		    	id:'orguserWinDiv',
		    	title:'新建部门', 
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:650,
			    height:500,
				close:function(){
					document.getElementById('orgUserFrame').contentWindow.location.reload(true);
				}
			 });
		
	}else{
		art.dialog.tips('请在部门树中选择您要添加的部门位置');
		return;
	}
	
}
function editDept(id){
	var pageUrl = "department_edit.action?id="+id; 
	art.dialog.open(pageUrl,{
    	id:'orguserWinDiv',
    	title:'部门信息维护',  
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:650,
	    height:500,
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
		if(type=='dept'){
			art.dialog.tips('部门下不能添加组织');
			return;  
		}
		var pageUrl = "company_add.action?parentid="+parentid; 
		art.dialog.open(pageUrl,{
	    	id:'orguserWinDiv',
	    	title:'新建组织单元',  
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:750,
		    height:550,
			close:function(){
				document.getElementById('orgUserFrame').contentWindow.location.reload(true);
			}
		 });
	}else{
		art.dialog.tips('请在组织树中选择您要添加的分子公司');
	}
}

function editcompany(id){
	var pageUrl = "company_edit.action?id="+id; 
	
	art.dialog.open(pageUrl,{
    	id:'orguserWinDiv',
    	title:'维护组织单元',  
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:750,
	    height:550,
		close:function(){
	        location.reload();
		}
	 });
}
function moveUpDept(id){
	var pageurl = "department_moveup.action";
	 $.post(pageurl,{id:id},function(msg){ 
	    	location.reload(); 
		 }); 
	return;
}
function moveDownDept(id){
			var pageurl = "department_movedown.action";
			 $.post(pageurl,{id:id},function(msg){ 
			    	location.reload();
				 }); 
			return;
}
