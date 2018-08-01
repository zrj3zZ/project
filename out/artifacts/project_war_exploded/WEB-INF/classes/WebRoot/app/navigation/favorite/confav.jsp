<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>系统收藏夹维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/navigation/consysfav.js" ></script>
<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     } 
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增操作
					add(); return false;
				}
		   else if(evt.ctrlKey && event.keyCode==83){ //ctrl+s保存
					doSubmit(); return false;
				}		
}); //快捷键
	</script>
</head>
<body class="easyui-layout">
<div region="north" border="false" style="padding:0px;overflow:no">
<div style="padding:2px;background:#efefef;border-bottom:1px solid #efefef">
<a href="javascript:add();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
</div>
</div>
<div region="center" style="padding:3px;border:0px">
<table id="sysfavgrid"></table>
</div>
<!-- 编辑窗口 -->
<div id="idialog" icon="icon-ok"  style="width:300px;height:200px;padding-top:10px;background: #fafafa;">			
 <form id="iform" method="post">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
		    <td align='right'><label for="sys_name">系统名称：</label></td>
	        <td><input class="easyui-validatebox" required="true" validType="maxLength[64]" type="text" id="sys_name" name="sys_name"></input>&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
		    <td align='right'><label for="sys_url">系统URL：</label></td>
	        <td><input class="easyui-validatebox" required="true" validType="maxLength[256]" type="text" id="sys_url" name="sys_url"></input>&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
		    <td align='right'><label for="sys_target">提交目标：</label></td>
	        <td>
	        <select class="easyui-combobox" validType="noSelect" id="sys_target" editable='false'  name="sys_target" style="width:153px;">	
	        <option value="">请选择..</option>    
			<option value="mainFrame">mainFrame</option>
			<option value="_blank">_blank</option>	
			</select>
			<span style='color:red'>*</span>
	        </td>
        </tr>
        <tr>
		    <td align='right'><label for="sys_memo">备注：</label></td>
	        <td><input class="easyui-validatebox" validType="maxLength[500]" type="text" id="sys_memo" name="sys_memo"></input></td>
        </tr>
        </table>
        <input type='hidden' id='type' name='type'>
        <input type='hidden' id='sys_id' name='sys_id'>
        <input type='hidden' id='sys_index' name='sys_index'>
        <input type='hidden' id='isys_id' name='isys_id'>
        <input type='hidden' id='isys_index' name='isys_index'>  
    </form>	   
</div>
</body>
</html>