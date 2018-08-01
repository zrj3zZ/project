<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title> 
	<link rel="stylesheet" type="text/css" href="admin/css/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_showlist.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src ="iwork_js/engine/form_showlist.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<style>
body {
	font-size:12px;
	padding:0px;
	margin:0px;
	border:0px
}
.content{
	padding-left:10px;
	margin-right:auto;
	text-align:center; 
}
.flowbox {
	width:240px; 
	height:200px;
	background:#fcfcfc; 
	float:left;
	display:inline-block;
	margin:3px;
	position:relative;
	border:1px solid #ccc;
	color:#333;
} 
.flowbox:hover {
	width:240px;
	height:200px;
	float:left;
	display:inline-block;
	margin:3px;
	position:relative;
	border:1px solid #FF9900;
	color:#FF9900;
	font-weight:bold;
	background:#fff; 
}
.flowbox_title_bar{
	cursor:pointer;
	background-repeat:repeat-x;
	border-bottom:1px solid #efefef;
	background-image:url(iwork_img/crumb_bg.png); 
	
}
.flowbox_title {
	float:left;
	text-align:left;
	font-size:12px;
	
}
.flowbox_date {
	float:right;
	width:70px;
}
.flowbox_del {
	float:right;
	width:20px;
	height:20px;
	cursor:pointer
}
.flowbox_body {
	clear:both;
	display:block;
	padding-top:5px;
}
.flowbox_body h1 {
	font-weight:bold;
	font-size:12px;
	text-align:left;
	padding:5px;
	cursor:pointer
}
.flowbox_body h2 {
	color:#999;
	font-size:12px;
	text-align:left;
	padding:5px;
	cursor:pointer;
	font-weight:100;
	margin:5px;
	padding:5px;
}
.flowbox_btn {
	position:absolute;
	right:10px;
	bottom:10px;
	color:#00F
}
.flowbox_btn a {
	text-decoration:none
}
.flowbox_btn a:linked {
color:#00F;
}
.flowbox_btn a:visited {
	color:#00F;
}
.flowbox_btn a:hover {
	color:#00F;
	text-decoration:underline
}
.flowbox_btn a:active {
	color:#00F; 
}
</style>
</head> 
<body>  
	<s:if test="list==null || list.size()==0">
	            <div class="none_item"><img src="iwork_img/metadata.gif" border="0"> 未找到表单模型</div>
	 </s:if> 
	 	 <div class="content">
				<s:iterator  value="list" status="status">
				
					<div class="flowbox"  >
					<div class="flowbox_title_bar"   onclick="openFormMap('<s:property value="iformTitle"/>',<s:property value="id"/>);" >
						<table width="100%">
							<tr><td class="flowbox_title">
					           				<s:if test="metadataType==0||metadataType==null">
											 <img src="iwork_img/table_edit.png" class="titleIcon" border="0"/>
							 				</s:if>
											<s:elseif test="metadataType==1">
											<img src="iwork_img/page_go.png" class="titleIcon" border="0"/>
											</s:elseif>
											<s:elseif test="metadataType==2">
											 	<img src="iwork_img/table_relationship.png" class="titleIcon" border="0"/>
											</s:elseif> 	 		
					           		 		
					           		 		<s:property value="iformTitle"/>
							</td>
							<td class="flowbox_del">
								<a href="javascript:del(<s:property value="id"/>);"><img border="0" src="iwork_img/engine/bin_del.png" alt="删除" /></a> 
							</td> 
							</tr>
						</table>
					</div>
					  <div class="flowbox_body"   onclick="openFormMap('<s:property value="iformTitle"/>',<s:property value="id"/>);" >
					    <h1>
							<s:if test="metadataType==0||metadataType==null">
											<!-- <img src="iwork_img/table_edit.png" class="titleIcon" border="0"/> -->
											【数据表单】
							 				</s:if>
											<s:elseif test="metadataType==1">
											【流程表单】
											</s:elseif>
											<s:elseif test="metadataType==2">
											【行项目】
											</s:elseif> 	 		
					           		 		
						</h1>
					    <h2>
								<s:if test="memo==null||memo==\"\""> 
										    		<span >未描述</span>
										    	</s:if>
										    	<s:else> 
										    		<s:if test="memo.length()>100">
										    			<span title="<s:property value="memo" escapeHtml="true"/>"><s:property value="memo.substring(0,100)" escapeHtml="false"/> ...</span>
										    		</s:if> 
										    		<s:else>
										    			<s:property value="memo" escapeHtml="false"/>
										    		</s:else>
										    	</s:else> 
						</h2>
					  </div>
					 
					  <div class="flowbox_btn" >
					  	<a  href="#"   onclick="openFormMap('<s:property value="iformTitle"/>',<s:property value="id"/>);" class="easyui-linkbutton" plain="true" iconCls="icon-sysbtn">域设置</a>
					           		 	
					           		 	<s:if test="metadataType!=2">
					           		 		<a href="javascript:void(0)" style="margin-left:1px;margin-right:1px" id="mb1" class="easyui-menubutton" menu="#backlist<s:property value="id"/>" iconCls="icon-sysbtn">表单布局</a>
					           		 		<div id="backlist<s:property value="id"/>"  style="width:150px;">
													<div  iconCls="icon-ifrom"  onclick="openFormDesigner('<s:property value="iformTitle"/>',<s:property value="id"/>,1);">通用表单</div>
													<div  iconCls="icon-purview-mobile"  onclick="openFormDesigner('<s:property value="iformTitle"/>',<s:property value="id"/>,2);">移动表单</div>
											</div>
					                	</s:if>
					</div>
					</div>
					
					<%-- tr>
						<td  class="right_left" style="padding-left:5px;width:98%">
					   		 <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
					      <tr> 
					        <td style="width:100px"  class="right_center">
					        	 <table>
											<s:if test="metadataType==0||metadataType==null">
											 <tr><td><img src="iwork_img/engine/data_form.png" alt="数据表单" width="74" height="61" style=" padding:10px 0px 10px 10px"/></td></tr>
											<tr><td class="icon_txt">数据表单</td></tr>
							 				</s:if>
											<s:elseif test="metadataType==1">
											<tr><td><img src="iwork_img/engine/process_model.png" alt="流程表单" width="74" height="61" style=" padding:10px 0px 10px 10px"/></td></tr>
											<tr><td class="icon_txt">流程表单</td></tr>	
											</s:elseif>
											<s:elseif test="metadataType==2">
											<tr><td><img src="iwork_img/engine/openwin.png" alt="流程表单" width="74" height="61" style=" padding:10px 0px 10px 10px"/></td></tr>
											<tr><td class="icon_txt">行项目表</td></tr>	
											</s:elseif> 
									</table>
					        </td>
					         <td width="1%"  class="right_center" ><img src="iwork_img/engine/vertical.jpg" width="1" height="85" /></td>
					        <td class="right_center" width="90%"> 
					           		 <table  border="0" cellspacing="0" cellpadding="0" style="width:100%;height:100%">
					           		 	<tr>
					           		 		<td class="data_title"><s:property value="master"/>&nbsp;&nbsp;
					           		 		<s:property value="%{getText('{0,date,yyyy-MM-dd }',{createdate})}"/>
					           		 		<a><a href="javascript:del(<s:property value="id"/>);"><img src="iwork_img/engine/bin_del.png" style="margin-left:5px;margin-right:5px;" border="0" /></a></td>
					           		 	</tr>
					           		 	<tr> 
					           		 		<td class="title">
					           		 		<s:if test="visitType==0||metadataType==null">
					           		 			【验证访问】
											</s:if>
											<s:elseif test="visitType==1">
												【匿名访问】
											</s:elseif>
					           		 		<s:property value="iformTitle"/>
					           		 		</td>
					           		 	</tr>
					           		 	<tr>
					           		 		<td class="memo" style="padding-left:50px;" width="100%">
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
					           		 			<a  href="#"   onclick="openFormMap('<s:property value="iformTitle"/>',<s:property value="id"/>);"  class="easyui-linkbutton" plain="false" iconCls="icon-sysbtn">域设置</a>
					           		 	
					           		 	<s:if test="metadataType!=2">
					           		 			<a  href="#"   onclick="openFormDesigner('<s:property value="iformTitle"/>',<s:property value="id"/>);"  class="easyui-linkbutton" plain="false" iconCls="icon-sysbtn">表单布局</a>
					                	</s:if>
					           		 	</td>
					           		 </table>
					          </td> 
					        <td ><div class="right_right"></div></td>
					      </tr>
					    </table>
					    </td>
						</tr> --%>
			</s:iterator>
			</div>	
			<s:form name="editForm" id="editForm">
			</s:form>
</body>
</html>
