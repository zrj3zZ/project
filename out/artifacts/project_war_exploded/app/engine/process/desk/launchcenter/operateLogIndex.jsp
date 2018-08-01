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
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		WdatePicker({eCont:'div_calendar',startDate:'<s:property value="specDate" escapeHtml="false"/>',onpicked:function(dp){
			jumpSpecDay(dp.cal.getDateStr());
		}});
		checkHasData();
	});
	
	//初始化判断是否有数据
	function checkHasData(){
		var todayStr = $("#todayStr").html();
		if(todayStr==null){
			var trSize = $("tr.content").size();
			if(trSize==0){
				art.dialog.tips("您这一天没有任何操作记录",2);
			}
		}
	}
	
	//跳转到指定的天
	function jumpSpecDay(specDate){
		var logType = $("#logType").val(); 
		var pageurl="/sysOperateLogIndexAction.action?logType="+logType+"&specDate="+specDate;
		window.location.href=pageurl; 
	}
	
	//删除一条记录
	function deleteItem(id){
		if (id != "") {
			var url = 'sysOperateLogdeleteOperateLogAction.action';
			$.post(url,{id:id},function(rText){
				if(rText!='success'){
			    	art.dialog.tips("删除记录失败");
			    }else{
					$("#tr"+id).remove();
			    }
		    });
		} else {
			art.dialog.tips("删除记录失败,请稍后重试!");
			return;
		}
	}
	
	//删除用户某天的全部记录
	function deleteCurDayLog(){
		var b = art.dialog.confirm("您确定当天的删除吗?", function() {
			var url = 'sysOperateLogdeleteUserCurDayLog.action';
			$.post(url, {
				specDate : "<s:property value="specDate" escapeHtml="false"/>",
				logType : "<s:property value="logType" escapeHtml="false"/>"
			}, function(rText) {
				if (rText != 'success') {
					art.dialog.tips("删除记录失败");
				} else {
					var todayStr = $("#todayStr").html();
					if(todayStr!=null){
						$("tr.content").each(function(){
							var date = $(this).attr("date");
							if(date=='today'){
								$(this).remove();
							}
						});
					}else{
						$("tr.content").remove();
					}
					art.dialog.tips("删除成功",2);
				}
			});
		}, function() {
		});
	}
	
	//删除用户的全部记录
	function deleteUserAllLog() {
		var b = art.dialog.confirm("您确定删除全部记录吗?", function() {
			var url = 'sysOperateLogdeleteUserAllLog.action';
			$.post(url, {
				logType : "<s:property value="logType" escapeHtml="false"/>"
			}, function(rText) {
				if (rText != 'success') {
					art.dialog.tips("删除记录失败");
				} else {
					$("tr.content").remove();
					art.dialog.tips("删除成功",2);
				}
			});
		}, function() {
		});
	}
    <s:if test="'PROCESS_LAUNCH_CENTER_OPERATE_LOG'==logType">
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
	
	//增加一条操作记录
	function addOperateLog(actProcDefId, title){
		var url = "sysOperateLogaddOperateLogAction.action";  
	    $.post(url, {
	    	loginfo : actProcDefId,
	    	memo	: encodeURI(title),
			logType : "PROCESS_LAUNCH_CENTER_OPERATE_LOG"
		}, function(rText) {
			if (rText != 'success') {
				art.dialog.tips("增加记录失败");
			} 
		});
	}
	
	//我发起的流程
 	function mylaunchProcess(){
 		window.location.href="processLaunchCenter!recentlaunch.action";
 	}
	
	//回到发起中心主页
	function goTolaunchCenter(){
		window.location.href = "processLaunchCenter!index.action";
	}
	</s:if>
	<s:if test="'PERSION_REPORT_CENTER_SORT'==logType">
	//进入报表中心
	function goToReportCenter(){
		window.location.href = "ireport_rt_showcenter.action";
	}
	//打开报表	
	function openReport(logInfo,memo){
		var reportId;
		var type;
		var title = memo;
		if(logInfo.indexOf('::')>0){
			var ss = logInfo.split('::');
			reportId = ss[0];
			type = ss[1];
			var url = "ireport_rt_index.action?reportId="+reportId+"&reportType="+type;
			addOperateLog(title,reportId,type);
			openDialog(title,url); 
		}else{
			art.dialog.tips("打开报表失败,请联系管理员");
		} 
	}
	//添加和编辑窗口
	function openDialog(title,url){
	     window.parent.addTab(title,url,''); 
	}
	//增加一条操作记录
	function addOperateLog(title,reportId,type){
		var url = "sysOperateLogaddOperateLogAction.action";  
	    $.post(url, {
	    	loginfo : reportId+"::"+type,
	    	memo	: encodeURI(title),
			logType : "PERSION_REPORT_CENTER_SORT"
		}, function(rText) {
			if (rText != 'success') {
				art.dialog.tips("增加记录失败");
			} 
		});
	}
    </s:if>
</script>
	</head>
	<body> 
		<div style="min-width:700px;width:expression(document.body.clientWidth <= 700? "700px": "auto" );">
			<div class ="top_tab">
				<div class="report_top">
				  <ul class="sub_tab">
				  	<s:if test="'PERSION_REPORT_CENTER_SORT'==logType">
			    		<li><a href="javascript:goToReportCenter()" >报表中心</a></li>
				    	<li>|</li>
				    	<li><a href="javascript:window.location.reload();" class="sub_tab_selected" style="color:#000">查看日志 </a></li>
			    	</s:if>
			    	<s:if test="'PROCESS_LAUNCH_CENTER_OPERATE_LOG'==logType">
				    	<li><a href="javascript:goTolaunchCenter()" >流程发起中心</a></li>
				    	<li>|</li>
				    	<li><a href=javascript:mylaunchProcess() >最近发起的流程</a></li>
				    	<!-- 
				    	<li>|</li>
				    	<li><a href="javascript:window.location.reload();" class="sub_tab_selected" style="color:#000">发起日志</a></li>
				    	-->
			    	</s:if>
				  </ul>
				</div>
			</div>
			<div class="report_left">
			  <div class="report_right">
				  <div id="div_calendar" style="text-align: center;"></div>
				  <p style="margin:20px 10px 10px 10px;"><a href="javascript:deleteCurDayLog()">清除这一天的记录</a></p>
				  <p style="margin:10px;"><a href="javascript:deleteUserAllLog()">清除全部记录</a></p>
			  </div>
			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			  	<tr>
			      <td width="14%">&nbsp;</td>
			      <td width="63%">&nbsp;</td>
			      <td width="2%">&nbsp;</td>
			      <td width="21%">&nbsp;</td>
			    </tr>
			    <tr>
			      <s:if test="'PERSION_REPORT_CENTER_SORT'==logType">
			      	<td colspan="4" style="font-size:14px; font-weight:bold; color:#0175bb">查看日志</td>
			      </s:if>
			      <s:if test="'PROCESS_LAUNCH_CENTER_OPERATE_LOG'==logType">
			      	<td colspan="4" style="font-size:14px; font-weight:bold; color:#0175bb">发起日志</td>
			      </s:if>
			    </tr>
			    <tr>
			      <td width="14%">&nbsp;</td>
			      <td width="63%">&nbsp;</td>
			      <td width="2%">&nbsp;</td>
			      <td width="21%">&nbsp;</td>
			    </tr>
			    <!-- 今天的事件 -->
			    <s:if test="null!=todayStr">
			    <tr>
			      <td id="todayStr" colspan="4"><span class="time_style2"><s:property value="todayStr" escapeHtml="false"/></span></td>
			    </tr>
			    <s:iterator value="todayOperateLogList" status="status">
				    <tr date="today" class="content" id="tr<s:property value="id" escapeHtml="false"/>">
				      <td class="time_style"><s:date name="createdate" format="HH:mm"/></td>
						<s:if test="'PERSION_REPORT_CENTER_SORT'==logType">
							<td>
								<a
									href='javascript:openReport("<s:property value="loginfo" escapeHtml="false"/>","<s:property value="memo" escapeHtml="false"/>")'><s:property
										value="memo" escapeHtml="false" />
								</a>
							</td>
						</s:if>
						<s:if test="'PROCESS_LAUNCH_CENTER_OPERATE_LOG'==logType">
							<td>
								<a
									href='javascript:startProcess("<s:property value="loginfo" escapeHtml="false"/>","<s:property value="memo" escapeHtml="false"/>")'><s:property
										value="memo" escapeHtml="false" />
								</a>
							</td>
						</s:if>
						<td>&nbsp;</td>
				      <td><a href='javascript:deleteItem("<s:property value="id" escapeHtml="false"/>")'>删除本条记录 </a></td>
				    </tr>
			    </s:iterator>
			    <tr>
			      <td>&nbsp;</td>
			      <td>&nbsp;</td>
			      <td>&nbsp;</td>
			      <td>&nbsp;</td>
			    </tr>
			    </s:if>
			    <!-- 昨天的事件 -->
			    <s:if test="null!=yestodayStr">
			    <tr>
			      <td colspan="4"><span class="time_style2"><s:property value="yestodayStr" escapeHtml="false"/></span></td>
			    </tr>
			    <s:iterator value="yestodayOperateLogList" status="status">
				    <tr class="content" id="tr<s:property value="id" escapeHtml="false"/>">
				      <td class="time_style"><s:date name="createdate" format="HH:mm"/></td>
				      <s:if test="'PERSION_REPORT_CENTER_SORT'==logType">
							<td>
								<a
									href='javascript:openReport("<s:property value="loginfo" escapeHtml="false"/>","<s:property value="memo" escapeHtml="false"/>")'><s:property
										value="memo" escapeHtml="false" />
								</a>
							</td>
						</s:if>
						<s:if test="'PROCESS_LAUNCH_CENTER_OPERATE_LOG'==logType">
							<td>
								<a
									href='javascript:startProcess("<s:property value="loginfo" escapeHtml="false"/>","<s:property value="memo" escapeHtml="false"/>")'><s:property
										value="memo" escapeHtml="false" />
								</a>
							</td>
						</s:if>
				      <td>&nbsp;</td>
				      <td><a href='javascript:deleteItem("<s:property value="id" escapeHtml="false"/>")'>删除本条记录 </a></td>
				    </tr>
			    </s:iterator>
			    <tr>
			      <td>&nbsp;</td>
			      <td>&nbsp;</td>
			      <td>&nbsp;</td>
			      <td>&nbsp;</td>
			    </tr>
			    </s:if>
			    <!-- 最近一天的事件 -->
			    <s:if test="null!=latestdayStr">
			    <tr>
			      <td colspan="4"><span class="time_style2"><s:property value="latestdayStr" escapeHtml="false"/></span></td>
			    </tr>
			   <s:iterator value="latestOperateLogList" status="status">
				    <tr class="content" id="tr<s:property value="id" escapeHtml="false"/>">
				      <td class="time_style"><s:date name="createdate" format="HH:mm"/></td>
				      <s:if test="'PERSION_REPORT_CENTER_SORT'==logType">
							<td>
								<a
									href='javascript:openReport("<s:property value="loginfo" escapeHtml="false"/>","<s:property value="memo" escapeHtml="false"/>")'><s:property
										value="memo" escapeHtml="false" />
								</a>
							</td>
						</s:if>
						<s:if test="'PROCESS_LAUNCH_CENTER_OPERATE_LOG'==logType">
							<td>
								<a
									href='javascript:startProcess("<s:property value="loginfo" escapeHtml="false"/>","<s:property value="memo" escapeHtml="false"/>")'><s:property
										value="memo" escapeHtml="false" />
								</a>
							</td>
						</s:if>
				      <td>&nbsp;</td>
				      <td><a href='javascript:deleteItem("<s:property value="id" escapeHtml="false"/>")'>删除本条记录 </a></td>
				    </tr>
			    </s:iterator>
			    </s:if>
			    
			  </table>
			  <s:hidden id="logType" name="logType"></s:hidden>
			</div>
		</div>
	</body>
</html>