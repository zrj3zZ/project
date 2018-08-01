<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="GBK" contentType="text/html; charset=GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script language="javascript" src="iwork_js/tree/TreeView.js"></script>
</head>

<body bgcolor="#F3F3F3">
<table cellpadding="0" cellspacing="0" class="jgtd1">
	<tr>
	<td  valign="top" ><table cellpadding="0" cellspacing="0" class="jgtd2" height=100%>
      <tr>
        <td valign="top">
		
		  <table width="100%" height="100%" border="0">
  <tr> 
    <td valign="top">
    <div id=treeviewarea ></div>
          <s:property value="kmEngerpriseTree"  escapeHtml="false"/>
</td>
  </tr>
</table>
		</td>	
      </tr>
    </table></td>
	
  </tr>
</table>

</body>
</html>
