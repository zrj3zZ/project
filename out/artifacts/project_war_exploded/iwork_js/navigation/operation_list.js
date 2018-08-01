	    function doSearch(){
			if(document.all.sysNavSystemid.value=="")
			{	
				alert("请输入查询关键字!");
			}else{
		//	alert(document.all.sysNavSystemid.value);
				window.location.href="operation_list.action?queryName=DIRECTORY_ID&&queryValue="+document.all.sysNavSystemid.value;
		 	}
		}
		function addOperation(){
			var ptype = $("#ptype").val();
			var pid = $("#pid").val();
			var pageUrl ="operation_add.action?ptype="+ptype+"&pid="+pid;
			 art.dialog.open(pageUrl,{
					id:'addressDialog', 
					title:"添加操作",
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:510,
				    close:function(){
				 		location.reload();
			 		}
			 });
			return;
		}
		function editOperation(id){
			var pageUrl ="operation_edit.action?id="+id;
			art.dialog.open(pageUrl,{
				id:'addressDialog', 
				title:"编辑操作",
				lock:true,
				background: '#999', // 背景色
				opacity: 0.87,	// 透明度
				width:500,
				height:510,
				close:function(){
				location.reload();
			}
			});
			return;
		}
		
		function deleteOperation(id){
			var pageUrl ="operation_delete.action";
			art.dialog.confirm('确定要删除吗？',function () {
				$.post(pageUrl,{id:id},function(msg){
			    	location.reload(true);
				 }); 
			},function () {
				});
			return;
		}
		