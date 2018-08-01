<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>  
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><s:property value="systemTitle"/></title>   
	<link rel="stylesheet" type="text/css" href="iwork_layout/blue/css/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_layout/blue/css/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css" /> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="admin/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_layout/blue/js/index.js"></script>  
	<script type="text/javascript" src="iwork_layout/blue/js/extends.js"></script>  
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>	
	  <script type="text/javascript"> 
	  $(function(){   
			   $('#mainFrameTab').tabs({
			    });
	   });
         
        </script>
        <style type="text/css">
			.nav {
			    font-family:Trebuchet MS,sans-serif;
			    font-size:0;
				width:100%;
			    background-image:url(iwork_layout/blue/img/background.png);
			    overflow:hidden
			}
			.top_head{
				border-bottom:0px solid #777;
				vertical-align:bottom;
				background-image:url(iwork_layout/blue/img/logo.png);
				background-repeat:no-repeat;
				height:95px;
				background-position:10px 15px
			}

			.login_head_nav {
				float:right;
				color:#003399;
				font-family:微软雅黑;
				font-size:14px;
				margin-right:30px;
			} 
			.login_head_nav ul {
				display:block;
			}
			.login_head_nav li {
				float:left;
				margin:0 10px;
				list-style-type:none;
			}
			.login_head_nav a {
				font-family:微软雅黑;
				font-size:14px;
				color:#003399;
			}
			.login_head_nav a:hover {
				font-family:微软雅黑;
				font-size:14px;
				color:#003399;
				text-decoration:none;
				background:#efefef;
			}
			.search input {
				z-index: 1;
				float: right;
				position: relative;
				width: 150px;
				padding-top: 3px;
				padding-bottom: 2px;
				padding-left: 10px;
				padding-right: 22px;
				border-radius: 13px;
				background: #fff;
			-webkit-transition: all ease-out .2s;
			transition: all ease-out .2s;
			}
			.txt {
				border: 1px solid #E8E4D8;
				border-radius: 3px 3px 3px 3px;
				height: 19px;
				line-height: 19px !important;
				padding: 3px 4px 2px;
			}
			.search {
				top: 7px;
				position: relative;
				overflow: hidden;
				width: 210px;
				height: 30px;
				margin: 0 0 0px 0px;
			}
			.search button {
				z-index: 2;
				width: 16px;
				height: 16px;
				position: absolute;
				border:0px;
				cursor:pointer
			}
			.search-submit {
				position: absolute;
				top: 4px;
				right: 6px;
				background:url(../iwork_img/desk/search.png);
			}
			.search-submit:hover {
				position: absolute;
				top: 4px;
				right: 6px;
				background:url(../iwork_img/desk/search_b.png);
			}
			.search input:focus {
				border-color: rgb(184, 222, 248);
				box-shadow: 0px 0px 5px rgba(123, 179, 207, 0.6);
			}
		</style>
</head>
<body  class="easyui-layout" style="background:#b4daf0">
<div region="north"  border="false" split="false"   class="nav" id="layoutNorth">
<div  class="top_head">
	<div style="padding:5px;text-align:right;"> 
	 <div class="login_head_nav">
	 <table width="100%">
	 	<tr>
	 		<td style="vertical-align:bottom">
	 			<s:property value="currentUserStr" escapeHtml="false"/>
	 		</td>
	 		<td>
    			<div id="search" class="search">
      				<input name="searchKey"  onKeyDown="enterKey();"  id="searchKey" value="" class="txt" type="text"></input>
      				<button class="search-submit" onclick="eagleSearch()" data-ca="search-btn"></button>
  			  </div>
	 		</td>
	 		<td>
	 			<ul>
			        <li><a href="javascript:addTab('我的日程','schCalendarAction.action','')">日程</a></li>
			        <li><a href="javascript:addTab('系统消息','sysmsg_index.action','')"  >系统消息</a></li>
			        <li><a href="javascript:addTab('个人设置','syspersion_info.action','');">设置</a></li>
			        <li><a href="javascript:exitOut()">退出</a></li>
		      </ul>
	 		</td>
	 	</tr>
	 </table>
    </div>
	</div>
	<div style="padding:5px;padding-left:100px;margin-top:45px;background:#285b7a;height:70px;">
				 	<a href="mainAction.action" class="easyui-linkbutton" iconCls="icon-home" data-options="plain:false" >首页</a>
				 		<s:property value="topMenuHtml" escapeHtml="false"/> 
					</div>
</div>
</div> 
<div region="center" border="false" style="border:0px;padding-left:5px;padding-right:5px;overflow:hidden;background-image:url(iwork_layout/default/img/center_bg.jpg);" id="layoutCenter" >
		<div id="mainFrameTab"  class="easyui-tabs" fit="true" border="false">
			<div title="<s:text name="portal.tab.Index.title"/>"  cache="false" > 
					<iframe width='100%' height='99%'  src='iworkMainPage.action?channelid=0' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div>
			<div title="<s:text name="portal.tab.process.title"/>"  cache="false" > 
					<iframe width='100%' height='99%'  src='process_desk_index.action' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div>
				<div title="<s:text name="portal.tab.desk.title"/>"  cache="false" > 
				<iframe width='100%' height='100%'  src='pt_person_index.action' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div> 
				<div title="<s:text name="portal.tab.search.title"/>"  cache="true" > 
				<iframe width='100%' height='99%'  src='eaglesSearch_Index.action' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div> 
			</div>
</div>  
<div region="south"  id="layoutSouth"  border="false" style="border-top:1px solid #efefef;padding:2px;height:10px;padding-left:10px;background-image:url(iwork_layout/default/img/center_bg.jpg);">
	</div>
</body>
</html>
