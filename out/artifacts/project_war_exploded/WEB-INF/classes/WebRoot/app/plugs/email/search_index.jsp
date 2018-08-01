<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">  
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script language="javascript" src="iwork_js/commons.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
		fieldset{
			border:1px solid #999;	
			color:#666;
			background:#FFFFEE;
		}
		input{
			color:#0000ff;
			padding:3px;
		}
		SELECT{
			color:#0000ff;
			padding:3px;
		}
		.Wdate{
			width:100px;
			padding:3px;
		}
		.td_left{
			text-align:left;
		}
		.td_right{
			text-align:right;
		}
	</style>
	<script type="text/javascript" >
		function  doSearch(){
			var sender = $("#sender").val();
			var recever = $("#recever").val();
			var position = $("#position").val();
			var folderid = $("#folderid").val();
			var begindate = $("#begindate").val();
			var enddate = $("#enddate").val();
			var keyword = $("#keyword").val();
			if(sender==''&&recever==''&&begindate==''&&enddate==''&&keyword==''){
				art.dialog.tips("请输入查询条件");
				return ;
			}
			var url = "iwork_email_search.action";
	 		$('#editForm').attr('action',url); 
	 		$('#editForm').attr('target',"ifm");
	 		$('#editForm').submit(); 
		}
	</script>
</head>
<body class="easyui-layout">
		 <div region="north" border="false"  style="overflow:hidden;height:140px;padding:10px;" > 
		 	<s:form id="editForm" name="editForm" theme="simple">
		 		<fieldset width="100%">
            		<legend>高级查询</legend>    
					<div>
						<table width="100%">
							<tr>
	            				<td class="td_right">发送人：</td>
	            				<td class="td_left"><input type="text" name="sm.sender" id="sender" value=""/><a id='radioBtnhtml' href="###" title="单选地址薄"  onclick="radio_book('false','false','','','','','','','sender');" class="easyui-linkbutton" plain="true" iconCls="icon-radiobook"></a> </td>
	            				<td class="td_right">接收人：</td>
	            				<td class="td_left"><input type="text" name="sm.recever" id="recever"  required="required" value=""/> <a id='radioBtnhtml' href="###" title="单选地址薄"  onclick="radio_book('false','false','','','','','','','recever');" class="easyui-linkbutton" plain="true" iconCls="icon-radiobook"></a></td>
	            			</tr>
							<tr>
								<td class="td_right" width="100">
									文件夹：
								</td>
								<td nowrap class="td_left">
									<SELECT id="folderid" name="sm.folderid" class="txt txt2">
									    <OPTION value="all">全部邮件</OPTION> 
										<OPTION value="-2">收件箱</OPTION>
										<!-- <OPTION value="0">草稿箱</OPTION> -->
										<OPTION value="-1">已发送</OPTION>
									</SELECT>
								</td>
								<td  class="td_right" >开始日期/结束日期：</td>
		           			    <td  class="td_left"><input id="begindate" class="Wdate" value=""  name="sm.begindate" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'enddate\')||\'2020-10-01\'}'})"/>至<input id="enddate" class="Wdate" name="sm.enddate" value=""   type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'RELEASEDATE\')}',maxDate:'2020-10-01'})"/></td>
							
	            			</tr>
	            			<tr> 
		           			    <td class="td_right">
									位置：
								</td>
								<td nowrap class="td_left">
									<SELECT id="position" name="sm.position" class="txt txt2">
									 	<OPTION value="0">全部内容</OPTION>
										<OPTION value="2">主题</OPTION>
										<OPTION value="1">邮件正文</OPTION>
										<OPTION value="3">附件名</OPTION>
									</SELECT>
								</td>
	           			        <td  class="td_right" >关键词：</td>
	           			        <td  class="td_left"><input type="text" name="sm.keyword"  id="keyword"  required="required" value=""/> </td>
	            			 	<td valign='bottom' style='padding-bottom:5px;'> <a id="btnEp" class="easyui-linkbutton" icon="icon-search" href="javascript: doSearch();" >查询</a></td> </tr>
		                </table>         
					</div>
				</fieldset>
			</s:form>
        </div>   
        <div region="center" border="false" style="background: #fff; border:1px solid #efefef;padding:5px;">
         	 <iframe width='100%' height='99%' src ="iwork_email_search.action" frameborder="0" marginheight="0" marginwidth="0" frameborder="0" scrolling="auto" id="ifm" name="ifm"></iframe>
        </div>
</body>
</html>
