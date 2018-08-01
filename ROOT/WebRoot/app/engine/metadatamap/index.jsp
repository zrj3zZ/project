<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link href="iwork_css/engine/sysenginemetadatamap.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>   
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/engine/metadatamap_index.js" ></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	var mainFormValidator;
		$(function(){
			mainFormValidator =  $("#editForm").validate({
				debug:true
			 });
			 mainFormValidator.resetForm();	
			 $("#fieldtitle").focus();
		});
		//响应回车
	function enterKey(){
		if (window.event.keyCode==13){
			addItem();
			return;
		}
	} 
		function showUpfile(){
			var pageUrl="sysEngineMetadataMap_Excel_impPage.action?metadataid="+$("#metadataid").val();
			var dialogId = "showUpfile"; 
			var title = "批量添加字段";
			art.dialog.open(pageUrl,{
			    	id:dialogId, 
			    	title:title,  
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:300,
				    height:250,
					close:function(){
					   location.reload();
					}
				 });
		}
		
		function downfile(){
			alert('??');
		}
	</script>
</head>
<body class="easyui-layout" onKeyDown="enterKey()">
	<div region="north" style="border:0px;height:38px;">
		<div class="tools_nav">
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				<a href="javascript:delDataMap();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
				<span style="float:right">
				<a href="javascript:showUpfile();" class="easyui-linkbutton" plain="true" iconCls="icon-excel">批量导入</a>
				<a href="template/metadatamap/template.xls"  target="_blank" class="easyui-linkbutton" plain="true" iconCls="icon-excel">获取模版</a>
				</span>
			</div>
	</div>
	<div region="center" style="padding:0px;border:0px;" >
			<s:form name="editForm" id="editForm" action="sysEngineMetadataMap_save.action" theme="simple">
				<table  width="100%"  cellspacing="0" cellpadding="0" style="border:1px solid #efefef">
					<thead>
						<tr class="header">
							<td   width="40" nowrap="nowrap" style="text-align:center"> <input type="checkbox"  name="all" class="selectCheck" onclick="check_all(this,'id')"/></td>
							<!-- <td   width="70"  nowrap="nowrap">字段类型<br></td> -->
							<td   width="15%" >字段标题<br></td>
							<td   width="15%">字段名称<br></td>
							<td   width="15%" >字段类型<br></td>
							<td   width="15%" >长度<br></td>
							<td   width="10%">是否允许为空<br></td>
							<td  width="15%" >默认值<br></td>
							<td  width="15%" >操作<br></td>
						</tr>
						</thead>
						<tbody>
						<s:iterator value="availableItems" status="status">
							<tr class="cell">
								<s:if test="fieldname==\"ID\"">
								<td  style="text-align:right"> <s:property value="#status.count"/>&nbsp;<input class="selectCheck" disabled="disabled" title="序号字段不允许删除"  type="checkbox" name="id" value="<s:property value="id"/>"/></td>
								<td><s:property value="fieldtitle"/></td>
								<td><s:property value="fieldname"/></td>
								</s:if>
								<s:else>
									<td  style="text-align:right"> <s:property value="#status.count"/>&nbsp;<input class="selectCheck"  type="checkbox" name="id" value="<s:property value="id"/>"/></td>
									<td><a href="javascript:modifyBaseInfo('<s:property value="id"/>')"><s:property value="fieldtitle"/></a></td>
								<td><a href="javascript:modifyBaseInfo('<s:property value="id"/>')"><s:property value="fieldname"/></a></td>
								</s:else>
								<!--
								<s:if test="maptype==0">
									<td>实体字段</td>
								</s:if>
								<s:else>
									<td>虚拟字段</td>
								</s:else>-->
								
								<td><s:property value="fieldtype"/></td>
								<td><s:property value="fieldLength"/></td>
								<s:if test="fieldnotnull==0">
								<td>是</td>
								</s:if>
								<s:else>
									<td>否</td>
								</s:else>	
								<td><s:property value="fielddefault"/></td>		
								<s:if test="fieldname==\"ID\"">
									<td>&nbsp;</td>
								</s:if>
								<s:else>
									<td><a href="javascript:modifyBaseInfo('<s:property value="id"/>')">[属性]</a></td>
								</s:else>					
								
							</tr>
						 </s:iterator>
				<tr class="cell">
						<td></td>
							<td ><input class="{maxlength:32,required:true}" onblur="autoCreateKey();" onKeyDown="enterKey()" style="width:100px;" type="text" name="model.fieldtitle" id ="fieldtitle" ></td>
							<td ><input class="{maxlength:32,required:true}" onKeyDown="enterKey()"  style="width:80px" type="text" name="model.fieldname" id ="fieldname" ></td>
							<td>
								<select id="fieldtype" onChange="selectType(this);" name="model.fieldtype" style="width:80px;" required="true">
									<option value="字符">字符</option>
									<option value="数值">数值</option>
									<option value="日期">日期</option>
									<option value="日期时间">日期时间</option>
									<option value="文本">文本</option>
								</select> 
							</td>
							<td><input style="width:60px;"  class="{required:true}" type="text" name="model.fieldLength" id ="fieldLength" ></td>
							<td>
								<select id="model.fieldnotnull"  class="{required:true}"  name="model.fieldnotnull" style="width:80px;" >
										<option value="0">是</option>
										<option value="1">否</option>
								</select>
							</td>
							<td   ><input  type="text" name="model.fielddefault" id ="model.fielddefault"  ></td>
							<td colspan="6">
								<a id="btnEp" class="easyui-linkbutton" icon="icon-save" href="javascript:addItem();" >保存</a>
							</td>
						</tr>
						<tr>
							
						</tr>
			</table>
			<s:hidden name="metadataid" id="metadataid"></s:hidden>
			</s:form>
	</div>
</body>
</html>
<script type="text/javascript">
	$("#fieldtitle").focus();
</script>
