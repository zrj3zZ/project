<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_showlist.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src ="iwork_js/process/design_showlist.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>  
</head>
<body>
	<s:if test="list==null || list.size()==0">
	            <div class="none_item"><img src="iwork_img/metadata.gif" border="0"> 未找到流程模型</div>
	 </s:if> 
	 	<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<s:iterator  value="list" status="status">
					<tr>
						<td  class="right_left" style="padding-left:5px;" width="98%">
					   		 <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
					      <tr> 
					        <td style="width:100px"  class="right_center">
					        	 <table>
											 <tr><td>
											 <s:if test="status==0||status==null">
											 	<div style="position:relative;"><div class="forbidden"></div><img src="iwork_img/engine/process_model.png" alt="流程模型" width="74" height="61" style=" padding:10px 0px 10px 10px"/></div>
											 </s:if>
									      	<s:elseif test="status==1">
									      		<img src="iwork_img/engine/process_model.png" alt="流程模型" width="74" height="61" style=" padding:10px 0px 10px 10px"/>
									      	</s:elseif>
											 </td></tr>
											<tr><td class="icon_txt"><s:property value="version"/></td></tr>
											
									</table>
					        </td> 
					         <td width="1%"  class="right_center" ><img src="iwork_img/engine/vertical.jpg" width="1" height="85" /></td>
					        <td class="right_center" width="90%"> 
					           		 <table  border="0" cellspacing="0" cellpadding="0" style="width:100%;height:100%">
					           		 	<tr>
					           		 		<td class="data_title">流程状态:
					           		 		<s:if test="status==0||status==null">
									      		【关闭】<a href="javascript:changeStatus(<s:property value="id"/>,1);" title="点击变更当前流程为发布状态" style="color:#929292; font-size:12px; margin-left:20px; font-family:"宋体""><img src='iwork_img/goto.gif' border='0'/>发布流程</a>
									      	</s:if>
									      	<s:elseif test="status==1">
									      		【运行中】<a href="javascript:changeStatus(<s:property value="id"/>,0);"  title="点击取消当前流程的发布状态"  style="color:#929292; font-size:12px; margin-left:20px; font-family:"宋体""><img src='iwork_img/goto.gif' border='0'/>取消发布</a>
									      	</s:elseif>
					           		 		&nbsp;&nbsp;
					           		 		<a href="processDeploy_showVersionList.action?processKey=<s:property value="prcKey"/>"><img src="iwork_img/min_monitor.gif" border="0">
					           		 		[查看/复制]版本历史</a>
					           		 		</td>
					           		 	</tr>
					           		 	<tr> 
					           		 		<td class="title">
					           		 		<s:property value="title"/>[<s:property value="uploader"/>]
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
					           		 	<td style="padding-bottom:5px">
					           		 			<a  href="#" onclick="openProcessDef('<s:property value="title"/>',<s:property value="id"/>,'<s:property value="prcKey"/>','<s:property value="actDefId"/>')"   class="easyui-linkbutton" plain="false" iconCls="icon-sysbtn">流程设置</a>
					           		 			<a  href="#"  onclick="showProcessDesigner('<s:property value="title"/>',<s:property value="id"/>,'<s:property value="prcKey"/>','<s:property value="actDefId"/>')"   class="easyui-linkbutton" plain="false" iconCls="icon-sysbtn">节点设置</a>
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
