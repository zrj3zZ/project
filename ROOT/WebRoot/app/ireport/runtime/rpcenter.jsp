<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="UTF-8">
	<head>
		<title>IWORK</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="Content-Language" content="zh-cn" />

		<link href="iwork_css/engine/procee_launcher_center.css" rel="stylesheet" type="text/css" />
		
        <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.2.min.js"></script>
		<link href="iwork_js/workflowCenter/inettuts.css" rel="stylesheet" type="text/css" />

	<script type="text/javascript">
	var process_launch_center_sort_index = 'process_launch_center_sort_index';
	
	//增加一条操作记录
	function addOperateLog(title,reportId,type){
		var url = "sysOperateLogaddOperateLogAction.action";  
	    //var pars = "logType=PERSION_REPORT_CENTER_SORT&loginfo="+reportId+"::"+type+"&memo="+encodeURI(encodeURI(title));   
	    
	    $.post(url,{
	    	logType:"PERSION_REPORT_CENTER_SORT",
	    	loginfo:reportId+"::"+type,
	    	memo:title
	    },function(data){
	    	showaddLogResponse(data);
	    });
	    
	    
	}
	
	function showaddLogResponse(response) {
	    var rText = response;
	    if(rText!='success'){
	    	alert("保存操作记录失败");
	    }
	}
	//恢复默认布局
	function backToDefault(){
  		var msg = "确认恢复默认布局吗?";
  		if (confirm(msg) == true) {
  			var saveStrType = jQuery("#saveStrType").val();
  			var cookieString = "";
  	        $.post("save_report_center_save_str.action",
  	        	{reportCenterSaveStr:cookieString,saveStrType:saveStrType},
  	        	function(result){
  	        		location.reload();
  	        });
  		} else {
   			return;
  		}
 	}
 	
 	function showDefaultResponse(response){
 		var rText = response.responseText;
	    if(rText=='default'){
	    	alert("已经是默认布局");
	    }else if(rText=='error'){
	   	 	alert("保存操作记录失败");
	    }else{
	    	window.location.reload();
	    }
 	}
 	
 	//最近发起的流程
	function recentLog(){
		window.location.href = "sysOperateLogIndexAction.action?logType=PERSION_REPORT_CENTER_SORT";
	}
	
	function openReport(title,reportId,type){
		var url = "ireport_rt_index.action?reportId="+reportId+"&reportType="+type;
		addOperateLog(title,reportId,type);
		openDialog(title,url);
	}
	//添加和编辑窗口
	function openDialog(title,url){
	     window.parent.addTab(title,url,''); 
	}
	</script>


	</head>
	<body  style="background: none repeat scroll 0 0 #FFFFFF;">
	
	<div class="report_top">
			<ul class="sub_tab">
				<li>
					<a href="javascript:this.location.reload();" class="sub_tab_selected" style="color: #000">报表中心</a>
				</li>
				<li>
					|
				</li>
				<li>
					<a href="javascript:recentLog()">查看日志</a>
				</li>
			</ul>
			<div style="text-align: right; border: 0px; padding: 3px; font-size: 12px; padding-right: 10px;">
			<a href="javascript:backToDefault();">恢复默认布局</a>
			</div>
		</div>
	
    <div id="columns">
        <s:property value="reportCenterHTML" escapeHtml="false" />
    </div>
    
    <!-- 保存页面布局的TYPE -->
	<input type="hidden" name="saveStrType" id="saveStrType" value="Report_Center_Position">
	<script type="text/javascript" src="iwork_js/workflowCenter/jquery-ui-personalized-1.6rc2.min.js"></script>
    <script type="text/javascript" src="iwork_js/workflowCenter/cookie.jquery.js"></script>
    <script type="text/javascript" src="iwork_js/workflowCenter/inettuts.js"></script>

	</body>
</html>