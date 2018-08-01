<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="head_bg"> 
	<div class="head"> 
	  <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
	  	<tr>
	  	<td rowspan="2" style="width:100px;height:100%">
	  	  <div class="logo" onclick="showMainPage();"><img src="iwork_layout/bpm2013/img/logo.png" height="35"></div>
	  	</td>
	  	<td></td>
	  	<td class="head_fav_bar" rowspan="2" >
	  	 	<div class="head_font_top">
			    <a href="#"  id="createCenter"  target="_parent">发起中心</a> 
			    | <a href="#"  id="reportCenter"   target="_parent">报表中心</a> 
			    | <a href="#"  id="eaglesSearch"  target="_parent">鹰眼检索</a> 
			    | <a href="#"  id="userOnLine" onclick="showOnline();"  target="_parent">在线用户</a> 
			    | <a href="#"  id="personConfig"  target="_parent">设置</a> 
			    | <a href="#"  id="loginOut"  target="_parent">退出平台</a></div>
			  </div>
	  	</td></tr>
	  	<tr>
	  		<td class="head_nav_menu">
	  		<table>
	  		<tr>
			  		<s:iterator value="systemList">
			  		<td>
			  			<div class="menuItem" onclick="showContent(<s:property value="id"/>)">
			  			<div class="menuImg"><img onerror="javascript:this.src='iwork_img/menulogo_baibaoxiang.gif'" src="<s:property value="sysIcon"/>"></div>
			  			<div class="menutitle"><s:property value="sysName"/></div>
			  			</div>
			  		</td>
			  	
			  	</s:iterator>
			  </tr>
			  </table>
	  		</td>
	  	</tr>
	  </table>
	</div>
</div>