<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>流程处理中心</title>
<link href="iwork_css/process_center.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
<script src="iwork_js/process/process_desk.js"></script> 
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script> 
<script>
jQuery(document).bind('keydown',function (evt){		
	if(evt.ctrlKey&&evt.shiftKey){
		return false;
	}
	else if(event.keyCode==13){ //回车 /查询操作
	
	}
}); //快捷键

$(function(){
		$(".list-selected").click(
				function(){
					$(".list-selected").hide();
					$(".icon").show();
					window.location="processManagement_batch_index.action";
				});	   
	})
	
	function showInfo(taskId){
	if($("#more"+taskId).hasClass("more")){
		$("#more"+taskId).removeClass("more"); 
		$("#more"+taskId).addClass("js");
				$.post('process_desk_getsummary.action',{taskId:taskId},function(msg)
				 { 
					$("#content"+taskId).find(".summary_main").html(msg);
				 }); 
	}else{
		$("#more"+taskId).removeClass("js"); 
		$("#more"+taskId).addClass("more");
	}
	$("#content"+taskId).animate({height: 'toggle', opacity: 'toggle'});
}
function claim(taskId,userId){
	document.frmMain.taskId.value = taskId;
	document.frmMain.userId.value = userId;
	  $.post('processManagement_claimTask.action',$("#frmMain").serialize(),function(data)
            {
		    	if(data=='success'){
		    		 lhgdialog.tips("任务领取成功",5);
		    		 reloadPage();
		    		 
		    	}else{
		    		lhgdialog.tips("任务领取失败(错误编号:ERROR-00001)",5)
		    	}
		  });  
}
function reloadPage(){
		    window.location.reload();
		}
</script>
</head>
<body  class="easyui-layout" >
<div region="north" border="false" split="false"  style="height:35px;" id="layoutNorth">
<div class="process_header">
  <div class="process_head_tab_box"> 
 	 <a class="process_head_tab_active" title="显示所有待审批流程未读的抄送任务或通知" href="process_desk_index.action">待办流程</a>
    <!-- <a class="process_head_tab"  title="显示当前用户已办理，但未最终归档的流程任务"  href="process_desk_finish.action">已办跟踪</a>  
     <a class="process_head_tab"  title="显示所有接收的抄送及流程通知任务"  href="process_desk_notice.action"> 通知/抄送</a>
      <a class="process_head_tab"  title="显示当前用户已办理，所有办理过的任务历史及审批意见"  href="process_desk_history.action"> 办理日志</a>
       <a class="process_head_tab"  title="显示所有当前用户发起的流程单据跟踪查询"  href="process_desk_createlog.action">我发起的流程</a> -->
      
    <!--    <a class="process_head_tab" href="process_desk_notice.action"> 已归档流程</a>-->
      </div>
      <div style="padding-top:3px;">
      </div>
  <div class="process_head_tab_right">
    <form id="search" class="search">
      <input name="searchKey" id="searchKey"  class="txt" type="text">
      </input>
      <button class="search-submit" data-ca="search-btn"></button>
    </form>
  </div>
  <div class="switch-view">
	<a href="#" class="list-selected"  title="切换到缩略图模式"></a>
		<a href="#" class="icon" title="切换到列表模式" style="display:none;border:0px;"></a>
 </div>
</div>
</div>
	<div region="center" border="false"  id="layoutCenter" style="overflow:auto">
<div class="wr_table" >
  <table  border="0" cellspacing="0" cellpadding="0">
    <thead>
      <tr class="wr-table-hd-inner" >
        <td class="row3">序号</td>
        <td class="row1">来自</td>
        <td colspan="2" >任务标题</td>
        <td class="row2">我接收时间</td>
        <td class="row1">流程历时</td>
      </tr>
      </thead>
   <s:iterator value="tasklist" status="st">
    <tbody>
    	<s:if test="isRead==1"> 
    		<s:if test="taskType=='Notice'">
		    		<tr class="table-tr-notice" id="notice_<s:property value="taskId"/>"> 
			        <td class="row3"><s:property value="#st.index+1"/></td>
			        <td class="row1"><s:property value="ownerName"/></a></td>
			        <td class="task_title" onclick="openNoticePage('<s:property value="title"/>','<s:property value="actDefId"/>',<s:property value="instanceId"/>,<s:property value="excutionId"/>,<s:property value="taskId"/>,<s:property value="dataid"/>);">【抄送】<s:property value="title"/></td>
			        <td><ul class="inline-operatebar">
			        	<s:property value="operate" escapeHtml="false"/> 
			          </ul></td>
			        <td class="row2"><s:property value="%{getText('{0,date,yyyy-MM-dd HH:mm  }',{createDate})}"/></td>
			        <td class="row1"><s:property value="longTime"/></td>
			      </tr> 
    		</s:if>
    		<s:else>
		    		<tr class="wr-table-tr-row" id="task_<s:property value="taskId"/>"> 
			        <td class="row3"><s:property value="#st.index+1"/></td>
			        <td class="row1"><s:property value="ownerName"/></a></td>
			        <td class="task_title" onclick="openTaskPage('<s:property value="title"/>','<s:property value="actDefId"/>',<s:property value="instanceId"/>,<s:property value="excutionId"/>,<s:property value="taskId"/>);"><font class="black_font"><s:property value="title"/></font></td>
			        <td><ul class="inline-operatebar">
			        	<s:property value="operate" escapeHtml="false"/> 
			          </ul></td>
			        <td class="row2"><s:property value="%{getText('{0,date,yyyy-MM-dd HH:mm  }',{createDate})}"/></td>
			        <td class="row1"><s:property value="longTime"/></td>
			      </tr> 
    		</s:else>
	       
      </s:if>
      <s:else>
      <tr class="wr-table-td-inner"  id="task_<s:property value="taskId"/>"> 
        <td class="row3 "><s:property value="#st.index+1"/><label>
          </label></td> 
        <td class="row1 "> <s:property value="ownerName"/></a></td>
        <td class="task_title" onclick="openTaskPage('<s:property value="title"/>','<s:property value="actDefId"/>',<s:property value="instanceId"/>,<s:property value="excutionId"/>,<s:property value="taskId"/>);"><font class="black_font"><s:property value="title"/></font></td>
        <td><ul class="inline-operatebar">
        	<s:property value="operate" escapeHtml="false"/> 
          </ul></td>
        <td class="row2"><s:property value="%{getText('{0,date,yyyy-MM-dd HH:mm }',{createDate})}"/></td>
        <td class="row1"><s:property value="longTime"/></td>
      </tr>
      </s:else>
      
      <tr class="wr-table-td-inner summary" id="content<s:property value="taskId"/>">
        <td class="row3 ">&nbsp;</td>
        <td class="row1">&nbsp;</td>
        <td colspan="4" ><div class="summary_main"><s:property value="content"/></div></td>
      </tr>
 </tbody>
   </s:iterator>
  </table>
   <form name="frmMain" id="frmMain" style="display:none">
					<input type="hidden" name="taskId" id="taskId">
					<input type="hidden" name="userId" id="userId">
					<input type="hidden"  value = "test_parent" name="test_parent_userId" id="test_parent_userId">
				</form>
</div>
</div>

</body>
</html>
<script>
showTaskCount();
</script>