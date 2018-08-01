<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
<base href="<%=basePath%>">
<title>视频列表</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
</head>
<frameset id="video_frame" cols="450,*" marginwidth="0" marginheight="0" frameborder="no" border="0" framespacing="0" rows="*">
  <frame name="video_play" id="video_play" marginwidth="0" marginheight="0" scrolling="no" src="videoPlay.action" noresize>
  <frame name="video_list" id="video_list" marginwidth="0" marginheight="0" scrolling="no" src="videoList.action">
</frameset>
<noframes>
<body>
<p> 此网页使用了框架，但您的浏览器不支持框架。 </p>
</body>
</noframes>
</html>
