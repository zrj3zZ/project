	//点击事件
			function setOrgComapny(event, treeId, treeNode, clickFlag){
				var url = "openUnPurviewDeptIndex.action?companyid="+treeNode.id+"&companyname="+encodeURI(treeNode.companyname)+"&companytype="+encodeURI(treeNode.companytype);
				window.location.href = url; 
			}
		
		
		var demoMsg = {
			async:"正在进行异步加载，请等一会儿再点击...",
			expandAllOver: "全部展开完毕",
			asyncAllOver: "后台异步加载完毕",
			asyncAll: "已经异步加载完毕，不再重新加载",
			expandAll: "已经异步加载完毕，使用 expandAll 方法"
		}
 		function beforeAsync() {
			curAsyncCount++;
		}
		function onAsyncSuccess(event, treeId, treeNode, msg) {
			curAsyncCount--;
			if (curStatus == "expand") {
				expandNodes(treeNode.children);
			} else if (curStatus == "async") {
				asyncNodes(treeNode.children);
			}
			if (curAsyncCount <= 0) {
				if (curStatus != "init" && curStatus != "") {
					$("#demoMsg").text((curStatus == "expand") ? demoMsg.expandAllOver : demoMsg.asyncAllOver);
					asyncForAll = true;
				}
				curStatus = "";
			}
		}
		function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
			curAsyncCount--;
			if (curAsyncCount <= 0) {
				curStatus = "";
				if (treeNode!=null) asyncForAll = true;
			}
		}
 		function onClick(event, treeId, treeNode, clickFlag){
 			var zTree = $.fn.zTree.getZTreeObj("purviewTree");
 			var type = treeNode.nodeType;
 				$("#deptId").val(treeNode.id);
	 			zTree.expandNode(treeNode, true, null, null, true);
	 			var url = "openUnDeptPurviewList.action?deptId="+treeNode.id;
				var setting2 = {
					check: {
						enable: true
					},
					view: { 
						selectedMulti: false
					},
					async: {
						enable: true, 
						url:url,
						dataType:"json"
					},callback: { 
						onClick:onPurviewClick
					}
				};
				$.fn.zTree.init($("#purviewListTree"), setting2); 
 		}
		var curStatus = "init", curAsyncCount = 0, asyncForAll = false,
		goAsync = false;
		//全部展开
		function expandAll() {
			if (!check()) {
				return;
			}
			var zTree = $.fn.zTree.getZTreeObj("purviewTree");
			if (asyncForAll) {
				$("#demoMsg").text(demoMsg.expandAll);
				zTree.expandAll(true);
			} else {
				expandNodes(zTree.getNodes());
				if (!goAsync) {
					$("#demoMsg").text(demoMsg.expandAll);
					curStatus = "";
				}
			}
		}
		//全部折叠
		function unExpandAll() {
			var zTree = $.fn.zTree.getZTreeObj("purviewTree");
			zTree.expandAll(false);
		}
		function expandNodes(nodes) {
			if (!nodes) return;
			curStatus = "expand";
			var zTree = $.fn.zTree.getZTreeObj("purviewTree");
			for (var i=0, l=nodes.length; i<l; i++) {
				zTree.expandNode(nodes[i], true, false, false);
				if (nodes[i].isParent && nodes[i].zAsync) {
					expandNodes(nodes[i].children);
				} else {
					goAsync = true;
				}
			}
		}
		function asyncAll() {
			if (!check()) {
				return;
			}
			var zTree = $.fn.zTree.getZTreeObj("purviewTree");
			if (asyncForAll) {
				$("#demoMsg").text(demoMsg.asyncAll);
			} else {
				asyncNodes(zTree.getNodes());
				if (!goAsync) {
					$("#demoMsg").text(demoMsg.asyncAll);
					curStatus = "";
				}
			}
		}
		function asyncNodes(nodes) {
			if (!nodes) return;
			curStatus = "async";
			var zTree = $.fn.zTree.getZTreeObj("purviewTree");
			for (var i=0, l=nodes.length; i<l; i++) {
				if (nodes[i].isParent && nodes[i].zAsync) {
					asyncNodes(nodes[i].children);
				} else {
					goAsync = true;
					zTree.reAsyncChildNodes(nodes[i], "refresh", true);
				}
			}
		}
		function check() {
			if (curAsyncCount > 0) {
				$("#demoMsg").text(demoMsg.async);
				return false;
			}
			return true; 
		}
		
		//点击权限组树
		function onPurviewClick(event, treeId, treeNode,clickFlag) {
		var zTree = $.fn.zTree.getZTreeObj("purviewListTree");
			var type = treeNode.type;
			if(type=='category'){
				zTree.expandNode(treeNode, true, null, null, true);
			}else{
				if(!treeNode.checked){
	 				zTree.checkNode(treeNode, true, true, clickFlag);
		 		}else{
	 				zTree.checkNode(treeNode, false, true, false);
		 		}
			}
		}
				//设置权限
		function setpurview(){
					if($("#deptId").val()==""){
		 				alert('请选择您要授权的对象');
		 				return;
		 			}
					var zTree = $.fn.zTree.getZTreeObj("purviewListTree");
		 			var nodes = zTree.getCheckedNodes(true);
		 			var str = ""; 
		 			for(var i=0;i<nodes.length;i++){
		 				var type = nodes[i].type;
		 				if(type=='category')continue;
		 				var tmp = nodes[i].id;
		 				if(i<nodes.length-1){ 
		 					tmp+=","; 
		 				}
		 				str+=tmp;
		 			}
		 			
		 			$("#purviewIds").val(str);
					$.post('openPurviewDept_setDeptlist.action',$("#editForm").serialize(),function(data){
				    	if(data=='success'){
				    		alert("授权成功");
				    	}else{
				    		alert("授权异常,请稍后再试");
				    	}
				  }); 
		}
		
		function showMenu() {
				var cityObj = $("#citySel"); 
				var cityOffset = $("#citySel").offset();
				$("#menuContent").css({left:+ "10px", top:cityOffset.top-20  + "px"}).slideDown("fast");
				$("body").bind("mousedown", onBodyDown);
				return false; 
		} 
		function hideMenu() {
			$("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		} 
		function onBodyDown(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		}