<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_showlist.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src ="iwork_js/engine/metadata_showlist.js"></script> 
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
	            <div class="none_item"><img src="iwork_img/metadata.gif" border="0"> 未找到存储模型</div>
	 </s:if> 
	 	<table width="100%" cellspacing="0" cellpadding="0" border="0" style="border:1px solid #999">
	 			<tr class="header">
							<td   width="5%"  nowrap="nowrap">存储类型<br></td>
							<td   width="20%">标题<br></td>
							<td   width="15%" >存储表名<br></td>
							<td   width="20%" >模型描述<br></td>
							<td   width="10%">管理员<br></td>
							<td  width="10%" >更新日期<br></td>
							<td  width="20%" >操作<br></td>
						</tr>
	 			
				<s:iterator  value="list" status="status">
					<tr  class="cell">
						<td   width="70"  nowrap="nowrap">
							<s:if test="entitytype==0||entitytype==null">
							存储模型
							</s:if>
							<s:elseif test="entitytype==1">
							视图模型
							</s:elseif>  
						</td>
							<td   width="100"><s:property value="entitytitle"/><br></td>
							<td   width="15%" ><s:property value="entityname"/><br></td>
							<td   width="15%" ><s:property value="descirption"/><br></td>
							<td   width="10%"><s:property value="uploader"/></td>
							<td  width="10%" ><s:property value="%{getText('{0,date,yyyy-MM-dd }',{updatadate})}"/><a></td>
							<td  width="30%" >
								<a href="###"  onclick="openBaseInfo('<s:property value="entitytitle"/>',<s:property value="id"/>);">属性设置</a>
								<a href="javascript:del(<s:property value="id"/>);">删除</a>
							</td>
						</tr>
			</s:iterator>
			</table>
			<s:form name="editForm" id="editForm">
			</s:form>
</body>
</html>
