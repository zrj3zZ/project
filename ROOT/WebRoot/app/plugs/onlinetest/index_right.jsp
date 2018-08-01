<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<title>试卷内容</title>
		<link rel="stylesheet" href="iwork_css/common.css" type="text/css">
		<link rel="stylesheet" href="iwork_css/plugs/exam.css"
			type="text/css">
		<script type="text/javascript"
			src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript">
		
			function startTest(topicId){ 
			 
				var title = "<s:property value='topic.topicTitle'/>";				
				var url = "startTest.action?examNo=" + topicId;
				openDialog(title,url); 
				addOperateLog(actProcDefId, title);
			}
			
			//添加和编辑窗口
			function openDialog(title,url){
				 window.parent.parent.setRedirect('',url,title); 
			}
			
			//增加一条操作记录
			function addOperateLog(actProcDefId, title){
				var url = "sysOperateLogaddOperateLogAction.action";  
				var pars = "logType=PROCESS_LAUNCH_CENTER_OPERATE_LOG&loginfo="+actProcDefId+"&memo="+encodeURI(encodeURI(title));   
				var addLogAjax = new Ajax.Request(  
					 url,  
					 {method: 'get', parameters: pars,onComplete: showaddLogResponse}  
				);
			}
			
		</script>
	</head>
	<body>
		<s:property value='topicContent' escapeHtml="false" />
		<table width='100%' height='208' border='0' cellpadding='0'
			cellspacing='0'>
			<tr>
				<td width='10%' height='39'>
					&nbsp;
				</td>
				<td width='86%' class='title'>
					<s:property value='topic.topicTitle' escapeHtml="false" />
				</td>
				<td width='8%'>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td height='27'>
					&nbsp;
				</td>
				<td>
					<table width='100%' border='0' cellspacing='0' cellpadding='0'>
						<tr>
							<td width='20%'>
								<span class='td_title'>考试时间</span>：
							</td>
							<td class='td_data'>
								<s:property value='topic.topicTime' escapeHtml="false" />
							</td>
						</tr>
						<tr>
							<td width='20%'>
								<span class='td_title'>考试范围</span>：
							</td>
							<td class='td_data'>
								<s:property value='topic.topicArea' escapeHtml="false" />
							</td>
						</tr>
						<tr>
							<td width='20%'>
								<span class='td_title'>试卷分数</span>：
							</td>
							<td class='td_data'>
								<s:property value='topic.scoreExplain' escapeHtml="false" />
							</td>
						</tr>
						<tr>
							<td width='20%'>
								<span class='td_title'>试卷说明：</span>
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
					</table>
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
				<td valign='top' class='td_data_memo'>
					&nbsp;&nbsp;&nbsp;&nbsp;<s:property value='topic.topicContent' escapeHtml="false" />
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
				<td align='right'>
					<s:property value='topic.topicScore' escapeHtml="false" />
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
		</table>
	</body>
</html>