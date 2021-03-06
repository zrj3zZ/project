﻿<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

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
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src ="iwork_js/engine/dem_showlist.js"></script>  
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
</head> 
<body>  
	<s:if test="list==null || list.size()==0">
	            <div class="none_item"><img src="iwork_img/metadata.gif" border="0"> 未找到数据管理模型</div>
	 </s:if> 
	 	<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<s:iterator  value="list" status="status">
					<tr>
						<td  class="right_left" style="padding-left:5px;" width="98%">
					   		 <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
					      <tr> 
					        <td style="width:100px"  class="right_center">
					        	 <table>
											<s:if test="type==0||type==null">
											 <tr><td><img src="iwork_img/engine/data_form.png" alt="公共数据维护" width="74" height="61" style=" padding:10px 0px 10px 10px"/></td></tr>
											<tr><td class="icon_txt">公共数据维护</td></tr>
							 				</s:if>
											<s:else>
											<tr><td><img src="iwork_img/engine/process_form.png" alt="个人数据维护" width="74" height="61" style=" padding:10px 0px 10px 10px"/></td></tr>
											<tr><td class="icon_txt">个人数据维护</td></tr>	
											</s:else> 
									</table>
					        </td>
					         <td width="1%"  class="right_center" ><img src="iwork_img/engine/vertical.jpg" width="1" height="85" /></td>
					        <td class="right_center" width="100%"> 
					           		 <table  border="0" cellspacing="0" cellpadding="0" style="width:100%;height:100%">
					           		 	<tr>
					           		 		<td class="data_title"><s:property value="master"/>&nbsp;&nbsp;
					           		 		<a><a href="javascript:del(<s:property value="id"/>);"><img src="iwork_img/engine/bin_del.png" style="margin-left:5px;margin-right:5px;" border="0" /></a></td>
					           		 	</tr>
					           		 	<tr> 
					           		 		<td class="title">
					           		 			[<s:property value="id"/>]<s:property value="title"/>
					           		 		</td>
					           		 	</tr>
					           		 	<tr>
					           		 		<td class="memo" style="padding-left:50px;">
					           		 			<s:if test="memo==null||memo==\"\""> 
										    		<span style="width:300px;">未描述</span>
										    	</s:if>
										    	<s:else>
										    		<s:if test="memo.length()>100">
										    			<span title="<s:property value="memo" escapeHtml="true"/>"><s:property value="memo.substring(0,100)" escapeHtml="false"/> ...</span>
										    		</s:if> 
										    	</s:else> 
					           		 		</td>
					           		 	</tr>
					           		 	<td style="padding-bottom:5px">
					           		 			 <a  href="#"   onclick="openBaseInfo('<s:property value="title"/>',<s:property value="id"/>);"  class="easyui-linkbutton" plain="false" iconCls="icon-sysbtn">设置</a>
					           		 	
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
