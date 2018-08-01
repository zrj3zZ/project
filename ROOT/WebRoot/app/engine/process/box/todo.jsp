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
<link rel="stylesheet" type="text/css" href="iwork_css/process_box.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/process/process_box.js"></script> 
<script src="iwork_js/process/process_desk.js"></script> 
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script>
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
		    		 art.dialog.tips("任务领取成功",5);
		    		 reloadPage();
		    		 
		    	}else{
		    		art.dialog.tips("任务领取失败(错误编号:ERROR-00001)",5)
		    	}
		  });  
}
function reloadPage(){
		    window.location.reload();
		}
</script>
</head>
<body  class="easyui-layout" >
<div region="north" border="false" split="false" class="processBoxTitle" id="layoutNorth">
	<table width="100%">
		<tr>
			<td style="vertical-align:middle;"><img src="iwork_img/menulogo_huiyishenqin.gif" alt="" /><s:property value="processTitle" escapeHtml="false"/></td>
			<td style="vertical-align:top;">
			</td>
		</tr>
	</table>
</div>
<div region="west" border="false"  id="layoutLeft" class="leftDiv">
	<div class="leftTools">
		<a href="###" onClick="createProcess('<s:property value="actDefId" escapeHtml="false"/>')" class="easyui-linkbutton" plain="false" iconCls="icon-add">&nbsp;新　建&nbsp;</a>
	</div>
    <hr style="width:180px; height:2px;border:none;border-top:2px solid #99ccff;"/>
    <div>
    	<s:property value="tabHtml" escapeHtml="false"/>
    </div>
   <div class="memoTip">
   	  流程说明:
     </div>
    <div class="memo">
    	<s:property value="processMemo" escapeHtml="false"/>
    </div>
</div>
	<div region="center" border="false"  id="layoutCenter" style="overflow:auto">
<div class="wr_table" >
  <table  border="0" cellspacing="0" cellpadding="0">
    <thead>
      <tr class="wr-table-hd-inner" >
        <td class="row3"><s:text name="process.desk.title.num"/></td>
        <td class="row1"><s:text name="process.desk.title.from"/></td>
        <td colspan="2" ><s:text name="process.desk.title.tasktitle"/></td>
        <td class="row2"><s:text name="process.desk.title.recivetime"/></td>
        <td class="row1"><s:text name="process.desk.title.timeline"/></td>
      </tr>
      </thead>
   <s:iterator value="tasklist" status="st">
    <tbody>
    	<s:if test="isRead==1"> 
    		<s:if test="taskType=='Notice'">
		    		<tr class="table-tr-notice" id="notice_<s:property value="taskId"/>"> 
			        <td class="row3"><s:property value="#st.index+1"/></td>
			        <td class="row1"><s:property value="ownerName"/></a></td>
			        <td class="task_title" onclick="openNoticePage();"><s:property value="title" escapeHtml="false"/></td>
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
        <td class="row1">b<s:property value="longTime"/></td>
      </tr>
      </s:else>
      
      <tr class="wr-table-td-inner summary" id="content<s:property value="taskId"/>">
        <td class="row3 ">&nbsp;</td>
        <td class="row1">&nbsp;</td>
        <td colspan="4" >t<div class="summary_main"><s:property value="content"/></div></td>
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
