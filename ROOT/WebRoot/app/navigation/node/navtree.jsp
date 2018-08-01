<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script language="javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript">
		$(document).ready(function(){
			var setting = {
					edit: {
						enable: true,
						drag: {
							isMove: true,
							next:true,
							inner:true,
							prev:true
						}
					},
					async: {
						enable: true, 
						url:"sysNode_tree_Json.action",
						dataType:"json",
						autoParam:["id","nodeType"]
					},
					callback: {
						onClick:onClick,
						beforeDrag: beforeDrag,
						beforeDrop: beforeDrop
						
					}
				};
			$.fn.zTree.init($("#nav_function_tree"), setting);
			//点击事件
			function onClick(event, treeId, treeNode, clickFlag){
					var pageUrl = ""; 
					var zTree = $.fn.zTree.getZTreeObj("nav_function_tree");
					zTree.expandNode(treeNode, true, null, null, true);
					var id = treeNode.id;  
					if(id!=0){
						if(treeNode.nodeType=='SYS'){
							pageUrl = "sysNode_list.action?systemId="+treeNode.id;
						}else{
							pageUrl = "sysNode_list.action?parentNodeId="+treeNode.id;
						}
					}else{
						pageUrl = "sysindex.action";
					}
					window.parent.frames["navlistFrame"].location.href =pageUrl;
			}
			function beforeDrag(treeId, treeNodes) {
				for (var i=0,l=treeNodes.length; i<l; i++) {
					if (treeNodes[i].drag === false) {
						return false;
					}
				}
				return true;
			}
			function beforeDrop(treeId, treeNodes, targetNode, moveType) {
				return targetNode ? targetNode.drop !== false : true;
			}
		/*  
            $("#nav_function_tree").tree({   
	          url: 'sysNode_tree_Json.action',
	          onBeforeExpand:function(node){
	          	$('#nav_function_tree').tree('options').url = "sysNode_tree_Json.action?nodeId=" + node.id+"&nodeType="+node.attributes.type;
	          },   
	          onClick:function(node){
	             	window.parent.frames[node.attributes.target].location.href = node.attributes.url;
	          }
           });*/
		});
	</script>
	<style type="text/css">
		.tree_top {
			background:url(../../iwork_img/engine/tree_top_bg.jpg) no-repeat;
			height:15px;
			line-height:26px;
			padding-left:10px;
			padding-top:0px;
			font-weight:bold;
			width:217px;
		}
		.tree_top_icon {
			background:url(../../iwork_img/engine/tree_title.png) no-repeat;
			display:block;
			height:16px;
			padding-left:20px;
		}
		.tree_main {
			
			padding:0px 0px 0px 0px;
			text-align:left;
			vertical-align:top;
			background:url(../../iwork_img/engine/tree_bg.jpg);
		}
		.tree_bottom {
			background:url(../../iwork_img/engine/tree_bottom_bg.jpg);
			height:14px;
		}
		.topTitle{
			font-size:16px;
		}
	</style>
</head>
<body >
<div class="tools_nav">
<div class="topTitle">菜单导航</div>
</div>
<table style="height:80%;" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="tree_main">
							<ul id="nav_function_tree" class="ztree"></ul>
						</td>
					</tr>
					<tr>
						<td class="tree_bottom"></td>
					</tr>
				</table>
	<!-- <table cellpadding="0" cellspacing="0" class="jgtd1" height="100%">
		<tr>
			<td height="25">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="jgleft">
							&nbsp;
						</td>
						<td class="jgbg">
							<img  src="iwork_img/process_file.gif" border="0" height="18">&nbsp;&nbsp;&nbsp;导航菜单&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:this.location.reload();"><img  src="iwork_img/refresh2.gif" border=0></a>
						</td>
						<td class="jgright">
							&nbsp;&nbsp;
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td valign="top">
				<table cellpadding="0" cellspacing="0" class="jgtd2">
					<tr>
						<td valign="top">
							<table width="100%" height="100%" border="0">
								<tr>
									<td valign="top">
										<ul id="nav_function_tree" class="ztree"></ul>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table> -->
</body>
</html>
