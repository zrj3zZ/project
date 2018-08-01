<!DOCTYPE html>  
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh">  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>挂牌企业规范管理系统</title>
	<s:include value="link.jsp"> </s:include>

	
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/jquery.form.js" charset="gb2312"></script>
    <link href="iwork_css/message/sysmsgpage.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="iwork_js/message/sysmsgpage.js"></script>
	<script type="text/javascript" src="iwork_js/message/sysmsglist.js"></script>
</head>
<body  >
<s:include value="topbar.jsp">
   </s:include> 
<div id="page-wrap">
        <div id="feedback" class="feedback-fixed">
            <a href="javascript:void(0)"></a>
        </div>
        <div id="main-wrap">
            <div id="st-index-grid" class="st-grid">
            <!-- 左侧菜单  --> 
				<s:include value="leftmenu.jsp">
				   </s:include>
                <div id="col8" class="st-section" >
                    <div id="col5" class="st-index-main"  style="width:830px">
                    <div class="border boxShadow extend-foot minh" style="padding-top:15px">
                        
                    <!--feednav-->
                    <div class="feed-nav mt15">
                    <!--tab menu-->
                    <div class="tab-menu tab-animate">
                        <div class="feed-group">
                                                <i class="ico-circle-arrow-down" event-node='feed_tab_btn' title="展开"></i>
                                                </div>
                         <ul id="column_tab">
                          </ul>
                        <div class="tab-animate-block"></div>
                    </div>
                    </div>
                    <!--资讯列表-->
                    <div id="feed-lists"  style="padding-left:15px;padding-right:10px;">
					</div>
					<s:property value='total'/>
					<s:if test="$('#total').val()==''">
	暂无系统消息
</s:if><s:else>
<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
</s:else>
</div>
			<s:hidden name="total" id="total"></s:hidden>
			<s:hidden name="pageNo" id="pageNo"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
					</div>
					</div>
					</div>
<!--=> ft <=-->
<div id="footer">
    <div class="client">
        
    </div>
    <div class="copyright">
        <p>Copyright(&copy) 2014 挂牌企业规范管理系统</p>
    </div>
</div>
<!--=> ft End <=-->
</body>
</html>