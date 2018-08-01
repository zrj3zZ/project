<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html> 
<head>
  <meta charset="utf-8"> 
  <title>详细资料</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
  <link rel="stylesheet" href="iwork_css/weixin/process/style.css">
  	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.layout-latest.js"></script>
	<script  type="text/javascript" src="iwork_js/weixin/weixin_tools.js"></script> 
<script>

</script>
<style type="text/css">
	.personDiv{
		background-color:#fff;
		border:1px solid #efefef;
	}
	.personDiv td{
		padding:5px;
	}
	.info{
		font-size:16px;
		padding:2px;
	}
	.info span{
	font-size:14px;
		color:#666;
	}
</style>
</head>
<body ontouchstart="">
<div data-role="page" id="pageone">
 <div data-role="content" >
			<div>
				<table width="100%">
					<tr>
						<td><img src="iwork_img/nopic.gif"/></td>
						<td>
							<div class="info">用户名：<span><s:property value="model.username"/>[<s:property value="model.userid"/>]</span></div>
							<div class="info">部门：<span><s:property value="model.departmentname"/></span></div>
							<div class="info">岗位：<span><s:property value="model.postsname"/></span></div>
							<div class="info">状态：<span><s:property value="model.workStatus"/></span></div>
						</td>
					</tr> 
				</table>
			<div>
			<div class="personDiv">
				<table width="100%">
					<tr>
						<td>手机</td><td><s:property value="model.mobile"/></td>
					</tr>
					<tr>
						<td>邮箱</td><td><s:property value="model.email"/></td>
					</tr>
					<tr>
						<td>办公室电话</td><td><s:property value="model.officetel"/></td>
					</tr>
					<tr>
						<td>传真</td><td><s:property value="model.officefax"/></td>
					</tr>
					<tr>
						<td>QQ</td><td><s:property value="model.qqmsn"/></td>
					</tr>
				</table> 
			</div>
			</div>
		</div>
		<!-- <div>
			<a href="#" class="ui-btn ui-corner-all">编辑个人信息</a>
			<a href="#" class="ui-btn ui-corner-all">修改口令</a>
			<a href="#" class="ui-btn ui-corner-all">消息提醒方式修改</a>
		</div> -->
		</div>
</div>
</body>
</html>
<script>
function hideOption(){
	 wx.hideOptionMenu();
}
setTimeout("hideOption()",500);
</script>