<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<style type="text/css" >
		.process_font1 {
			float:left;
			margin:0px 10px 0px 15px;
			height:30px;
			line-height:30px;
		}
		.process_header {
			background:url(../iwork_img/desk/process_nav_bg.png) repeat-x;
			line-height:32px;
			height:32px;
			clear:both;
			width:100%;
			border-bottom:1px solid #ccc;
		}
		.process_head_tab_box {
			float:left
		}
		.process_head_tab_right {
			right: 80px;
			top: -4px;
			position: absolute;
			width: 200px;
			height: 26px;
		}
		a.process_head_tab:hover{
			height:32px;
			line-height:32px;
			display:inline-block;
			padding:0px 20px;
			text-transform: uppercase;
			color: #767676;
			cursor: pointer;
			border-right:1px solid #dbdbdb;
			border-bottom:2px solid #fa7a20;
			font-weight:bold;
			margin-left:-3px;
			font-family:'microsoft yahei';
			font-weight:bold
		}
		a.process_head_tab_active {
			height:32px;
			line-height:32px;
			display:inline-block;
			padding:0px 20px;
			text-transform: uppercase;
			color: #fa7a20;
			cursor: pointer;
			border-right:1px solid #dbdbdb;
			border-bottom:2px solid #fa7a20;
			font-weight:bold;
			margin-left:-6px;
			font-family:'microsoft yahei';
			font-weight:bold
		}
		a.process_head_tab{
			height:32px;
			line-height:32px;
			display:inline-block;
			padding:0px 20px;
			text-transform: uppercase;
			color: #767676;
			cursor: pointer;
			border-right:1px solid #dbdbdb;
			border-bottom:0px solid #dbdbdb;
			margin-left:-3px;
			font-family:'microsoft yahei';
			font-weight:bold;
		}
		.item_list{
			text-align:center;
		}
		.flowbox {
		   cursor:pointer;
			width:120px; 
			height:100px;
			background:#fcfcfc; 
			float:left;
			display:inline-block;
			margin:13px;
			position:relative;
			border:1px solid #ccc; 
			color:#333;
			padding:10px;
			font-family:微软雅黑;
		} 
		
		.flowbox_select {
			width:120px; 
			height:100px;
			cursor:pointer;
			float:left;
			display:inline-block;
			margin:13px;
			position:relative;
			border:1px solid #ccc;
			color:#333;
			font-weight:bold;
			padding:10px;
			font-family:微软雅黑;
			background:transparent url(iwork_img/accept.png) no-repeat scroll 100% 100%;
			background-color:#ffffcc; 
		}
		.flowbox_select:hover {
			float:left;
			border:1px solid #FF9900;
			color:#FF9900;
			font-weight:bold;
		}
		.flowbox:hover {
			float:left;
			border:1px solid #FF9900;
			color:#FF9900;
			font-weight:bold;
			background:#fff; 
		}
		.subTitle{
			text-align:center;
			padding:10px;
			font-size:14px;
			font-family:微软雅黑;
			font-weight:bold;
			border-bottom:1px solid #ccc;
			background:#F5F5F5;
		}
		.selectList{
			text-align:left;
			padding:10px;
			font-size:14px;
			font-family:微软雅黑;
			font-weight:bold;
		}
	</style>
	<script type="text/javascript">
		//加载导航树  
		var arr = new Array();
		$(function(){
			initTree();
		});
		function initTree(){
			//加载组织树 
			var setting1 = {
				async: {
					enable: true, 
					url:"company_tree_json.action",
					dataType:"json",
					autoParam:["id","nodeType"] 
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					onClick:setOrgComapny
				} 
			};
			
			
			//加载部门数
			var setting2 = {
					check: {
						enable: true
					},
					view: { 
						selectedMulti: false
					},
					async: {
						enable: true,
						url:"multibook_orgjson.action?companyId=<s:property value='companyId' escapeHtml='false'/>",
						dataType:"json",
						autoParam:["id","nodeType"]
					},callback: { 
						onClick:onClick
					}
				};
				$.fn.zTree.init($("#grouptree"), setting1);
			$.fn.zTree.init($("#orgtree"), setting2);
			//	
		}
		function setOrgComapny(event, treeId, treeNode, clickFlag){
				var url = "system_config_mgrIndex.action?companyId="+treeNode.id;
				window.location.href = url;
		}
		//点击事件
		function onClick(event, treeId, treeNode, clickFlag){
			var zTree = $.fn.zTree.getZTreeObj("orgtree"); 
					if(treeNode.isParent){ 
						if(treeNode.open){
							zTree.expandNode(treeNode, false, null, null, true);
						}else{ 
							zTree.expandNode(treeNode, true, null, null, true);
						}
					}else{
						if(!treeNode.checked){
	 						zTree.checkNode(treeNode, true, true, clickFlag);
		 				}else{
	 						zTree.checkNode(treeNode, false, true, false);
		 				}
					}
		}
		
		function setUserList(){
			var zTree = $.fn.zTree.getZTreeObj("orgtree"); 
		 			var nodes = zTree.getCheckedNodes(true);
		 			var arr = new Array();
		 			var arr_name = new Array();
		 			var str = ""; 
		 			for(var i=0;i<nodes.length;i++){
		 				var type = nodes[i].nodeType;
		 				if(type!='user')continue;
		 				var tmp = nodes[i].id;
		 					arr.push(tmp);
		 					arr_name.push(nodes[i].name);
		 			}
		 			var userids = arr.join(",");
		 			var key =  $("#lookandfeelKey").val();
		 			
		 			if(key==''){
		 				alert('请选择外观样式');
		 				return;
		 			}
		 			if(arr_name.length>0){
		 				if(confirm('确认给['+arr_name.join(",")+']设置新的外观样式吗？')){
		 					$("#purviewIds").val(str);
								$.post('system_config_styleset.action',{userids:userids,key:key},function(data){
				    				if(data=='success'){
				    					alert("授权成功");
				    				}else{
				    					alert("授权异常,请稍后再试");
				    				}
				    				});
		 				}
		 			}else{
		 				alert('请选择要设置的用户');
		 			}
		 			
		 			/*
		 			*/

		}

		//全部展开
	    function expandAll() {
			var zTree = $.fn.zTree.getZTreeObj("orgtree");
				zTree.expandAll(true); 
		}

	  	//全部折叠
		function unExpandAll() {
			var zTree = $.fn.zTree.getZTreeObj("orgtree");
			zTree.expandAll(false);
		}
		
		function setLookAndFeel(obj,val){
			$("#skinLayoutSet").val(val);
			var list = $(".item_list").find(".flowbox_select");
			 for(var i=0;i<list.length;i++){
			 	var item = list.get(i);
			 	$(item).attr("class","flowbox");
			 }
			 $(obj).attr("class","flowbox_select");
			 $("#lookandfeelKey").val(val);
		}
		
		function showMenu() {
				var cityObj = $("#citySel"); 
				var cityOffset = $("#citySel").offset();
				$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top-20  + "px"}).slideDown("fast");
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
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 --> 
	<div region="north" border="false" style="height:40px;border:1px solid #efefef;">
	<div class="process_header">
  <div class="process_head_tab_box"> 
 	 <a class="process_head_tab_active" title="用户主题及样式初始化" href="system_config_mgrIndex.action">用户主题及样式初始化 </a>
     <a class="process_head_tab"  title="用户自定义桌面初始化"  href="##">用户自定义桌面初始化</a>  
      </div>
      <div style="padding-top:3px;">
      </div> 
  </div>
</div>
 <div region="west" style="width:200px;backrgound:#efefef;border:1px solid #efefef;"> 
 	<div class="item_list">
 		<div class="subTitle"><img src="iwork_img/010538151.gif" border="0"/>主题及皮肤样式</div>
 					<s:iterator value="lookandfeel" status='st'>
							<div class="flowbox" onclick="setLookAndFeel(this,'<s:property value="key"/>')"  >
								<div  class="purview"><img style="width:100px;" src="iwork_img/nopic1.gif" onerror="this.src='iwork_img/nopic1.gif'"/></div>
								<div  class="feelTitle"><s:property value="title"/></div>
							</div>
					</s:iterator>
		</div>
 </div>
	<div region="center" style="border:0px;text-align:center;text-align:left"> 
	<div class="tools_nav">
		<a  href="#" style="margin-left:1px;margin-right:1px"  onclick='setUserList()' text='Ctrl+s' class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
	</div>
			<div class="selectList">
				 <div style="padding:2px; text-align:left;background:#E7E7E7;border:1px solid #fff;">
				 	<input id="citySel" type="text"  onclick="setSearchTxt(this)" value="" style="width:200px;background:#F5F5F5;padding-left:5px;color:#666;border:1px solid #fff"/>
				&nbsp;<a id="menuBtn" href="#" onclick="showMenu(); return false;">选择</a></li>
				 </div>
				 <div id="menuContent" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef; position: absolute;"> 
										<ul id="grouptree" class="ztree" style="margin-top:0;background:#fafafa;border:1px solid #FFCC00"></ul> 
				 </div> 
						
			</div>
			<div>
				 <div>
					 <ul id="orgtree" class="ztree"></ul>
				 </div>
						
			</div>
			<s:hidden  id="selectUserList" name="selectUserList"/>
			<s:hidden  id="lookandfeelKey" name="lookandfeelKey"/>
	</div>
</body> 
</html>
<script  type="text/javascript" >
	expandAll(); 
</script>
