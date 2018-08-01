<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>短信日志查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">		
		<script type="text/javascript" src="iwork_js/calendar.js"></script>	
		</head>
		<body>
<form action="./login.wf" method=post name=frmMain>
  <table width="100%" border="0" cellspacing="0" cellpadding="2" bordercolorlight="#4A9FB5" bordercolordark="#FFFFFF">
    <tr> 
      <td  valign="top">
      	<table  border="1"  cellspacing="0" cellpadding="0" bordercolorlight="#CCCCCC" bordercolordark="#FFFFFF">
          <tr>  
						<td class="actionsoftReportTitle" align="center"  nowrap>序号</td>
						<td class="actionsoftReportTitle" align="center"  nowrap>类别</td>
            <td class="actionsoftReportTitle" align="center"  nowrap>时间</td>
						<td class="actionsoftReportTitle" align="center"  nowrap>操作者</td>
			      <td class="actionsoftReportTitle" align="center">详情</td>
           </tr>
      		<%=request.getAttribute("loglist") %> 
      	</table>
      </td>
    </tr>
  </table>
  
</form>
</body>
<script>
function msgManagementUpdate(form,mycmd){
	var t = document.getElementsByName("chk");
	for(var i=0;i<t.length;i++){   
	if(t[i].checked){
	document.frmMain.tvalue.value+=t[i].value+" ";	
		}
	}
	if(document.frmMain.tvalue.value==""||document.frmMain.tvalue.value==null){
	alert("请选择要修改的条目！");
	return false;
	}
	if(form.updatechanel.value==""&&form.updatestatus.value==""){
	  alert("请选择要修改的状态或通道！");
	  return false;
	}
	form.cmd.value=mycmd; 
	form.submit();	
	return false;

}
function gotoPage(form,mycmd,pageNow){ 
 	form.pageNow.value=pageNow;
 	form.target='_self';
 	return execMyCommand2(frmMain,mycmd,'_self');
 }
 function execMyCommand2(form,mycmd,targetName){
	form.cmd.value=mycmd;
	form.target=targetName;
  form.submit(); 
	return false;
 }
 if(frmMain.returnvalueupdate.value != "" & frmMain.returnvalueupdate.value != "a"){
  	alert(frmMain.returnvalueupdate.value);
   
  }
</script>
</html>