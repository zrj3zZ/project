<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<base href="<%=basePath%>">
<title>My JSP 'video_play.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="../../../iwork_css/plugs/video.css" href="tvlist.css" rel="stylesheet" type="text/css" >

<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
</head>
<body>
<div class="tvlist_left">
  <div class="video">
    <s:property value="list" escapeHtml="false"/>
  </div>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td height="139" ><div class="tv_list_top"></div></td>
    </tr>
    <tr>
      <td height="237" ><div class="tv_list_left"></div>
        <div class="tv_list_right"></div></td>
    </tr>
    <tr>
      <td ><div class="tv_list_bottom" id="name"><s:property value="name" escapeHtml="false"/></div></td>
    </tr>
  </table>
</div>
<s:hidden name="iworkCmsVideo.id"></s:hidden>
<s:hidden name="iworkCmsVideo.videofile"></s:hidden>
<s:hidden name="iworkCmsVideo.picfile"></s:hidden>
<s:hidden name="iworkCmsVideo.title"></s:hidden>
</body>
</html>
