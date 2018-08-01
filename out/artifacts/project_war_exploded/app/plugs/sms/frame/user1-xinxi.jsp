<%@page pageEncoding="GBK" contentType="text/html; charset=GBK" %>

<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
		<title>短信平台-短信日志查询</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">		
		</head>
<body>
<form  method=post name=frmMain>

<div id="win" style='margin-left:10px;width:700px'></div>
      <table width="700px" border="1"  cellspacing="0" cellpadding="0" bordercolorlight="#CCCCCC" bordercolordark="#FFFFFF">
          <tr> 				
					<td class="actionsoftReportTitle" nowrap><div align="center" > </div></td>
          <td class="actionsoftReportTitle" nowrap><div align="center" >发送部门</div></td>
          <td class="actionsoftReportTitle" nowrap><div align="center">发送人</div></td>
					<td class="actionsoftReportTitle" nowrap><div align="center">剩余数量</div></td>
			    <td class="actionsoftReportTitle" nowrap><div align="center">成功数量</div></td>
			    <td class="actionsoftReportTitle" nowrap><div align="center">未成功数量</div></td>
					<td class="actionsoftReportTitle" nowrap><div align="center">已使用费用(元)</div></td>
					<td class="actionsoftReportTitle" nowrap><div align="center">开通日期</div></td>
					<td class="actionsoftReportTitle" nowrap><div align="center">设置</div></td>
          </tr>
      <%=request.getAttribute("list") %></table>
  
</form>
</body>
<script>
function userdataEdit(form,mycmd,myid){
    form.cmd.value=mycmd;
		form.id.value=myid;
		form.target="AWS_AJAX_OPTER";
		form.submit();
		return false;
}
function savedata(form,mycmd,myid){
        form.cmd.value=mycmd;
		form.id.value=myid;
 		form.target="_self";
 		disableAll(form);
		if(form.typechange.value=='0'&&form.countchange.value!='0'){
			alert("请选择余额增减类型!");
			return false;
		}
		if(form.typechange.value!=0&&form.countchange.value=='0'){
			alert("请填写增减数量!");
			return false;
		}
		var reg=/^[0-9]*[1-9][0-9]*$/;
		if(!reg.test(form.countchange.value)){
			alert("数量只能填写正整数!");
			return false;
		}
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
 
</script>
</html>
