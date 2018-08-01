<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>在线考试</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>
<frameset rows="38,*" border="0" frameBorder = "0" framespacing="0" bordercolor="#F4F4F4" id="d1">
		<frame style="border:1px solid #efefef; " src="topicTop.action" name="online_test_top"  scrolling="auto" id="online_test_top" noresize="noresize"/>
	<frameset cols="200,*" border="0" frameBorder = "0" framespacing="0" bordercolor="#F4F4F4" id="d2">
		  <frame style="border:1px solid #efefef" src="topicList.action" name="online_test_left"  scrolling="auto" id="online_test_left" noresize="noresize"/>
		  <frame style="border:1px solid #efefef;border-left-style: none;" src="topicContent.action" name="online_test_right"  scrolling="auto" id="online_test_right" noresize="noresize"/>
	</frameset>
</html> 