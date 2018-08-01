<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<link href="iwork_css/dtree.css" rel="stylesheet" type="text/css" />
<script src="iwork_js/dtree/dtree.js" type="text/javascript"></script>
</head>
<%
	String fieldname = request.getParameter("fieldname");
	String fieldid = request.getParameter("fieldid");
 %>
<script>
function selectOne(){
	var selectValue="";
	for ( var i=0; i < editForm.elements.length; i++ ){
		if (( true==editForm.elements[i].checked) && (editForm.elements[i].type == 'radio' )) 
		 selectValue=editForm.elements[i].value;
	}
	try{
		var idValue=selectValue.substring(0,selectValue.indexOf("|"));
		var nameValue=selectValue.substring(selectValue.indexOf("|")+1);
		//alert(window.dialogArguments.document.editForm.parentdepartmentid.value);
		<%
			if(!fieldname.equals("")){
			//	fieldname = fieldname.replace(".","\\.");
		%>
			var form = window.dialogArguments.document.editForm;
				for (var i = 0; i<form.elements.length;i++){
					if(form.elements[i].name=='<%=fieldname%>'){
						form.elements[i].value = nameValue;
					}
					
				}
		<%}%>
		<%
			if(!fieldid.equals("")){
		//	fieldid = fieldid.replace(".","\\.");
		%>
			//var  fieldid = "";
			var form = window.dialogArguments.document.editForm;
				for (var i = 0; i<form.elements.length;i++){
					if(form.elements[i].name=='<%=fieldid%>'){
						form.elements[i].value = idValue;
					}
					
				}
		<%}%>
		
	}catch(e){
		alert(e.message);
		return false;
	}
	window.close();
 }
</script>
<body bgcolor="#d0d0d0">
<s:form name="editForm"  validate="true">
<table cellpadding="0" cellspacing="0" class="jgtd1">
<tr>
<td height="25"><table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="jgleft">&nbsp;</td>
    <td class="jgbg"><img  src="iwork_img/process_file.gif" border="0">&nbsp;&nbsp;&nbsp;部门组织结构&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td class="jgright">&nbsp;&nbsp;</td>
  </tr>
</table></td> 
  </tr>
	<tr>
	<td  valign="top" >
	<table cellpadding="0" cellspacing="0" class="jgtd2">
      <tr>
        <td valign="top">
		  <table width="100%" height="100%" border="0">
			  <tr>
			    <td valign="top" align="left">
			    <a href="javascript: d.openAll();"><b>&nbsp;展开&nbsp;</b></a> | <a href="javascript: d.closeAll();"><b>&nbsp;收起&nbsp;</b></a> | <a href="javascript:selectOne();"><b>&nbsp;确认选择&nbsp;</b></a> | <a href="javascript:window.close();"><b>关闭窗口</b></a>
			</td>
			  </tr>
			  <tr>
			    <td valign="top" align="left">
			    <div align="left" style="OVERFLOW: auto; WIDTH: 220px; HEIGHT:350">
						<s:property value="navTree"  escapeHtml="false"/>
				</div>
			</td>
			  </tr>
</table>
		</td>
      </tr>
    </table></td>
	
  </tr>
</table>
</s:form>
</body>
</html>
