<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>操作日志</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
	<script language="javascript" src="iwork_js/commons.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="iwork_js/system/sys_operation_log.js"></script>
	<link href="iwork_css/system/sys_operation_log.css" rel="stylesheet" type="text/css" />
	 <script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     }
}); //快捷键

</script>
</head>
<body class="easyui-layout">
<div region="center" style="padding:10px;border:0px;">
	<div style="padding:0px;border:1px solid #ccc;background:#FFFFEE;">
		<table width='90%' border='0' cellpadding='0' cellspacing='0'> 
			<tr> 
				 <td style='padding-top:10px;padding-bottom:10px;'> 
					<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
						<tr> 
							<td class= "searchtitle">操作人：</td>
							<td class= "searchdata"><input type=text size='8' class="easyui-validatebox" name='user' id='user' value='<s:property value='operateUser'  escapeHtml='false'/>'/>&nbsp;<span style='color:red'>*</span></td>
							<td class= "searchtitle">操作日期：</td>
							<td class= "searchdata">
							<input id="start" name="start" class="Wdate" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'{%y-20}-%M-%d %H:%m:%s'})" value='<s:property value="operateDateStart"  escapeHtml="false"/>' />
							至
							<input id="end" name="end" class="Wdate" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'start\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" value='<s:property value="operateDateEnd"  escapeHtml="false"/>'/>
							</td>
							<td class= "searchtitle">操作类型：</td> 
							 <td class= "searchdata">
								<select name='type' id='type'>
									<option value=''>请选择</option>
									<option value='0'>添加</option>
									<option value='1'>删除</option>
									<option value='2'>修改</option>
									<option value='3'>查询</option>
								</select>
							 </td> 
						</tr> 
						<tr> 
							 <td class= "searchtitle">操作表：</td> 
							 <td class= "searchdata"><input type=text size='8' class="easyui-validatebox" name='table' id='table' value='<s:property value='operateTable'  escapeHtml='false'/>'/>&nbsp;<span style='color:red'>*</span></td>
							 <td class= "searchtitle">功能名称：</td> 
							 <td class= "searchdata"><input type=text size='8' class="easyui-validatebox" name='function' id='function' value='<s:property value='functionName'  escapeHtml='false'/>'/></td>
						</tr> 
					</table> 
				</td> 
				<td valign='bottom' style='padding-bottom:5px;'> <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:query();" >查询</a></td>
			<tr> 
		</table>
	</div>
	<div style="padding:2px;margin-top:10px;margin-top:10px;border:1px solid #efefef">
		<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		<table id='sysoperateloggrid'></table>
		<div id='prowed_info_grid'></div> 
		<s:property value='infolist'  escapeHtml='false'/>
	</div>
</div>
<s:form>
	<input type="hidden" name="isDoSearch" value="false">
	<input type="hidden" name="logType" value="0">
	<input type="hidden" name="operateUser">
	<input type="hidden" name="operateDateStart">
	<input type="hidden" name="operateDateEnd">
	<input type="hidden" name="operateType">
	<input type="hidden" name="operateTable">
	<input type="hidden" name="functionName">
	
</s:form>
</body>
</html>
