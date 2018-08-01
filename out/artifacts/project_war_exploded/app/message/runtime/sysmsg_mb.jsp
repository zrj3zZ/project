<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jquery.mobile-1.3.2.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.3.2.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.layout-latest.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqm-datebox.core.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqm-datebox.mode.calbox.min.js"></script>
<style >
	#bg{
		background:#fff;
		padding:5px;
	}
	.msgTitle{
		vertical-align:top;
		line-height:20px;
		padding-right:5px;
		white-space:nowrap;
		border-bottom:1px solid #efefef;
	}
	.msgData{
		line-height:20px;
		padding-left:0px;
		border-bottom:1px solid #efefef;
		font-size:14px;
	}
</style>
</head>
<body>
	<div data-role="page" class="type-interior"> 
	<div data-role="content"> 
		<form   id="iformMain" name="iformMain" method="post"  data-ajax="true" action='processRuntimeFormSave.action' >
			<div id="bg">
				<table   cellpadding=0 cellspacing=0>
					<tr>
						<td class="msgTitle">标&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;题:</td>
						<td class="msgData"><s:property value="model.title"/></td>
					</tr>
					<tr>
						<td class="msgTitle">发&nbsp;&nbsp;送&nbsp;&nbsp;人:</td>
						<td class="msgData"><s:property value="model.sender"/></td>
					</tr>
					<tr> 
						<td class="msgTitle">类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型:</td>
						<td class="msgData">
						 <s:if test="type==0">
				       		 系统消息
				        </s:if>
				        <s:elseif test="type==1">
				        	  生日提醒</a>
				        </s:elseif>
				        <s:elseif test="type==2">
				        	  流程通知</a>
				        </s:elseif>
				        <s:elseif test="type==3">
				        	  流程通知</a>
				        </s:elseif>
				        <s:else> 
				          系统消息</a>
				        </s:else>
						</td>
					</tr>
					<tr>
						<td class="msgTitle">消息正文:</td>
						<td class="msgData"><s:property value="model.content" escapeHtml="false"/></td>
					</tr>
					<s:if test="url!=null&&url!=''">
					<tr>
						<td class="msgTitle">相关连接</td>
						<td>
							<a href="<s:property value="url"/>" target='_blank'><img border="0" src="iwork_img/link.png"/>相关链接</a>
						</td>
					</tr>
			      	</s:if>
					
						
				</table>
			</div>
		</form>
	</div><!-- /content --> 
</div><!-- /page --> 
</body>
</html>
