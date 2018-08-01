<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>My JSP 'video_list.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<link rel="stylesheet" type="text/css" href="../../../iwork_css/plugs/video.css">

<style>
a:link, a:visited {
	text-decoration:none;
}
a:hover {
	color:#000000;
}
</style>
<style type="text/css">
<!--
-->
</style>

<script type="text/javascript">
function getPageList(pageNum){
	$.post('videoList.action',{pageNum:pageNum},function(){});
}


function getList(pageNum){
	
	document.actionForm.submit();
	
}

function openVideo(){
	%.post('videoPlay.action',{pageNum:pageNum});	
}


</script>
</head>
<body>
<s:form name="actionForm" id="actionForm" action="videoList.action"  theme="simple">
<div class="tvlist_body">
<div class="tvlist_right">
<table width="100%" height="326" border="0" align="center" cellpadding="5" cellspacing="0" style="margin-top:0px">
  <tr>
    <td height="290" align="center" valign="top"><table width="100%" border="1" align="center" cellpadding="3" cellspacing="1" bordercolor="#BDCCEC" style="border-collapse:collapse; margin-top:5px;">
        <tr class=rowTitle>
          <td width="6%" align="center" nowrap bgcolor="#DBE3F4">序号</td>
          <td width="44%" align="center" nowrap bgcolor="#DBE3F4">视频名称</td>
          <td width="10%" align="center" nowrap bgcolor="#DBE3F4">播放</td>
          <!--		<td width="10%" align="center" nowrap bgcolor="#DBE3F4">下载</td>-->
          <td width="10%" align="center" nowrap bgcolor="#DBE3F4">上传人</td>
          <td width="20%" align="center" nowrap bgcolor="#DBE3F4">上传时间</td>
        </tr>
        <s:property value="list" escapeHtml="false" />
      </table></td>
  </tr> 
  <tr>
    <td align="center" class="main_content"><s:property value="pageNoList" escapeHtml="false" /></td>
  </tr>
</table>
<s:hidden name="pageNum"></s:hidden>
</div>
</div>
</s:form>
</body>
</html>
