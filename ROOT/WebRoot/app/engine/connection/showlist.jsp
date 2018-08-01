<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

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
	<script type="text/javascript" src ="iwork_js/engine/conn_showlist.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
		.memo span {
			display:block;
			width:350px;
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
			} 
	</style>
</head> 
<body>  
	<s:if test="list==null || list.size()==0">
	            <div class="none_item"><img src="iwork_img/metadata.gif" border="0"> 未找到接口模型</div>
	 </s:if>   
 
	 	<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<s:iterator value="list">
					<tr>
						<td  class="right_left" style="padding-left:5px;"  width="98%">
					   		 <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
					      <tr> 
					        <td style="width:120px"  class="right_center">
					        	 <table width="100%">
											 <tr><td>
											  <s:if test="status==0||status==null">
											 	<div style="position:relative;"><div class="forbidden"></div>
											 	<s:if test="inType=='sap'">
											 		<img src="iwork_img/plugs_sap.jpg" alt="SAP接口模型" width="74"style=" padding:10px 0px 10px 10px"/>
											 	</s:if>
											 	<s:elseif test="inType=='webservice'">
											 		<img src="iwork_img/plugs_webservice.jpg" alt="WebService接口模型" width="74"style=" padding:10px 0px 10px 10px"/>
											 	</s:elseif>
											 	</div>
											 </s:if>
									      	<s:elseif test="status==1">
									      		<s:if test="inType=='sap'">
									      			<img src="iwork_img/plugs_sap.jpg" alt="SAP接口模型" width="74"  style=" padding:10px 0px 10px 10px"/>
									      		</s:if>
											 	<s:elseif test="inType=='webservice'">
											 		<img src="iwork_img/plugs_webservice.jpg" alt="WebService接口模型" width="74"  style=" padding:10px 0px 10px 10px"/>
											 	</s:elseif>
									      	</s:elseif>
											 </td></tr>
											<tr><td class="icon_txt">
											<s:if test="inType=='sap'">
											 		SAP接口
											 	</s:if>
											 	<s:elseif test="inType=='webservice'">
											 		WebService接口
											 	</s:elseif>
											
											
											</td></tr>
											
									</table>
					        </td> 
					         <td width="1%"  class="right_center" ><img src="iwork_img/engine/vertical.jpg" width="1" height="85" /></td>
					        <td class="right_center" width="90%"> 
					           		 <table  border="0" cellspacing="0" cellpadding="0" style="width:100%;height:100%">
					           		 	<tr>
					           		 		<td class="data_title">接口状态:
					           		 		<s:if test="status==0||status==null">
					           		 		【关闭】
					           		 		</s:if>
					           		 		<s:else>
					           		 		【开启】
					           		 		</s:else>
									      		&nbsp;&nbsp;<a href="javascript:del(<s:property value="id"/>)"><img src="iwork_img/del5.gif"></a>
					           		 		</td>
					           		 	</tr>
					           		 	<tr> 
					           		 		<td class="title">
					           		 		<s:property value="inName"/>【<s:property value="uuid"/>】
					           		 		</td>
					           		 	</tr>
					           		 	<tr>
					           		 		<td class="memo">
					           		 			<span title="<s:property value="inMemo"/>"><s:property value="inMemo"/></span>
					           		 			<span title="<s:property value="inAddress"/>"><s:property value="inAddress"/></span>
					           		 		</td>
					           		 	</tr>
					           		 	<tr>
					           		 	<td style="padding-bottom:5px">
					           		 	<a  href="#" class="easyui-linkbutton" onclick="showParamsDlg(<s:property value="id"/>)"   plain="false" iconCls="icon-sysbtn">参数设置</a>
										<a  href="#" onclick="edit(<s:property value="id"/>)" class="easyui-linkbutton" plain="false" iconCls="icon-search">接口日志</a>
										<a  href="#" onclick="manualOpera(<s:property value="id"/>)"  class="easyui-linkbutton" plain="false" iconCls="icon-ok">手动执行</a>
					           		 		</td>
					           		 		</tr>
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
