<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="../iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="../iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="../iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="../iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
	<script type="text/javascript" src="../iwork_js/commons.js" charset="gb2312"></script>
	<link href="../iwork_css/public.css" rel="stylesheet" type="text/css" />
	
	<link rel="stylesheet" type="text/css" href="../iwork_css/engine/sysiformcomp.css">
</head>
<body class="easyui-layout">
<div region="center" style="padding:3px;border-top:0px;">
<s:form  id="baseInfoForm" name="baseInfoForm" action="sysEngineIFormMap_saveBaseInfo.action"  theme="simple">
	<div region="center" border="false" style="padding: 10px; background:#FEFDF5; border: 1px solid #ccc;">
		<table width="300" border="0" cellspacing="0" cellpadding="0">
			  <tr>
			    <td style="white-space:nowrap">字段名称:</td>
			    <td>
			    	
			  </tr>
			  <tr>
			    <td style="white-space:nowrap">标题:</td>
			    <td>
			    	<input style="width:160px;" type="text" name="iformStyleMap.fieldTitle" id ="iformStyleMap.fieldTitle"  required="true"></td>
			  </tr>
			  <tr>
			    <td style="white-space:nowrap">外观类型:</td>
			    <td>
					 <s:select label="外观类型" name="iformStyleMap.displayType" id="iformStyleMap_displayType" list="%{displayTypeList}" listKey="key" listValue="title"  headerKey="" headerValue="请选择"> 
					</s:select>
				</td>
			  </tr>
			  <tr>
			    <td style="white-space:nowrap">脚本扩展:</td>
			    <td>
			    	<textarea rows="4" cols="25" name="iformStyleMap.htmlInner"></textarea>
			    </td>
			  </tr>
			  <tr>
			    <td style="white-space:nowrap">录入高度/宽度:</td>
			    <td>
			    	<input style="width:60px;" type="text" name="iformStyleMap.inputHeight" id ="iformStyleMap.inputHeight" />&nbsp;/&nbsp;
			    	<input style="width:60px;" class="easyui-validatebox"  type="text" name="iformStyleMap.inputWidth" id ="iformStyleMap.inputWidth"  >
			    
			    </td>
			  </tr>
			   <tr>
			    <td style="white-space:nowrap">参考值:</td>
			    <td>
			    	<input style="width:260px;" type="text" name="iformStyleMap.displayEnum" id ="iformStyleMap.displayEnum" />
			    </td>
			  </tr> 
			   <tr>
			    <td style="white-space:nowrap">是否允许为空:</td>
			    <td>
			    	 <select id="iformStyleMap.fieldNotnull"  name="iformStyleMap.fieldNotnull" style="width:80px;" required="true">
										<option value="0">是</option>
										<option value="1">否</option>
								</select>
			    </td>
			  </tr>
			   
			</table>			            	
	</div>					
						 
</s:form>
    </div>
</body>
</html>
