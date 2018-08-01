<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head><title>IWORK综合应用管理系统</title>
<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
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
<body>
<s:form id ="editForm" name="editForm" action="syspersion_update"  theme="simple">
<table width="100%" border="0" cellpadding="0" cellspacing="0" >
    <tr>
      <td align="center" valign="top" height="100%" >
		<div id="editInfo" style="width:80%;">
			<div id="basicInfo">
				<table width="100%">
					<tr>
						<td colspan="2" style="border-bottom:1px solid #efefef;height:7px;">
							<h4><span style="float: left">基本资料</span></h4> 
						</td>
					</tr>
					<tr>
						<td width="50%">
							<div id="basic-left">
								<table width="100%">
									<tr>
										<td class='td_title' width="10%">部门名称</td>
		    							<td class='td_data' width="40%"><s:property value="userModel.departmentname"/></td>
									</tr>
									<tr>
										<td class='td_title'>岗位名称</td>
										<td class='td_data'><s:property value="userModel.postsid"/>/<s:property value="userModel.postsname"/></td>
									</tr>
									<tr>
										<td class='td_title'>岗位职责</td>
										<td class='td_data'><s:property value="userModel.postsresponsibility"/></td>
									</tr>
									<tr>
										<td class='td_title'>直接上级</td>
										<td class='td_data'><s:property value="userModel.bossid"/></td>
										
									</tr>
									<tr>
										<td class='td_title'>我的状态</td>
										<td class='td_data'>
										<s:property value="workStatusOption"/>
										</td>
									</tr>
									<tr>
										<td class='td_title'>自我描述</td>
										<td class='td_data'><s:property value="userModel.selfdesc"/></td>
									</tr>
								</table>
							</div>
						</td>
						<td width="50%">
							<div id="basic-right">
								<table width="100%">
									<tr>
										<td style="text-align: center;vertical-align:middle">
											<div id="user_image_div">
												<img width="150" id="user_image" src='<s:property value='userImgPath'/>' alt='用户照片' title='用户照片' name='photoUpload' id='photoUpload' style="border:1px solid #e5e5e5;margin:3px;">
											</div>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div id="contactInfo">
				<table width="100%">
					<tr>
						<td colspan="2" style="border-bottom:1px solid #efefef;height:7px;">
							<h4><span style="float: left">联系方式</span></h4>
						</td>
					</tr>
					<tr>
						<td width="50%">
								<table width="100%">
									<tr>
										<td class='td_title'>手机</td>
		    							<td class='td_data' width="80%"><s:property value='userModel.mobile'/>&nbsp;</td>
									</tr>
									<tr>
										<td class='td_title'>邮箱</td>
										<td class='td_data'><s:property value='userModel.email'/>&nbsp;</td>
									</tr>
									<tr>
										<td class='td_title'>办公室电话</td>
		    							<td class='td_data'><s:property value='userModel.officetel'/>&nbsp;</td>
									</tr>
									<tr>
										<td class='td_title'>办公室传真</td>
		    							<td class='td_data'><s:property value='userModel.officefax'/>&nbsp;</td>
									</tr>
									<tr>
										<td class='td_title'>紧急联系人</td>
		    							<td class='td_data'><s:property value='userModel.jjlinkman'/>&nbsp;</td>
									</tr>
								</table>
						</td>
						<td width="50%">
								<table width="100%">
									<tr>
										<td class='td_title'>紧急联系电话</td>
		    							<td class='td_data' width="80%"><s:property value='userModel.jjlinktel'/>&nbsp;</td>
									</tr>
									<tr>
										<td class='td_title'>QQ/MSN</td>
		    							<td class='td_data'><s:property value='userModel.qqmsn'/>&nbsp;</td>
									</tr>
									<tr>
										<td class='td_title'>扩展一</td>
		    							<td class='td_data'><s:property value='userModel.extend1'/>&nbsp;</td>
									</tr>
									<tr>
										<td class='td_title'>扩展二</td>
		    							<td class='td_data'><s:property value='userModel.extend2'/>&nbsp;</td>
									</tr>
									<tr>
										<td class='td_title'>扩展三</td>
		    							<td class='td_data'><s:property value='userModel.extend3'/>&nbsp;</td>
									</tr>
								</table>
						</td>
					</tr>
				</table>
			</div>
		</div>
     </td>
    </tr>
  </table>
  	<s:hidden name="userModel.id"/>
  	<s:hidden name="userModel.userid"/>
  </s:form>
</body>	
</html>
