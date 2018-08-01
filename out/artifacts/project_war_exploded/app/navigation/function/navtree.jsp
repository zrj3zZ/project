<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script language="javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript">
		$(document).ready(function(){
            $("#nav_function_tree").tree({   
	          url: 'function_tree_Json.action',
	          onBeforeExpand:function(node){
	          	$('#nav_function_tree').tree('options').url = "function_tree_Json.action?nodeId=" + node.id+"&nodeType="+node.attributes.type;
	          },   
	          onClick:function(node){
	             if(node.attributes.type == ''){
	             	window.parent.frames[node.attributes.target].location.href = node.attributes.url;
	             }else if(node.attributes.type == 'fun'){
	             	parent.navlistFrame.edit(node.attributes.url);
	             }else{
	             	window.parent.frames[node.attributes.target].location.href = node.attributes.url;
	             	$(this).tree('toggle', node.target); 
	             }
	          }
           });
		});
	</script>
</head>
<body >
	<table cellpadding="0" cellspacing="0" class="jgtd1" height="100%">
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
										<ul id="nav_function_tree" style="padding-top:5px;"></ul>
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
