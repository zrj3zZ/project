<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>IWORK综合应用管理系统</title> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/mdm_index.js" ></script> 	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>		
	<script type="text/javascript">  
 //==========================装载快捷键===============================//快捷键
 		var status = 1; 
	    jQuery(document).bind('keydown',function (evt){		
			    if(evt.ctrlKey&&evt.shiftKey){
				return false;
			   }
			   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
				         this.location.reload(); return false;
			     } 
			  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增存储
						 addMetaData(); return false;
					}
			  else if(evt.ctrlKey && event.keyCode==83){ //ctrl+s保存
						doSubmit(); return false;
					}		
		}); //快捷键
		$(function(){ 
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
					  	var url = encodeURI("sysEngineMetadata_searchList.action?searchkey?searchkey="+searchTxt+"&showtype="+status);
						$('form').attr('action',url);
						$('form').attr('target',"metadataFrame");
						$('form').submit(); 
					  }
			  }
	</script>
	<style type="text/css">
		* {
			margin:0px;
			padding:0px;
			font-size:12px;
		}
		img {
			border: 0 none;
		}
		.tools_nav {
			background:url(iwork_img/engine/tools_nav_bg.jpg) repeat-x;
			height:34px;
			line-height:34px;
			padding-left:20px;
		}
		.search_btn {
			background:url(iwork_img/engine/search_btn.png) no-repeat;
			height:26px;
			width:189px;
			cursor:pointer; 
			white-space:nowrap;
			margin-right:10px;
			float:right;
			margin-top:3px;
		}
		.search_btn_onclick {
			background:url(iwork_img/engine/search_btn_onclick.png) no-repeat;
			height:26px;
			width:189px;
			cursor:pointer;
			margin-right:10px;
			float:right;
			margin-top:3px; 
		}
		.search_input {
			background:none;
			border:0 none;
			cursor:pointer;
			width:140px;
			height:20px;
			line-height:20px;
			margin-left:5px;
			margin-top:2px;
		}
		
		.search_button {
			background:none;
			border:0 none;
			width:40px;
			height:20px;
			cursor:pointer
		}
		.view {
			background:url(iwork_img/engine/view1.png);
			display:block;
			height:26px;
			width:61px;
			margin-right:10px;
			margin-top:3px;
			float:right;
			cursor:pointer
		}
		.view2 {
			background:url(iwork_img/engine/view2.png);
			display:block;
			height:26px;
			width:61px;
			margin-right:10px;
			margin-top:3px;
			float:right;
			cursor:pointer
		}
		.button1 {
			background:url(iwork_img/engine/button1_1.png) no-repeat;
			width:70px;
			height:27px;
			cursor:pointer;
			display:block;
			float:right;
			line-height:27px;
			margin-right:5px;
			padding-left:30px;
		}
		.button1_hover {
			background:url(iwork_img/engine/button1_2.png) no-repeat;
			width:70px;
			height:27px;
			cursor:pointer;
			display:block;
			float:right;
			line-height:27px;
			margin-right:5px;
			padding-left:30px;
		}
		.button1_click {
			background:url(iwork_img/engine/button1_3.png) no-repeat;
			width:70px;
			height:27px;
			cursor:pointer;
			display:block;
			float:right;
			line-height:27px;
			margin-right:5px;
			padding-left:30px;
		}
		.button2 {
			background:url(iwork_img/engine/button2_1.png) no-repeat;
			width:70px;
			height:27px;
			cursor:pointer;
			display:block;
			float:right;
			line-height:27px;
			margin-right:5px;
			padding-left:30px;
		}
		.button2_hover {
			background:url(iwork_img/engine/button2_2.png) no-repeat;
			width:70px;
			height:27px;
			cursor:pointer;
			display:block;
			float:right;
			line-height:27px;
			margin-right:5px;
			padding-left:30px;
		}
		.button2_click {
			background:url(iwork_img/engine/button2_3.png) no-repeat;
			width:70px;
			height:27px;
			cursor:pointer;
			display:block;
			float:right;
			line-height:27px;
			margin-right:5px;
			padding-left:30px;
		}
		.del {
			background:url(iwork_img/engine/bin_del.png) no-repeat;
			width:16px;
			height:16px;
			cursor:pointer;
			display:block;
			float:right;
			margin-right:5px;
			margin-top:15px;
			margin-bottom:5px;
		}
		.data {
			width:80px;
			height:16px;
			cursor:pointer;
			display:block;
			float:right;
			margin-right:5px;
			margin-top:15px;
			margin-bottom:5px;
		}
		/*树形结构样式*/
		.tree_left {
			height:500px;
			float:left;
			position:absolute;
			left:5px;
			top:36px;
			width:227px;
			color:#5c5c5c;
		}
		.tree_top {
			background:url(iwork_img/engine/tree_top_bg.jpg) no-repeat;
			height:15px;
			line-height:26px;
			padding-left:10px;
			padding-top:0px;
			font-weight:bold;
			width:217px;
		}
		.tree_top_icon {
			background:url(iwork_img/engine/tree_title.png) no-repeat;
			display:block;
			height:16px;
			padding-left:20px;
		}
		.tree_main {
			
			padding:0px 0px 0px 0px;
			text-align:left;
			vertical-align:top;
			background:url(iwork_img/engine/tree_bg.jpg);
		}
		.tree_bottom {
			background:url(iwork_img/engine/tree_bottom_bg.jpg);
			height:14px;
		}
		/*存储管理样式*/
		.right {
			margin-top:3px;
			margin-left:235px;
			color:#5c5c5c;
		}
		.right_frame {
		}
		.right_left {
			background:url(iwork_img/engine/right_left.jpg) no-repeat;
			width:7px;
			height:136px;
			float:left;
		}
		.right_center {
			background:url(iwork_img/engine/right_center.jpg);
			height:136px;
			width:100%;
		}
		.right_right {
			background:url(iwork_img/engine/right_right.jpg) no-repeat;
			width:8px;
			height:136px;
		}
		.right_left_click {
			background:url(iwork_img/engine/right_left_click.jpg) no-repeat;
			width:7px;
			height:136px;
			float:left;
			color:#0381ce;
		}
		.right_center_click {
			background:url(iwork_img/engine/right_center_click.jpg) repeat-x;
			height:136px;
			color:#0381ce;
		}
		.right_right_click {
			background:url(iwork_img/engine/right_right_click.jpg) no-repeat;
			width:8px;
			height:136px;
		}
		
	</style>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no;scrolling:no;border-bottom:1px;">
		<!-- <div class="tools_nav" style="padding-top:3px;">
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		 </div> -->
	</div>
    <!-- 导航区 -->
	<div region="west"  split="false" border="false"  style="height:100%;width:230px;padding-left:5px;padding-top:5px;overflow:hidden; border-top:1px solid #F9FAFD">
				<table style="height:100%;" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="tree_top"></td>
					</tr>
					<tr>
						<td class="tree_main">
							<div style="height:430px;width:210px;position:absolute; overflow-y:auto">
								<ul id="dem_tree" class="ztree"></ul>
							</div>
						</td>
					</tr>
					<tr>
						<td class="tree_bottom"></td>
					</tr>
				</table>
						 <form id="editForm" name="editForm"  action="/processDeploy_index.action" method="post">
						 		<input type="hiden" id="searchkey" name="searchkey">
						 		<input type="hiden" id="list_showtype" name="list_showtype">
						 </form>
    </div>
	<div region="center" style="padding:0px;border:0px;">
			<iframe id="demListFrame" src=""  frameborder="no" border="0" marginwidth="0″ marginheight="0″ scrolling="auto" allowtransparency="yes"  id="metadataFrame" name="demListFrame" width="100%" height="100%"></iframe>
			<!-- <table id="metadata_grid" style="margin:2px;"></table> -->
	</div>
	 
</body>
</html>
