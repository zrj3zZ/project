<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>IWORK综合办公管理系统</title>
<link href="../iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../iwork_skins/_def/js/custom_UI.js"></script>
<script type="text/javascript" src="../iwork_skins/_def/js/iframeH.js"></script>
</head> 

<body>
<table id="first" cellpadding="0" cellspacing="0" class="topbackground">
  <tr>
    <td class="daterow">当前日期：<s:property value="dateStr"  escapeHtml="false"/></td>
    <td class="topright" ><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="logohome"><a href="#">平台首页</a></td>
        <td class="logoexit"><a href="../logout.action" target="_parent">退出平台</a></td>
      </tr>
    </table></td>
  </tr>
</table>
<table id="second" cellpadding="0" cellspacing="0" class="menubackground">
  <tr>
  <td class="logo">&nbsp;</td>
    <td align="right"><table border="0">
      <tr>
	      <s:iterator value="systemList">
	      	 <td class="tdmenu">
	      	 	<a href='<s:url action="sysNavMenuList" ><s:param name="systemid" value="id" /></s:url>' target='<s:property value="sysTarget"/>'><img src='<s:property value="sysIcon"/>' width="40" height="32" border="0" /></a>
	      	 </td>
	      </s:iterator>
      </tr>
      <tr>
	       <s:iterator value="systemList">
	      	 	<td class="tdmenutext"><a href='<s:url action="sysNavMenuList" ><s:param name="systemid" value="id" /></s:url>' target='<s:property value="sysTarget"/>'><s:property value="sysName"/></a></td>
	      </s:iterator>
      </tr>
    </table></td>
  </tr>
</table>
<table cellpadding="0" cellspacing="0" class="userbackground">
  <tr>
    <td><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="userleft"><span class="user"></span>用户：<s:property value="currentUserStr"  escapeHtml="false"/></td>
        <td class="logomail">(<span class="ts"><a href="#">2</a></span>)</td>
        <td class="logomessenger">(<span class="ts"><a href="#">5</a></span>)</td>
        <td class="logotodo">(<span class="ts"><a target='mainFrame' href="<s:url action="sysmsg_list" ></s:url>"><s:property value="unreadCount"  escapeHtml="false"/></a></span>)</td>
        <td class="logodayarr">(0)</td>
      </tr>
    </table></td>
    <td ><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td>我要找人</td>
        <td><input name="textfield" type="text" class="search_input" /></td>
        <td><input name="Submit" type="button" class="btn_search" id="Submit" value="" /></td>
      </tr>
    </table></td>
    <td width="100" nowrap="nowrap"> 
    <div id="div_small"  style="display:none;">
			<a href="#" onClick="zoom();initFrameHeightSm();"><img src="../../../iwork_img/but_small.gif" width="75" height="21" border="0" /></a>
			</div>
		<div id="div_navigator" style="display:block;z-index: 0;left:1;top:100;">
			<a href="#" onClick="zoom();initFrameHeightBig();"><img src="../../../iwork_img/but_big.gif" width="75" height="21" border="0" /></a>
			</div>
    </td>
  </tr>
</table>

</body>
</html>
