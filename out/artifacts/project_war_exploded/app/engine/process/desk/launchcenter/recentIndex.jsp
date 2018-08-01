<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="UTF-8">
	<head>
	<title>IWORK</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="Content-Language" content="zh-cn" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link href="iwork_css/engine/process_desk_center_operate_log.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.6.min.js"></script>
	<script language="javascript" src="iwork_js/commons.js"></script>
	<script language="javascript" src="iwork_js/scrollpagination.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		specDateGlo='<s:property value='specDate' escapeHtml='false'/>';
		
		requireItem('');
		$('#contentTable').scrollPagination({
			contentPage: 'processLaunchCenter!itemHTML.action', // the url you are fetching the results
			contentData: {specDate :function(){return specDateGlo}}, // these are the variables you can pass to the request, for example: children().size() to know which page you are
			scrollTarget: $(window), // who gonna scroll? in this example, the full window
			heightOffset: 10, // it gonna request when scroll is 10 pixels before the page ends
			beforeLoad: function(){ 
				$('#loading').fadeIn();
			},
			afterLoad: function(elementsLoaded,data){
				 $('#loading').fadeOut();
				 if(data.indexOf("@@@@")>0){
					$(elementsLoaded).fadeInWithDelay();
					var ss = data.split("@@@@");
					specDateGlo=ss[1];
				 }else if(data==""){
					art.dialog.tips("您没有更多记录了",2);
				 }else{
					$("#contentTable").append(data); 
				 }
			}
		});
		
		// code for fade in element by element
		$.fn.fadeInWithDelay = function(){
			var delay = 0;
			return this.each(function(){
				$(this).delay(delay).animate({opacity:1}, 200);
				delay += 100;
			});
		};
		
		WdatePicker({eCont:'div_calendar',onpicked:function(dp){
			jumpSpecDay(dp.cal.getDateStr());
		}});
	});
	
	//加载更多项
	function getMoreItem(){
		if(null==specDateGlo||""==specDateGlo){
			alert("加载更多项失败,请稍后再试");
		}else{
			requireItem(specDateGlo);
		}
	}
	
	//请求某天的HTML
	function requireItem(specDate){
		$('#loading').fadeIn();	
		var url = "processLaunchCenter!itemHTML.action";
		$.post(url, {
	    	specDate : specDate
		}, function(rText) {
			$('#loading').fadeOut();
			if(rText.indexOf("@@@@")>0){
				var ss = rText.split("@@@@");
				$("#contentTable").append(ss[0]);
				specDateGlo = ss[1];
			}else if(rText==""){
				art.dialog.tips("您没有更多记录了",2);
			}else{
				$("#contentTable").append(rText); 
			}
		});  
	}
	
	//跳转到指定的天
	function jumpSpecDay(specDate){
		specDateGlo = specDate;
		var url = "/processLaunchCenter!specItemHTML.action";
		$.post(url, {
	    	specDate : specDate
		}, function(rText) {
			if(rText.indexOf("@@@@")>0){
				var ss = rText.split("@@@@");
				$("tr.content").remove();
				$("#contentTable").append(ss[0]);
				specDateGlo = ss[1];
			}else if(rText=="error"){
				alert("加载记录失败,请联系管理员");
			}else if(rText==""){
				$("tr.content").remove();
				art.dialog.tips("您这一天没有发起任何流程",2);
			}else{
				$("tr.content").remove();
				$("#contentTable").append(rText); 
			}
		});  
	}
	
	//发起流程
	function startProcess(actProcDefId, title) {
		var url = 'processRuntimeStartInstance.action?actDefId='+actProcDefId;
		var target = getNewTarget();
		var page = window.open('form/loader_frame.html',target,'width='+screen.width-50+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location=url;
		//记录操作
		addOperateLog(actProcDefId, title);
		return;
	}
	
	//回到发起中心主页
	function goTolaunchCenter(){
		window.location.href = "processLaunchCenter!index.action";
	}
	
	//最近发起的流程
	function recentLog(){
		window.location.href = "sysOperateLogIndexAction.action?logType=PROCESS_LAUNCH_CENTER_OPERATE_LOG";
	}
	
	//打开流程
	function openProcess(actDefId,instanceId,excutionId,taskId) {
		var url = 'loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId;
		var target = getNewTarget();
		var page = window.open('form/loader_frame.html',target,'width='+screen.width-50+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location=url;
		return;
	}
</script>
	</head>
	<body>
		<div style="min-width:700px;width:expression(document.body.clientWidth <= 700? "700px": "auto" );">
			<div class ="top_tab">
				<div class="report_top">
				  <ul class="sub_tab">
				    <li><a href="javascript:goTolaunchCenter()" >流程发起中心</a></li>
				    <li>|</li>
				    <li><a href="javascript:window.location.reload();" class="sub_tab_selected" style="color:#000">我最近发起的流程</a></li>
				    <!-- 
				    <li>|</li>
				    <li><a href="javascript:recentLog()" >发起日志</a></li>
				    -->
				  </ul>
				</div>
			</div>
			<div class="report_left">
			  <div class="report_right">
				  <div id="div_calendar" style="text-align: center;"></div>
			  </div>
			  <table id="contentTable" width="100%" border="0" cellspacing="0" cellpadding="0">
			  	<tr>
			      <td width="14%">&nbsp;</td>
			      <td width="63%">&nbsp;</td>
			      <td width="2%">&nbsp;</td>
			      <td width="21%">&nbsp;</td>
			    </tr>
			  	<tr>
			      <td colspan="4" style="font-size:14px; font-weight:bold; color:#0175bb">我最近发起的流程</td>
			    </tr>
			    <tr>
			      <td width="14%">&nbsp;</td>
			    </tr>
			  </table>
			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			    <tr>
			      <td width="100%">&nbsp;</td>
			    </tr>
			    <tr>
			      <td width="100%" style="text-align: center"><a href="javascript:getMoreItem()">更多</a></td>
			    </tr>
			  </table>
			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			    <tr>
			      <td width="100%">
			      	<div class="loading" id="loading">正在加载...</div>
    			  </td>
			    </tr>
			  </table>
			</div>
		</div>
	</body>
</html>