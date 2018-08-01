<!DOCTYPE html>  
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

                <!--左边 -->
<div id="col1" class="st-index-left">
<div class="left-wrap">
<!--个人信息-->
<div class="mod-person"> 
<a href="javascript:redirectUrl('syspersion_info.action')" class="face"><img src="iwork_file/USER_PHOTO/<s:property value="userid"/>.jpg" onerror="this.src='iwork_img/big.jpg'" width="144" /></a>
<a href="" class="name"><span><s:property value="currentUserStr"/></span></a>
<a href="" class="namebg"></a>
</div>
<!--左导航菜单-->
<div class="mod-sub-nav">
<!--  选中判断 -->
<ul class="basic-list"> 
	<li class="current" ><a href="mainAction.action" class="lev"><i class="aicon-home"></i>首页</a></li>
    <li  ><a href="javascript:selectLeftMenuBar('sys_message.action')" class="lev"><i class="aicon-email"></i>消息</a></li>
	<li  ><a href="https://itd.qimingdao.com/core/Collection/index" class="lev"><i class="aicon-star"></i>收藏</a></li>
</ul>
</div>
<!--应用-->
<div class="mod-app">
<fieldset class="P-line"><legend class="P-txt">应用</legend></fieldset>
<ul class="app-list">
<s:iterator value="systemList">
<li>
    <a href="" class="lev" event-args="<s:property value="id"/>" id='app_system_<s:property value="id"/>'>
    	<div class="icon-pic icon-app-task"><img src="iwork_img/menulogo_shangwu.gif" width='24'><s:property value="sysName"/><span id="load_icon<s:property value="id"/>"></span></div>
    </a>
     <ul  id="sub_menu_<s:property value="id"/>" style="display:none" class="app-sub-list"></ul>
</li>
</s:iterator>
<li ><a href="" class="lev"><i class="icon-pic ico-appadd"></i>添加</a></li>
</ul>
</div>
</div>
</div>
