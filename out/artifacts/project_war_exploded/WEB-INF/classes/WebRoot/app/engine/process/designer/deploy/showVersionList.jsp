<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_showlist.css"> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src ="iwork_js/process/design_showlist.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
		/* CSS Document */

		input {
		outline: none;
		}
		textarea {
		outline: none;
		}
		* {
			padding:0px;
			margin:0px;
			font-size:12px;
		}
		.box {
			width:99%;
		}
		.edition_frame{width:100%; padding:5px; background:url(iwork_img/edition_bg.jpg) repeat-x;}
		.edition_nav{  background:url(iwork_img/edition_nav.jpg) repeat-x;; height:32px; padding-left:0px; padding-right:0px;line-height:32px;}
		.edition_title{
			font-size:15px;
			color:#0000FF;
			font-weight:500;
			padding-left:20px;
			padding-right:20px;
			line-height:30px;
		}
		.edition_article {
			font-size:12px;
			color:#6c6c6c;
			padding-left:40px;
			line-height:20px;
			margin-bottom:5px;
		}
		.edition_btn{ line-height:24px; height:24px;clear:both ;text-align:right; padding-right:20px; width:100%; margin-bottom:10px;}
		
		body {
			margin-left: 0px;
			margin-top: 0px;
			margin-right: 0px;
			margin-bottom: 0px;
		}
	</style>
</head>
<body>
	<s:if test="list==null || list.size()==0">
	            <div class="none_item">无流程模型</div>
	 </s:if> 
	 <table width="100%" cellspacing="0" cellpadding="0" border="0">
				<s:iterator  value="list" status="status">
					<tr>
						<td  class="right_left" style="padding-left:5px;" width="100%">
					   		 <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
					      <tr> 
					        <td style="width:100px"  class="right_center">
					        	 <table width="100%">
											 <tr><td>
											 	<s:if test="status==0||status==null">
												 	<img src="iwork_img/server_off.gif" alt="流程模型" width="62" height="64" style=" padding:10px 0px 10px 10px"/>
												 </s:if> 
										      	<s:elseif test="status==1">
										      		<img src="iwork_img/server_on.gif" alt="流程模型" width="62" height="64" style=" padding:10px 0px 10px 10px"/>
										      	</s:elseif>
											 </td></tr>
											<tr><td class="icon_txt"><s:property value="versionType"/></td></tr>
											
									</table>
					        </td> 
					         <td width="1%"  class="right_center" ><img src="iwork_img/engine/vertical.jpg" width="1" height="85" /></td>
					        <td class="right_center"> 
					           		 <table  border="0" cellspacing="0" cellpadding="0" style="width:100%;height:100%">
					           		 	<tr>
					           		 		<td class="data_title">
					           		 		<s:if test="status==0||status==null">
									      		【关闭】
									      	</s:if>
									      	<s:elseif test="status==1">
									      		【运行中】
									      	</s:elseif>
					           		 		<s:property value="uploader"/>&nbsp;&nbsp;
					           		 		<s:property value="%{getText('{0,date,yyyy-MM-dd }',{uploadDate})}"/>
					           		 		<a href="javascript:del(<s:property value="id"/>,0);"><img src="iwork_img/engine/bin_del.png" style="margin-left:5px;margin-right:5px;" border="0" /></a>
					           		 		
					           		 		</td>
					           		 	</tr>
					           		 	<tr> 
					           		 		<td class="title">
					           		 		<s:property value="title"/>
					           		 		</td>
					           		 	</tr>
					           		 	<tr>
					           		 		<td class="memo">
					           		 			<s:if test="memo==null||memo==\"\""> 
										    		<span style="width:300px;">未描述</span>
										    	</s:if>
										    	<s:else>
										    		<s:property value="memo"/> 
										    	</s:else>
					           		 		</td>
					           		 	</tr>
					           		 	<td style="padding-bottom:5px;" ><s:if test="status==0||status==null">
					           		 				<a  href="#"  onclick="copyProcess(<s:property value="id"/>,'<s:property value="prcKey"/>','<s:property value="actDefId"/>')"  class="easyui-linkbutton" plain="false" iconCls="icon-sysbtn">版本复制</a>
					           		 				<a  href="#"  onclick="changeStatus(<s:property value="id"/>,1)" class="easyui-linkbutton" plain="false" iconCls="icon-sysbtn">版本发布</a>
												 </s:if> 
										      	<s:elseif test="status==1">
										      		<a  href="#"   onclick="copyProcess(<s:property value="id"/>,'<s:property value="prcKey"/>','<s:property value="actDefId"/>')"  class="easyui-linkbutton" plain="false" iconCls="icon-sysbtn">版本复制</a>
										      		<a  href="#"  onclick="changeStatus(<s:property value="id"/>,0)"   class="easyui-linkbutton" plain="false" iconCls="icon-sysbtn">版本停用</a>
										      	</s:elseif>
										<a  href="#"   onclick="showProcessDesigner('<s:property value="title"/>',<s:property value="id"/>,'<s:property value="prcKey"/>','<s:property value="actDefId"/>')"   class="easyui-linkbutton" plain="false" iconCls="icon-sysbtn">节点设置</a>
										<a  href="#"   onclick="openProcessDef('<s:property value="title"/>',<s:property value="id"/>,'<s:property value="prcKey"/>','<s:property value="actDefId"/>')"   class="easyui-linkbutton" plain="false" iconCls="icon-sysbtn">全局设置</a>
										      	
					           		 	
					           		 	</td>
					           		 </table>
					          </td> 
					        <td ><div class="right_right"></div></td>
					      </tr>
					    </table>
					    </td>
						</tr>
			</s:iterator>
			</table>
			<s:form name="editForm" id="editForm">
			</s:form>
</body>
</html>
