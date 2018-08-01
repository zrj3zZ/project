<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="UTF-8">
	<head>
	<title>IWORK</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="Content-Language" content="zh-cn" />

	<link href="iwork_css/engine/procee_launcher_center.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.2.min.js"></script>
    <script type="text/javascript" src="iwork_js/engine/process_launcher_center.js"></script>
	<link href="iwork_js/workflowCenter/inettuts.css" rel="stylesheet" type="text/css" />
	</head> 
	<body>
		<div class="report_top">
			<ul class="sub_tab">
				<li>
					<a href="javascript:this.location.reload();" class="sub_tab_selected" style="color: #000">流程发起中心</a>
				</li>
				<li>
					|
				</li>
				<li>
					<a href="javascript:mylaunchProcess()">最近发起过的流程</a>
				</li>
				<!-- 
				<li>
					|
				</li>
				<li>
					<a href="javascript:recentLog()">发起日志</a>
				</li>
				-->
			</ul>
			<div style="text-align: right; border: 0px; padding: 3px; font-size: 12px; padding-right: 10px;">
			<a href="javascript:backToDefault();">恢复默认布局</a>
			</div>
		</div>
		
		
		<div id="columns">
			<s:property value="processDeskLaunchCenterHTML" escapeHtml="false" />
		</div>
		
		
		<!-- 保存页面布局的TYPE -->
		<input type="hidden" name="type" id="type" value="">
		<input type="hidden" name="saveStrType" id="saveStrType" value="Process_Launch_Center_Position">
		<script type="text/javascript" src="iwork_js/workflowCenter/jquery-ui-personalized-1.6rc2.min.js"></script>
		<script type="text/javascript" src="iwork_js/workflowCenter/cookie.jquery.js"></script>
		<script type="text/javascript" src="iwork_js/workflowCenter/inettuts.js"></script>
	</body>
</html>