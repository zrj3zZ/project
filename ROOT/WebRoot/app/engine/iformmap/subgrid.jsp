<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysengineiformmap.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript" src="iwork_js/engine/iformmap_subgrid.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
</head>
<body class="easyui-layout">
<div region="center" style="padding:3px;border-top:0px;">
					<div id ="subTable"  title="子表" closable="false" style="padding:2px;"  cache="false" >
						<div style="border-bottom:1px solid #efefef;margin-bottom:3px;">
							<a href="javascript:addSubForm();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加子表</a>
							<a href="javascript:removeSubForm();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除子表</a>
							<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
						</div><a href="sysEngineSubform_listpage.action?formid=<s:property value="formid"/>">模型列表</a>
						<table id="subTable_grid"  style="height:400;"  iconCls="icon-edit" singleSelect="true"	idField="itemid" url="sysEngineSubform!load.action?formid=<s:property value='formid'  escapeHtml='false'/>" >
									<thead>
										<tr>
											<th field="ID" width="40">ID</th> 
											<th field="TITLE" width="120" >标题</th>
											<th field="KEY" width="70" >键值</th>		
											<th field="TYPE" width="60" align="left" >子表类型</th>
											<th field="METADATA_NAME" width="100" align="left" >存储表</th>
											<th field="METADATA_TITLE" width="120" >数据存储表</th>
											<th field="IS_RESIZE" width="100" align="center">自适应高度</th>
											<th field="DO" width="160" align="center" >操作</th>
										</tr>
									</thead>
						</table>
						
					</div>
		<!--子表添加窗口-->
    <div id="addSubForm_window" class="easyui-window" title="行项目子表维护" modal="true" closed="true" collapsible="false" minimizable="true"
        maximizable="false"  style="width:380px; height: 330px; padding: 5px;background: #fafafa;">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <s:form name="eidtSubForm" id="eidtSubForm"  action="sysEngineSubform!save.action"  theme="simple">
            <table width="100%" border="0" cellspacing="4" cellpadding="0">
			  <tr>
			    <td style="white-space:nowrap">关联子表:</td>
			    <td>
			    	<input  readonly onfocus="this.blur()" style="width:160px;background:#efefef;" type="text" name="subformTitle" id ="subformTitle"  required="true"></input><a href="javascript:openSubFormTree();"><img src='iwork_img/but_sp.gif' border='0'></a>
			    </td>
			  </tr>
			  <tr>
			    <td style="white-space:nowrap">键值:</td>
			    <td>
			    	<input class="easyui-validatebox" style="width:160px;"  type="text" name="subForm.key" id ="subForm_key"  required="false"><br/>
			    	<font color='#666'>注:非必填项，如不指定系统将自动生成</font>
			    	</td>
			  </tr>
			  <tr>
			    <td style="white-space:nowrap">子表标题:</td>
			    <td>
			    	<input class="easyui-validatebox" style="width:160px;"  type="text" name="subForm.title" id ="subForm_title"  required="true">
			  	</td>
			  </tr>
			  <tr>
			    <td style="white-space:nowrap">子表类型:</td>
			    <td>
				    	<select id="subForm_type"  name="subForm.type" style="width:80px;" required="true">
			           	    <option value="0">编辑子表</option>
							<option value="1">视窗子表</option>
						</select>
			    </td>
			  </tr>
			  <tr>
			    <td style="white-space:nowrap">是否自适应高度:</td>
			    <td style="white-space:nowrap">
			     <select id="subForm_isResize" onChange="isResizeChange();" name="subForm.isResize" style="width:80px;" required="true">
		           	    <option value="0">是</option>
						<option value="1">否</option>
					</select>
			    </td>
			  </tr>
			  <tr style="display:none" id="tr_height">
			    <td style="white-space:nowrap">高度:</td>
			    	<td>
			    		<input class="easyui-validatebox" style="width:60px;"  type="text" name="subForm.height" name="subForm_height" required="true"/>
			    	</td>
			  </tr>
			  <tr>
			    <td style="white-space:nowrap">是否自适应宽度:</td>
			    	<td style="white-space:nowrap">
			    		<select id="subForm_autoWidth" onChange="isAutoWidth()" name="subForm.autoWidth" style="width:80px;" required="true">
			           	    <option value="0">是</option>
							<option value="1">否</option>
						</select>
		    		</td>
			  </tr>
			  <tr style="display:none" id="tr_gridWidth">
			    <td style="white-space:nowrap">宽度:</td>
			    <td><input class="easyui-validatebox" style="width:60px;"  type="text" name="subForm.gridWidth" id="subForm_gridWidth" required="true"></input></td>
			  </tr>
			  <tr>
			    <td style="white-space:nowrap">锁定列:</td>
			    <td>
			    	  <select id="subForm_fixedrow"  name="subForm.fixedrow" style="width:80px;" required="true">
			    	  	<option value="">-空-</option>
		           	    <option value="1">一</option>
						<option value="2">二</option>
						<option value="3">三</option>
						<option value="4">四</option>
					</select>
					列
			    </td>
			  </tr>
			</table>	
                <s:hidden id = "subForm_id" name="subForm.id"></s:hidden>
                <input  type="hidden" id = "subForm_iformId" name="subForm.iformId" value="<s:property value='formid' escapeHtml='false'/>">
                <s:hidden   name="subForm.subformid" id="subForm.subformid"/>
               </s:form> 
            </div>
            <div region="south" border="false" style="text-align: right; height: 35px; line-height: 30px;padding-top:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:addSubFormSubmit();" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:addSubFormcancel()">取消</a>
            </div>
        </div>
        	<!-- 关联子表窗口 -->
	<div id="subformTreewindow" class="easyui-window" title="表单字典" modal="true" closed="true" collapsible="false" minimizable="true"
        maximizable="false"  style="width:380px;height:320px;padding:5px;left:500px;bottom:100px;background:#fafafa;">
        <div title="我的菜单" icon="icon-reload" closable="false" style="overflow:auto;padding:5px;">
						 <ul id="subformtree">
						 </ul> 
					</div>
    </div>
    </div>
    <s:hidden name="formid" value="%{formid}"></s:hidden> 
</body>
</html>
