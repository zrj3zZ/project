<%@page pageEncoding="GBK" contentType="text/html; charset=GBK" %>

<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
		<title>����ƽ̨-������־��ѯ</title>
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
          <td class="actionsoftReportTitle" nowrap><div align="center" >���Ͳ���</div></td>
          <td class="actionsoftReportTitle" nowrap><div align="center">������</div></td>
					<td class="actionsoftReportTitle" nowrap><div align="center">ʣ������</div></td>
			    <td class="actionsoftReportTitle" nowrap><div align="center">�ɹ�����</div></td>
			    <td class="actionsoftReportTitle" nowrap><div align="center">δ�ɹ�����</div></td>
					<td class="actionsoftReportTitle" nowrap><div align="center">��ʹ�÷���(Ԫ)</div></td>
					<td class="actionsoftReportTitle" nowrap><div align="center">��ͨ����</div></td>
					<td class="actionsoftReportTitle" nowrap><div align="center">����</div></td>
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
			alert("��ѡ�������������!");
			return false;
		}
		if(form.typechange.value!=0&&form.countchange.value=='0'){
			alert("����д��������!");
			return false;
		}
		var reg=/^[0-9]*[1-9][0-9]*$/;
		if(!reg.test(form.countchange.value)){
			alert("����ֻ����д������!");
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
