<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysengineiformmap.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript" src="iwork_js/engine/iformmap_index.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
</head>
<body class="easyui-layout">
<div  region="north" border="false"><div  class="tools_nav"> 
					<table width="100%">
						<tr>
							<td>
								<a href="javascript:addFieldItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增虚拟域</a>
								<a href="javascript:removeFieldItem();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">移除表单域</a>
								<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
							</td>
							<td style="text-align:right">
								<a href="javascript:moveTop();" class="easyui-linkbutton" plain="true" iconCls="icon-top">置顶</a>
								<a href="javascript:moveUp();" class="easyui-linkbutton" plain="true" iconCls="icon-up">上移</a>
								<a href="javascript:moveDown();" class="easyui-linkbutton" plain="true" iconCls="icon-down">下移</a>
								<a href="javascript:moveBottom();" class="easyui-linkbutton" plain="true" iconCls="icon-bottom">置底</a>
							</td>
						</tr>
					</table>
						
					</div></div>
<div region="center" style="border-top:0px;border:1px solid #efefef;"> 
<s:form  id="baseInfoForm" name="baseInfoForm" action="sysEngineIFormMap_saveBaseInfo.action"  theme="simple">
						<table id="iformmap_grid"  style = "height:450px;border:1px solid #fff" iconCls="icon-edit" singleSelect="false"	idField="ID" url="sysEngineIFormMap!load.action?formid=<s:property value='formid'  escapeHtml='false'/>" >
									<thead> 
										<tr> 
											 <th field="CK" checkbox=true></th>
											<th field="ID" width="40">ID</th>
											<th field="MAP_TYPE" width="100" >类型</th>	
											<th field="FIELD_NAME" width="100" >域名称</th>	
											<th field="FIELD_TITLE" width="150" align="left" >域标题</th>
											<th field="DISPLAY_TYPE" width="100" align="left" >显示类型</th>
											<th field="DISPLAY_WIDTH" width="60" align="center" >显示宽度</th>
											<th field="INPUT_WIDTH" width="60" align="center" >录入宽度</th>
											<th field="INPUT_HEIGHT" width="60" align="center">录入高度</th>
											<th field="FIELD_NOTNULL" width="80" align="center">是否允许为空</th>
											<th field="DO" width="60" align="center" >操作</th> 
											<!-- <th field="ORDERBY" width="100" align="center" >排序</th> -->
										</tr>
									</thead>
						</table>
			</s:form> 
	</div>
    <s:hidden name="formid" value="%{formid}"></s:hidden> 
</body>
</html>
