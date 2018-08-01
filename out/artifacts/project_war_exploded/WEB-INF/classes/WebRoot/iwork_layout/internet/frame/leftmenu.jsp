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
<a href="javascript:redirectLeftView('syspersion_info.action','other')" class="face"><img src="iwork_file/USER_PHOTO/<s:property value="userid"/>.jpg" onerror="this.src='iwork_img/big.jpg'" width="144" /></a>
<a href="" class="name"><span><s:property value="currentUserStr"/></span></a>
<a href="" class="namebg"></a>
</div>
<s:property value="leftMenuHtml" escapeHtml="false"/>
</div>
</div>
<s:hidden name="menukey" id="menukey" ></s:hidden>
