		function confirm1(id){
			 art.dialog.confirm('确认删除?', function(r){
				if (r){										
					location.href="role_delete.action?id="+id;
					return false;
				}
			});
		}

		 jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     } 
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增操作
					 this.location.href='role_add.action'; return false;
				}
}); //快捷键

	function addRole(){
		var groupid = $("#groupId").val();
		  var pageUrl = "role_add.action";	 
		if(groupid != ''){
			pageUrl = "role_add.action?groupId="+groupid;
		}
		  art.dialog.open(pageUrl,{
			  id:'dg_addInfo',  
		        title:"新建角色",
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:600,
			    height:410,
		        close:function(){
       		 		window.location.reload();
		  		}
			 });
  }
	function editRole(id){
		if(id == ''){
			alert("请选择要编辑的角色");
			return ;
		}
	      var  pageUrl = "role_edit.action?id="+id;
	      art.dialog.open(pageUrl,{
			  id:'dg_editInfo',  
			  title:'编辑角色',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:600,
			    height:410,
		        close:function(){
       		 		window.location.reload();
		  		}
			 }); 
  }
	function delgroup(){
		art.dialog.confirm('确认删除?', function(r){
			if (r){	
					var groupId = $("#groupId").val();
					var url = "role_delgroup.action";
					$.post(url,{groupId:groupId},function(data){
				         if(data=='success'){
				              art.dialog.tips("分组移除成功");
				             window.location.reload();
				         }else if(data=='ishavingsub'){
				           art.dialog.tips("分组中包含角色，无法进行删除，请将角色转移后，执行移除操作");
				         }else{
				         	 art.dialog.tips("移除异常！");
				         }
				    });
			}
		});
	}
	function editgroup(){
		var groupid = $("#groupId").val();
		if(groupid == ''){
			 art.dialog.tips("请选择要编辑的分组类型");
			return ;
		}
	      var pageUrl = "role_editgroup.action?groupId="+groupid;
	      art.dialog.open(pageUrl,{
			  id:'dg_editInfo',  
			  title:'编辑角色组',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:600,
			    height:410,
		        close:function(){
       		 		window.location.reload();
		  		}
			 }); 
  }
	function addgroup(){
		var pageUrl = 'role_addgroup.action';
		 art.dialog.open(pageUrl,{
			  id:'dg_editInfo',  
			  title:'新增角色组',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:600,
			    height:410,
		        close:function(){
      		 		window.location.reload();
		  		}
			 }); 
  }