<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>IWORK综合应用管理系统</title>
<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
	fieldset{margin:10px;font-size: 12px;border:1px solid #999;text-align:left}
	legend{color: #333;font-size: 14px;text-align:left}
li{list-style: none;color:#333;}
 .tips{
 	color:#0000ff;
 }
 select{
 	line-height:30px; 
 }
 .rolelist{
 	
 	font-size:14px;
 	height:35px;
 	width:100%;
 	background:#ffff00;
 	color:#0000ff;
 	padding:5px;
 	font-weight:bold;
 	font-family:微软雅黑;
 	
 }
 .rolelist option{
 	line-height:30px; 
 }
 
</style>
</head>	
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" >
	$(document).ready(function(){ 
		$('#rolelist').change(function(){ 
			var itemid = this.value; 
			$.post('persion_role_doexchange.action',{itemid:itemid},function(response){
    	 						if(response=='success'){ 
    	 							window.location.reload();
    	 						}else{
    	 							alert('角色切换异常，请联系管理员');
    	 						}
    					});
		});
	}) 
</script>
<body>
<form id="editForm" name="editForm" action="/syspersion_update.action" method="post">
<table width="100%" border="0" cellpadding="0" cellspacing="0" >
	<tr>
      <td>
		  		<div  class="tools_nav">
			  		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		  		</div>
	  </td>
    </tr>
    <tr>
      <td align="center" valign="top" height="100%" >
		<div id="editInfo" style="width:80%;">
			<div id="basicInfo">
				<fieldset>
					<legend>主角色信息</legend> 
					<div id="basic-left">
								<table width="100%">
									<tr>
										<td class='td_title' width="10%">公司名称</td>
		    							<td class='td_data' width="40%"><s:property value="mutiModel.companyname"/></td>
									</tr>
									<tr>
										<td class='td_title' width="10%">部门名称</td>
		    							<td class='td_data' width="40%"><s:property value="mutiModel.departmentname"/></td>
									</tr>
									<tr>
										<td class='td_title'>角色名称</td>
										<td class='td_data'><s:property value="mutiModel.roleName"/></td>
									</tr> 
								</table>
							</div>
				</fieldset>
				<fieldset>
					<legend>角色切换</legend> 
					<div id="basic-left">
						<s:property value="rolelistHtml" escapeHtml="false"/>
					</div>
				</fieldset>
				<table width="100%">
					<tr>
						<td colspan="2" style="border-bottom:1px solid #efefef;height:7px;">
							<h4><span style="float: left"></span></h4>
						</td>
					</tr>
					
				</table>
			</div>
		</div>
     </td>
    </tr>
  </table>
  	<input type="hidden" name="userModel.id" value="1" id="editForm_userModel_id"/>
  	<input type="hidden" name="userModel.userid" value="ADMIN" id="editForm_userModel_userid"/>
  	<input type="hidden" name="userModel.extend1" value="" id="editForm_userModel_extend1" class="true_input"/>
  	<input type="hidden" name="userModel.extend2" value="" id="editForm_userModel_extend2" class="true_input"/>
  	<input type="hidden" name="userModel.extend3" value="" id="editForm_userModel_extend3" class="true_input"/>
  </form>




</body>	
</html>
