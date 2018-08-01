<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src ="iwork_js/engine/form_showlist.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
		.header td{
			font-weight:bold;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:#fafafa url('../iwork_css/jquerycss/default/images/datagrid_header_bg.gif') repeat-x left bottom;
			border-bottom:1px dotted #ccc;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
		}
		.cell td{
					margin:0;
					padding:3px 4px;
					font-size:12px;
					overflow:hidden;
					text-align:left;
					border-bottom:1px dotted #eee;
					border-top:1px dotted #fff;
					border-right:1px dotted #ccc;
				}
	</style>     
</head>
<body>
	<s:if test="list==null || list.size()==0">
	            <div class="none_item"><img src="iwork_img/metadata.gif" border="0"> 未找到表单模型</div>
	 </s:if>
	 	<table width="100%" cellspacing="0" cellpadding="0" border="0" style="border:1px solid #999">
	 			<tr class="header">
							<td   width="5%"  nowrap="nowrap">表单类型<br></td>
							<td   width="20%">表单名称<br></td>
							<td   width="15%" >访问类型<br></td>
							<td   width="20%" >表单说明<br></td>
							<td   width="10%">管理员<br></td>
							<td  width="10%" >更新日期<br></td>
							<td  width="20%" >操作<br></td>
						</tr>
	 			
				<s:iterator  value="list" status="status"> 
					<tr  class="cell">
						<td   width="70"  nowrap="nowrap">
							<s:if test="metadataType==0||metadataType==null">
							数据表单
							</s:if>
							<s:elseif test="metadataType==1">
							流程表单
							</s:elseif>  
							<s:elseif test="metadataType==2">
							行项目表
							</s:elseif>  
						</td>
							<td   width="100"><s:property value="iformTitle"/><br></td>
							<td   width="15%" >
								<s:if test="visitType==0||visitType==null">
									登录验证
								</s:if>
								<s:elseif test="visitType==1">
									匿名访问
								</s:elseif>  
							</td> 
							<td   width="15%" ><s:property value="memo" escapeHtml="false"/><br></td> 
							<td   width="10%"><s:property value="master"/></td>
							<td  width="10%" ><s:property value="%{getText('{0,date,yyyy-MM-dd }',{updatedate})}"/><a></td>
							<td  width="30%" >
								<a  href="#"  onclick="openFormMap('<s:property value="iformTitle"/>',<s:property value="id"/>);" class="easyui-linkbutton" plain="true" iconCls="icon-sysbtn">域设置</a>
								<s:if test="metadataType!=2">
										<a href="javascript:void(0)" style="margin-left:1px;margin-right:1px" id="mb1" class="easyui-menubutton" menu="#backlist<s:property value="id"/>" iconCls="icon-sysbtn">表单布局</a>
					           		 		<div id="backlist<s:property value="id"/>"  style="width:150px;">
													<div  iconCls="icon-ifrom"  onclick="openFormDesigner('<s:property value="iformTitle"/>',<s:property value="id"/>,1);">通用表单</div>
													<div  iconCls="icon-purview-mobile"  onclick="openFormDesigner('<s:property value="iformTitle"/>',<s:property value="id"/>,2);">移动表单</div>
											</div>
								</s:if>
								<a  href="javascript:del(<s:property value="id"/>);" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
							</td>
						</tr>
			</s:iterator>
			</table>
			<s:form name="editForm" id="editForm">
			</s:form>
</body>
</html>
