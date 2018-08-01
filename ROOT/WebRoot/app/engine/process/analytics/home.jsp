<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>金鹰BPM流程绩效分析</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/analytics/home_page.css" rel="stylesheet" type="text/css" /> 
	<link href="iwork_css/analytics/menu.css" rel="stylesheet" type="text/css" />
	<link href="admin/css/dashboard.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script>
	<script type="text/javascript" src="iwork_js/analytics_index.js"></script>
	
	<style >
	html {
}
		.title{
			margin:10px;
			font-size:22px;
			font-family:黑体;
			font-weight:bold;
		}
		.mainDiv{ 
			width:1000px;
			height:100%;
			margin-left:auto;
			margin-right:auto;
		}
	</style>    
</head>
<body style="overflow-y:hidden">
<div class="home_navigation_bg">
  <div class="home_navigation">
    <div class="home_navigation_logo"><img style="height:35px" src="iwork_img/analytics/analytics.png"></div>
    <div class="home_navigation_tab"> 
    	<s:property value="toolbar" escapeHtml="false"/>
    </div>
    <div class="home_navigation_icon">
      <ul id="jsddm">
        <li><a href="#">操   作</a>
          <ul>
            <li><a href="#">创建面板</a></li>
            <li><a href="#">帮助</a></li>
            <li><a href="#">论坛</a></li>
            <li><a href="#">反馈</a></li>
            <li><a href="#">退出</a></li>
          </ul>
        </li> 
      </ul>
      <span class="icon_red_point" onclick="$('#sysQtip').show();return false;"></span> </div>
    <div class="homepage_head_tab_right">
      <div id="search" class="search">
        <input class="txt" type="text">
        </input>
        <button class="search-submit" ></button>
        <div class="search_xiala">
          <ul class="current_father">
            <li class="current"><a href="#">查找员工</a></li>
            <li><a href="#">搜索流程</a></li>
          </ul>
        </div>
      </div>
    </div>
    <div class="gn_tips" style="top: 43px;	left: 860px; display:none" id="sysQtip"> <a href="#" class="W_ico12 icon_close" onclick="$('#sysQtip').hide();return false;"></a>
      <ul class="tips_list">
        <li id="dblc">待办流程，<a target="_top" href="#"><span id="tiao1">2</span>条</a></li>
        <li id="wdxx">未读消息，<a target="_top" href="#"><span id="tiao2">3</span>条</a></li>
      </ul>
    </div>
  </div>
</div>
<div class="home_body">
	    <div id="columns">
    <ul id="column_1" class="column">  
<li class="widget widget color-yellow" id="widget_memoryMonitor">  
                <div class="widget-head">
                    <h3>三项费用出纳审核节点</h3>
                </div>
                <div class="widget-content" style="height:200px" >
           </div>
            </li>
<li class="widget widget color-yellow" id="widget_processTaskMonitor">  
                <div class="widget-head">
                    <h3>流程任务监控</h3>
                </div>
                <div class="widget-content" style="height:300px" >            </div>
            </li>
<li class="widget widget color-yellow" id="widget_userMonitor">  
                <div class="widget-head">
                    <h3>组织及用户信息监控</h3>
                </div>
                <div class="widget-content" style="height:80px" >              </div>
            </li>
 </ul>
<ul id="column_2" class="column">  
<li class="widget widget color-yellow" id="widget_serverMemoryMonitor">  
                <div class="widget-head">
                    <h3>服务器内存实时监控</h3>
                </div>
                <div class="widget-content" style="height:200px" >             </div>
            </li>
<li class="widget widget color-yellow" id="widget_serverDBPoolMonitor">  
                <div class="widget-head">
                    <h3>数据库连接池实时监控</h3>
                </div>
                <div class="widget-content" style="height:200px" >             </div>
            </li>
<li class="widget widget color-yellow" id="widget_messageBoard">  
                <div class="widget-head">
                    <h3>系统消息监控</h3>
                </div>
                <div class="widget-content" >             </div>
            </li>
 </ul>
<ul id="column_3" class="column">  
<li class="widget widget color-yellow" id="widget_loginCount">  
                <div class="widget-head">
                    <h3>最近15天登录次数</h3>
                </div>
                <div class="widget-content" style="height:350px" >        </div>
            </li>
<li class="widget widget color-yellow" id="widget_modleMonitor">  
                <div class="widget-head">
                    <h3>系统模型信息监控</h3>
                </div>
                <div class="widget-content" >              </div>
            </li>
<li class="widget widget color-yellow" id="widget_userOnline">  
                <div class="widget-head">
                    <h3>在线用户列表</h3>
                </div>
                <div class="widget-content" >
<div style="padding:5px;text-align:left;padding-left:10px;font-size:20px;border-bottom:1px solid #efefef">在线用户数为<span style="font-size:30px;color:red">1</span>人</div><div  style="line-height:18px;margin:3px;font-size:10px;color:#666">超级管理员[KS信息技术部]</div>
                </div>
            </li>
 </ul>

    </div>
</div>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.2.min.js"   ></script>
    <script type="text/javascript" src="iwork_js/portal/jquery-ui-personalized-1.6rc2.min.js"></script>
    <script type="text/javascript" src="iwork_js/portal/inettuts.js"></script>
</body>
</html>
	