<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
	<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
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
					pageUrl = "operation_list.action?ptype="+treeNode.nodeType+"&&pid="+treeNode.id;
					window.parent.frames["navOPeration_listFrame"].location.href =pageUrl;
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
</head>
<body >
	<table cellpadding="0" cellspacing="0" width="100%" height="100%">
		<tr>
			<td height="25">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="tools_nav">
							<img  src="iwork_img/process_file.gif" border="0" height="18">&nbsp;&nbsp;&nbsp;导航菜单&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:this.location.reload();"><img  src="iwork_img/refresh2.gif" border=0></a>
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
	</table>
</body>
</html>
