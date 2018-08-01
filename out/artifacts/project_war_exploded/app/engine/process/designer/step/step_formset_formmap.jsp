<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" href="iwork_css/jquerycss/ui/base/jquery.ui.all.css">
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.core.js"></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.widget.js"></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.mouse.js"></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.selectable.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	
	<style type="text/css">
		.td_title {
				color:#004080;
				font-size: 12px;
				text-align: right;
				letter-spacing: 0.1em;
				padding-right:10px;
				white-space:nowrap;
				vertical-align:middle;
				font-family:"微软雅黑";
				height:15px;
				
			}
		.td_data{
			color:#0000FF;
			text-align:left;
			padding-left:3px;
			font-size: 12px;
			vertical-align:middle;
			word-wrap:break-word;
			word-break:break-all;
			font-weight:500;
			line-height:14px;
			padding-top:5px;
			font-family:"微软雅黑";
			height:15px;
		}
		input {
		 color:#0000FF;
		}
		textarea{
		color:#0000FF;
		}
		body {
			margin-left: 0px;
			margin-top: 0px;
			margin-right: 0px;
			margin-bottom: 0px;
		}
	</style>
	<style>
	#feedback { font-size: 1.4em; }
	#selectable .ui-selecting { background: #efefef; }
	#selectable .ui-selected {  }
	#selectable { list-style-type: none; margin: 0; padding: 0; width: 100%; }
	#selectable li { margin: 0px; padding:0px;padding-left:20px;border:0px;border-bottom:1px solid #efefef; font-size: 1.4em;}
	</style>
	
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
		$(function() {
			$( "#selectable" ).selectable({
				stop: function() {
					var result = $( "#select-result" ).empty();
					$(".ui-selected", this ).each(function() {
					//	var name = "#field"+this.id;
						//var v = $(eval("#field"+this.id)).attr("value");
						var v = $("#fieldItem-"+this.id).attr("checked");
						if(v==false){ 
							$("#fieldItem-"+this.id).attr("checked",'true');
						}else if(v==true){
							$("#fieldItem-"+this.id).removeAttr("checked"); 
						}else{
						}
					});
				}
			}); 
		});
		
		
		function doModify(){ 
		 $('form').attr('action','sysFlowDef_stepFormMap_setModify.action');
			$("#editForm").submit();
		}
		function doReadOnly(){ 
		 $('form').attr('action','sysFlowDef_stepFormMap_setReadOnly.action');
			$("#editForm").submit();
		}
		function doHidden(){ 
		 $('form').attr('action','sysFlowDef_stepFormMap_setHidden.action');
			$("#editForm").submit();
		}
		//执行退出
		function cancel(){
			api.close();
		}
	</script>
</head>
<body  class="easyui-layout">
		<div region="north" border="false" style="background:#EFEFEF;padding-top:2px;height:30px;text-align:left;">
				<a href="javascript:doReadOnly();" class="easyui-linkbutton" plain="true" iconCls="icon-process-readonly">授权只读</a>
				<a href="javascript:doModify();" class="easyui-linkbutton" plain="true" iconCls="icon-process-modify">授权编辑</a>
				<a href="javascript:doHidden();" class="easyui-linkbutton" plain="true" iconCls="icon-process-hidden">授权隐藏</a>
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div> 
		<div region="center" style="padding:0px;border:0px">
            	<s:form id ="editForm" name="editForm" action="sysFlowDef_stepFormSet.action"  theme="simple">
            	<s:property value="maplist"  escapeHtml="false"/>
	                <s:hidden name="actDefId"/>  
	                <s:hidden name="actStepDefId"/>
	                <s:hidden name="prcDefId"/>
	                <s:hidden name="id"/>
	                <s:hidden name="formId"/>
	             </s:form> 
	     </div>
</body>
</html>
