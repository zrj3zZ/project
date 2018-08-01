<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>SAP连接登陆</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" type="text/css" href="iwork_css/login/themes/default/login.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
  </head>
  <script type="text/javascript">
  var mainFormValidator;
	$().ready(function() {
			mainFormValidator =  $("#editForm").validate({
				debug:false,
				errorPlacement: function (error, element) { //指定错误信息位置
				      if (element.is(':radio') || element.is(':checkbox')) {
				          var eid = element.attr('name');
				          error.appendTo(element.parent());
				      } else {
				          error.insertAfter(element);
				     }
				 } 
			 });
			 mainFormValidator.resetForm();
		});
  	function doBridgeLogin(){
  		var valid = mainFormValidator.form(); //执行校验操作
		if(!valid){
				return;
			}
		
		$("#editForm").submit();
  	}
  </script>
  <style>
  	.lo_showimg1{background:url(../../../../iwork_img/bridge.jpg) no-repeat #fff; height:330px; position:relative;}
  	.lo_list1{background:#fff; overflow:hidden; position:absolute; right:35px; top:30px;border:1px solid #efefef; padding-top:20px; width:342px;-webkit-box-shadow:0 0 5px rgba(0,0,0,0.3); -moz-box-shadow:0 0 5px rgba(0,0,0,0.3); box-shadow:0 0 5px rgba(0,0,0,0.3);}
	.lo_list1 li{padding:10px 30px; line-height:20px; overflow:hidden; zoom:1;}
	.lo_list1 li.spot_item{}
	.lo_list1 li.spot_item input{ background:none; font-size:12px; font-family:宋体;height:16px;color:#ccc;border:#d9d9d9 solid 1px; width:60px; padding:8px 10px; color:#666;}
	.lo_list1 li.spot_item input:hover{ background:none; border:1px solid #ff9933}
	.lo_list1 li.spot_item input:focus{ background:#ffffcc;}
	.lo_list1 li input.us_name,.lo_list li input.us_pwd,.lo_list li input.us_spot{border:#d9d9d9 solid 1px; width:260px; padding:8px 10px; color:#666;}
	.lo_list1 li input.us_spot{ width:130px;}
  	.lo_list1 li a.lo_bnt{display:block; width:280px; height:40px; line-height:40px; text-align:center; color:#fff; background:#00bfff; font-size:14px; text-decoration:none;}
  	.error{color: red;}
  	.topline{
  		border-bottom:1px solid #ccc;
  		margin-bottom:30px;
  	}
  	input{
  		padding:3px;
  		height:30px;
  		line-height:35px;
  		color:#666;
  		font-size:14px;
  		font-family:微软雅黑;
  	}
  </style>
  <body>
  	<div>
  		<input id="edit" type="hidden" value="<s:property value="html"/>"/>
  	</div>
<div class=" logo  "></div>
<div >
    <div class="lo_bg wap">
    <div class="lo_showimg1">
        <ul class="lo_list1">
        	<li class=" topline"><h3 ><s:property value="model.bridgeName"/></h3></li>
			<form action="conn_bridge_portal_login.action" method="get" id="editForm">
	  			<input id="uuid" type="hidden" name="uuid" value="<s:property value="uuid"/>"/>
		  		<table id="editTable" style="padding-left: 25px;line-height: 33px;">
		  			<s:property value="html" escapeHtml="false"/>
		  		</table>
	  		</form>
	  		<li><span><font color="red"><s:property value="msg"/></font></span></li>
            <li>
                <a class="lo_bnt lt" href="javascript:void(0);" onclick="doBridgeLogin();" title="登录">登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录</a>
            </li>
            <li>
            	提示：请填写用户名及密码信息，下次访问时将自动登陆该系统
            </li>
        </ul>
        </div>
    </div>
  
</div>

  	
  </body>
</html>
