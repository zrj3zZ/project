<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"></script> 
	<script type="text/javascript">
		function addItem(){
			if($("#model_paramType").val()==''){
				art.dialog.tips("请选择参数类型",2);
				return false;
			}
			if($("#model_paramName").val()==''){
				art.dialog.tips("请填写参数名称",2);
				return false;
			}
			if($("#model_paramVal").val()==''){
				art.dialog.tips("请填写参数值",2);
				return false;
			}
			if(length2($("#model_paramName").val())>100){
				art.dialog.tips("参数名称过长",2);
				return false;
			}
			if(length2($("#model_paramVal").val())>200){
				art.dialog.tips("参数值过长",2);
				return false;
			}
			if(length2($("#model_paramMemo").val())>500){
				art.dialog.tips("适用范围过长",2);
				return false;
			}
			$('form').attr('action','sysFlowDef_param!save.action');
			$("form").submit();
		}
		function deleteItem(id){
			art.dialog.confirm('你确定要删除流程参数吗？', function(){
			    $("#model_id").val(id);
               	$('form').attr('action','sysFlowDef_param!delete.action');
               	$("form").submit();
			}, function(){
			
			});
		}
	</script>
<style> 
		<!--
			#title { height:20px; background:#EFEFEF; border-bottom:1px solid #990000; font:12px; font-family:宋体; padding-left:5px; padding-top:5px;}
			#baseframe { margin:0px;background:#FFFFFF; border:1px solid #CCCCCC;}
			.toobar{
				 border-bottom:1px solid #990000; 
				 padding-bottom:5px; 
			}
			/*只读数据样式*/
			.readonly_data {
				vertical-align:bottom;
				font-size: 12px;
				line-height: 20px;
				color: #888888;
				padding-right:10px;
				border-bottom:1px #999999 dotted;
				font-family:"宋体";
				line-height:15px;
			}
			.table_form{
				font-family:"宋体";
				font-size: 12px;
			}
			/*数据字段标题样式*/
			.td_title {
			color:#004080;
				line-height: 23px;
				font-size: 12px;
				text-align: right;
				letter-spacing: 0.1em;
				padding-right:10px;
				white-space:nowrap;
				border-bottom:1px #999999 thick;
				vertical-align:middle;
				font-family:"宋体";
			}
			/*数据字段内容样式*/
			.td_data {
				color:#0000FF;
				line-height: 23px;
				text-align: left;
				padding-left: 3px;
				font-size: 12px;
				font-family:"宋体";
				border-bottom:1px #999999 dotted;
				vertical-align:middle;
				word-wrap:break-word;
				word-break:break-all;
				font-weight:500;
				line-height:15px;
				padding-top:5px;
				
			}
		-->
</style>
<body class="easyui-layout">
<div region="north" border="false" style="height:40px;">
		<div class="tools_nav">
			<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
			 <a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
	</div>
	<div region="center" style="padding:5px;border:0px;">
		<div >
				<div style="padding:0px;border:0px solid #ccc;">
				<fieldset>
			         <form name="frmMain" id="frmMain" action="sysFlowDef_param!save.action">
			              <table>
			              	<tr>
			               		<td>参数类型</td>
			               		<td><s:select name="model.paramType" list="#{0:'静态参数',1:'组织变量',2:'外部数据'}" listKey="key" listValue="value" theme="simple"></s:select>
			               		<span style="color:red">*</span></td>
			               	</tr>
			             	 <tr>
			              		<td>参数名称</td>
			               		<td><s:textfield name="model.paramName" theme="simple"></s:textfield><span style="color:red">*</span></td>
			               	</tr>
			               	<tr> 
			               		<td>参数值</td>
			               		<td><s:textfield name="model.paramVal" theme="simple"/><span style="color:red">*</span></td>
			               	</tr>
			               	<tr>
			               		<td>适用范围</td> 
			               		<td><s:textarea name = "model.paramMemo" theme="simple"/></td>
			               	</tr>
			              </table>
			              	<s:hidden name="model.prcDefId"></s:hidden>
							<s:hidden name="model.actDefId"></s:hidden>
							<s:hidden name="model.id"></s:hidden>
			         </form>
			        </fieldset>
				</div>
				
				<div style="padding:5px;border:0px solid #ccc;">
			               <table width="100%" border=1 style='border-color:#999'>
			               	<tr style="background:#FFFFEE;">
			               		<td width="40">序号</td>
			               		<td>参数类型</td>
			               		<td>参数名称</td>
			               		<td>参数值</td>
			               		<td>适用范围</td>
			               		<td>操作</td>
			               	</tr>
			               	<s:iterator  value="list" status="status">
			               		<tr> 
			               		<td><s:property value="#status.index+1"/></td>
			               		<td>
			               			<s:if test="paramType==0">
			               				静态参数
			               			</s:if>
			               			<s:elseif  test="paramType==1">
			               				组织变量
			               			</s:elseif>
			               			<s:elseif  test="paramType==2">
			               				外部数据
			               			</s:elseif>
			               		
			               		</td>
			               		<td><s:property value="paramName"/></td>
			               		<td><s:property value="paramVal"/></td>
			               		<td><s:property value="paramMemo"/></td>
			               		<td><a href='javascript:deleteItem(<s:property value="id"/>)'>删除</a></td>
			               	</tr>
			               	</s:iterator>
			               </table>
				</div>		
		</div>
	</div>
</body>
</html>
