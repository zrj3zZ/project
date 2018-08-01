<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><s:property value="systemTitle"/></title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/jquerycss/cluetip/jquery.cluetip.css" rel="stylesheet" type="text/css"/>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link href="iwork_layout/default/css/base.css" rel="stylesheet" type="text/css" /> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jquery.messager.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.cluetip.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>    
	<script type="text/javascript" src="iwork_js/index.js"></script>    
	<style type="text/css"> 
.tree_sr_box {
	background:url(iwork_img/tree_sr_bg.png) no-repeat;
	width:181px;
	height:26px;
	position:relative;
	padding-left:5px
}
.tree_sr_box2 {
	background:url(iwork_img/tree_sr_bg2.png) no-repeat;
	width:181px;
	height:26px;
	position:relative;
	padding-left:5px
}
.tree_sr_input {
	border:0 none;
	height:20px;
	line-height:20px;
	padding-left:20px;
	padding-right:20px;
	margin-top:3px;
	width:170px;
	color:#999;
	font-size:10px;
	background:transparent;
}
.search_btn1 {
	background:url(iwork_img/search_btn.png) no-repeat;
	height:26px;
	width:16px;
	position:absolute;
	display:block;
	cursor:pointer;
	top:4px;
	left:5px;
	border:0 none;
	color:#646464;
	outline:none;
}
.search_btn2 {
	background:url(iwork_img/magnifier_icon2.png) no-repeat 3px 4px;
	height:26px;
	width:16px; 
	position:absolute;
	display:block;
	cursor:pointer;
	top:2px;
	left:150px;
	border:0 none;
	color:#646464;
	outline:none;
}
.search_btn3 {
	background:transparent;
	height:26px;
	width:26px;
	position:absolute;
	display:block;
	cursor:pointer;
	top:2px;
	left:150px;
	border:0 none;
	color:#646464;
	outline:none;
}
#search_btn {
	outline:none;
}

		input {
			outline: none;
		}
		.title{
			font-size:16px; 
			font-weight:bold;
			padding:20px 10px;
			background:#eee;
			overflow:hidden;
			border-bottom:1px solid #ccc;
		}
		.t-list{
			padding:5px;
		}
	</style>	
</head>
<body  class="easyui-layout" >
<!-- TOP区 -->
    <!-- 导航区 -->
	<div region="west"  region="west"  border="false" split="true"  style="width:200px;padding:0px;overflow:hidden;border-right:0px solid #efefef;">
		<div class="easyui-layout" fit="true" style="background:#ccc;border:0px">
				<div region="center" style="background:#ccc;border:0px">
					<div class="easyui-tabs" fit="true" border="false" style="border:0px"> 
						<div title="我的菜单" closable="false" style="overflow:auto;padding:0px;background-image:url(iwork_img/noise.png)">
						<div style="width:100%;border-bottom:1px solid #EFEFEF;background:#fff">
			            <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" title="添加至收藏夹" onclick="addcoll();">添加至收藏夹</a>
			            <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-reload" title="刷新" onclick="reloadnav();">刷新</a>
			            </div>
							 <ul id="navtree" class="ztree" ></ul>
						</div>
						<div title="收藏夹" closable="false" style="overflow:auto;padding:0px;background-image:url(iwork_img/noise.png)">
	                    <div style="width:100%;border-bottom:1px solid #EFEFEF;background:#fff">
			            <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" title="整理收藏夹" onclick="arrcoll();">整理收藏夹</a>
			            <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-reload" title="刷新" onclick="reloadfav();">刷新</a>
			            </div>				
						     <ul id="favtree" class="ztree"></ul>
						</div>
					</div>	
				</div>
				<div region="south" split="true" style="height:40px;border:0px;border-top:1px solid #efefef;padding:5px;">
					<div class="tree_sr_box">
				       <input type="button" class="search_btn1" id="search_btn1" onclick="this.className='search_btn1'" >
 					   <input id="input_focus" class="tree_sr_input" value="输入菜单名搜索">
  					   <input type="button" class="search_btn3"  id="search_btn"> 
				    </div>
				</div>
		</div>
			</div>
	<div region="south" border="false" style="border-top:1px solid #ccc;padding:2px;height:25px;padding-left:10px;">
		<table width="100%" border="0">
			<tr>
				<td><div id="bgclock"></div></td>
				<td style="text-align:right;padding-right:5px;"><a id="userOnLineLink" href="###" onclick="showOnline();"><img src="iwork_img/user.png" border="0"/>在线用户</a></td>
			</tr>
		</table>
	</div>
	<div region="center" border="false" style="border-left:1px solid #efefef">
			<div id="mainFrameTab"  class="easyui-tabs" fit="true" border="false">
				<div title="首页"  cache="false" > 
					<iframe width='100%' height='100%'  src='iworkMainPage.action?channelid=0' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div>
				<div title="我的桌面"  cache="false" > 
				<iframe width='100%' height='100%'  src='pt_person_index.action' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div> 
				<div title="鹰眼检索"  cache="false" > 
				<iframe width='100%' height='100%'  src='eaglesSearch_Index.action' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div> 
			</div>
	</div>
	<div region="north" border="false" split="false" >
	<div class="head_bg">
	<div class="head"> 
	  <div class="logo" onclick="showMainPage();"></div>
	  <div class="head_font">
	    <div class="head_font_top">
	    <a href="#"  id="createCenter" target="_parent">发起中心</a> 
	    | <a href="#"  id="reportCenter" target="_parent">报表中心</a> 
	    | <a href="#"  id="eaglesSearch"  target="_parent">鹰眼检索</a> 
	    | <a href="#"  id="userOnLine" onclick="showOnline();"  target="_parent">在线用户</a> 
	    | <a href="#"  id="personConfig"  target="_parent">设置</a> 
	    | <a href="#"  id="loginOut"  target="_parent">退出平台</a></div>
	  </div>
	  <div class="head_info_font">
	     <div class="head_font_bottom">
	     <span><img src="iwork_layout/default/img/hire-me.png" width="16" height="16" style="vertical-align:text-bottom; margin-bottom:2px; margin-bottom:-2px\9; margin:0px 3px 0px 10px;"/><a class="basic" href="#" rel="user_tip.action?userid=<s:property value='userid'  escapeHtml='false'/>"><s:property value="currentUserStr"  escapeHtml="false"/></a></span>
	     <img src="iwork_layout/default/img/exclamation_octagon_fram.png" width="16" height="16" style="vertical-align:text-bottom; margin-bottom:2px; margin-bottom:-2px\9; margin:0px 3px 0px 10px;"/><span id="workflow">(<s:property value="todoCount"  escapeHtml="false"/>)</span>
	     <span  id="sysmsg"><img src="iwork_layout/default/img/email.png" width="16" height="16" style="vertical-align:text-bottom; margin-bottom:2px; margin-bottom:-2px\9; margin:0px 3px 0px 10px;" />(<s:property value="unreadCount"  escapeHtml="false"/>)</span></div>
	  </div>
	</div>
	</div> 	
    </div>
			<div id="mm" class="easyui-menu" style="width:150px;">
				<div id="mm-tabclose">关闭</div>
				<div id="mm-tabcloseall">全部关闭</div>
				<div id="mm-tabcloseother">除此之外全部关闭</div> 
				<div class="menu-sep"></div>
				<div id="mm-tabcloseright">当前页右侧全部关闭</div>
				<div id="mm-tabcloseleft">当前页左侧全部关闭</div>
				<div class="menu-sep"></div>
				<div id="mm-exit">退出</div>
			</div> 
			<s:form>
				<s:hidden name = "funid"></s:hidden>
				<s:hidden name = "funtext"></s:hidden>
			</s:form>	
<iframe name='hidden_frame' id="hidden_frame" width= "0"  height= "0" style="VISIBILITY: hidden">
</iframe>  	
</body>
</html>
<script>
//setInterval("popUpSysMsg();",10000);
</script>