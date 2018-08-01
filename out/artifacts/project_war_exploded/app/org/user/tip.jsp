<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
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
.info{
	line-height: 23px;
	color:#004080;
	letter-spacing: 0.1em;
	white-space:nowrap;
	border-bottom:1px #999999 thick;
	vertical-align:middle;
	font-family:"微软雅黑";
}
</style>
</head>	
<body>
<table width="300px" style="margin: 20px">
	<tr>
		<td width="30%">
			<div id="user_photo">
			<img width="90" id="user_image" src='<s:property value='userImgPath'/>' alt='用户照片' title='用户照片' name='photoUpload' id='photoUpload' style="border:1px solid #e5e5e5;margin:3px;">
			</div>
		</td>
		<td width="70%" style="padding-left: 10px;">
			<div id="user_content" style="text-align:left;">
				<table width="100%" cellspacing="5" cellpadding="0px;">
					<tr>
			    		<td class="info">员工编号:<s:property value="model.userno"/></td>
			    	</tr>
					<tr>
			    		<td class="info">部门名称:<s:property value="model.departmentname"/></td>
			    	</tr>
			    	<tr>
			    		<td class="info">岗位名称:<s:property value="model.postsid"/></td>
			    	</tr>
			    	<tr>
			    		<td class="info">岗位职责:<s:property value="model.postsresponsibility"/></td>
			    	</tr>
			    	<tr>
			    		<td class="info">直接上级:<s:property value="model.bossid"/></td>
			    	</tr>
				</table>
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="2"> 
			<div id="user_content_more" style="text-align:left;">
				<table width="100%" cellspacing="5" cellpadding="0">
					<tr>
			    		<td class="info">我的状态:<s:property value="model.workStatus"/></td>
					</tr>
					<tr>
			    		<td class="info">手机号:<s:property value="model.mobile"/></td>
					</tr>
					<tr>
			    		<td class="info">邮箱:<s:property value="model.email"/></td>
					</tr>
					<tr>
			    		<td class="info">办公室电话:<s:property value="model.officetel"/></td>
					</tr>
					<tr>
			    		<td class="info">自我描述:<s:property value="model.selfdesc"/></td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
</table>
</body>	
</html>
