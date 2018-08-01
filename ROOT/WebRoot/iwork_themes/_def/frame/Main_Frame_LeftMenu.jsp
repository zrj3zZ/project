<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%String systemid = request.getParameter("systemid"); %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>菜单导航</title>
<link href="/iwork_css/public.css" rel="stylesheet" type="text/css" />
<script src="/iwork_js/tree/ua.js" type="text/javascript"></script>
<script src="/iwork_js/tree/ftiens6.js" type="text/javascript"></script>
<script src="/iwork_js/tree/custom_UI.js" type="text/javascript"></script>
</head>
<body>
<table width="100%" height="100%" border="0" class="treebackground">
  <tr>
    <td valign="top">
		<script>
				var USETEXTLINKS = 1;
				var STARTALLOPEN = 0;
				classPath = "../../../iwork_img/treeimg/";
				ftv2blank = "ftv2blank.gif";
				ftv2doc = "ftv2doc.gif";
				ftv2folderclosed = "ftv2folderclosed.gif";
				ftv2folderopen = "ftv2folderopen.gif"; 
				ftv2lastnode = "ftv2lastnode.gif";
				ftv2link = "ftv2link.gif";
				ftv2mlastnode = "ftv2mlastnode.gif";
				ftv2mnode = "ftv2mnode.gif";
				ftv2node = "ftv2node.gif";
				ftv2plastnode = "ftv2plastnode.gif";
				ftv2pnode = "ftv2pnode.gif";
				ftv2vertline = "tv2vertline.gif";
				basefrm = "mainFrame";
		</script>
		<s:property value="treescript"  escapeHtml="false"/>
</td>
  </tr>
</table>
</body>
</html>
<s:if test="systemid==null">
	<script>
		window.parent.frames["tree_but"].location="control.jsp?status=0";
	</script>
</s:if>
<s:else>
	<script>
		parent.frames["tree_but"].show_left()
		//show_left();
	</script>
</s:else>
