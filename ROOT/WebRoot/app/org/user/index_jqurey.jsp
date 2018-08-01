<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%
	String lookandfeel = "_def";
 %>
<html>
<head>
<title> </title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript">
		$(function(){
			$('#orguserTree').tree({
				animate:true, 
				
				url: 'jsonOrgUsertree.action',
				onClick:function(node){
					$(this).tree('options', node.target);
					
				},onBeforeExpand:function(node){
               	 	//alert(node.attributes.type);
                     $('#orguserTree').tree('options').url = "jsonOrgUsertree.action?pid=" + node.id;// change the url                       
                 }
			});
			$('#tt').tabs({
				tools:[{
					iconCls:'icon-add',
					handler: function(){
						alert('add');
					}
				},{
					iconCls:'icon-save',
					handler: function(){
						alert('save');
					}
				}]
			});
		});
	</script>
<style type="text/css">
body { font-family:Verdana; font-size:14px; margin:0;}
#container {margin:0 auto; width:100%;}
#sidebar { float:left; width:25%; height:500px; }
#content { float:right; width:75%; height:500px;}/*因为是固定宽度，采用左右浮动方法可有效避免ie 3像素bug*/
</style>
</head>
<body >
<s:form>
<div id="container">
  <div id="sidebar">
		<table cellpadding="0" cellspacing="0" class="jgtd1">
<tr>
<td height="25"><table width="90%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="jgleft">&nbsp;</td>
    <td class="jgbg"><img  src="iwork_img/man3.gif" border="0" height="18">&nbsp;&nbsp;&nbsp;用户管理组织结构&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:this.location.reload();"><img  src="iwork_img/refresh2.gif" border=0></a></td>
    <td class="jgright">&nbsp;&nbsp;</td>
  </tr>
</table></td>
  </tr>
	<tr>
	<td  valign="top" ><table cellpadding="0" cellspacing="0" class="jgtd2">
      <tr>
        <td valign="top">
		
		  <table width="100%" height="100%" border="0">
  <tr>
    <td valign="top" height="450">
	 <ul id="orguserTree" animate="true">
	 </ul> 
	</td>
	  </tr>
	</table>
			</td>
	      </tr>
	    </table></td>
		
	  </tr>
	</table>
  </div>
  <div id="content">
		<div id="tt" >
		<div title="用户列表" iconCls="icon-org-user" style="padding:5px;height:400px;">
			<table id="test" class="easyui-datagrid" fit="true">
				<thead>
					<tr>
					
					 	<th field="f1" width="47">ID</th>
				        <th field="f2" width="66" >用户ID</th>
				        <th field="f2" width="123"  >用户名称</th> 
				        <th field="f2" width="107"  >部门编号</th>
				        <th field="f2" width="121"  >部门名称</th>
				        <th field="f2" width="71"  >角色ID</th>
				        <th field="f2" width="99"  >工作状态</th>
				        <th field="f2" width="122"  >兼职信息</th>
				        <th field="f2" width="277"  >操作</th>
			
						
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>d1</td>
						<td>d2</td>
						<td>d3</td>
						<td>d1</td>
						<td>d2</td>
						<td>d3</td>
						<td>d1</td>
						<td>d2</td>
						<td>d3</td>
					</tr>
					<tr>
						<td>d11</td>
						<td>d21</td>
						<td>d31</td>
						<td>d11</td>
						<td>d21</td>
						<td>d31</td>
						<td>d11</td>
						<td>d21</td>
						<td>d31</td>
					</tr>
				</tbody>
			</table>
		
		</div>
	</div>

  </div>
</div>
</s:form>
</body>

</html> 