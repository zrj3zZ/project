<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:property value="systemTitle"/></title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/cluetip/jquery.cluetip.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css"  />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_layout/bpm2013/css/base.css" />
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jquery.messager.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.cluetip.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"></script>    
	<script type="text/javascript" src="iwork_layout/bpm2013/js/index.js"></script>    
</head>
<body  class="easyui-layout" > 
<div data-options="region:'north',border:false" style="height:58px;">
	<div class="head_bg">
	<div class="head"> 
	  <div class="logo" onclick="showMainPage();"><img src="iwork_layout/bpm2013/img/logo.png" height="35"></div>
	  <div class="head_font">
	    <div class="head_font_top"> 
	    <a href="javascript:addTab('发起中心','processLaunchCenter!index.action','');"  id="createCenter"  target="_parent">发起中心</a> 
	    | <a href="javascript:addTab('报表中心','ireport_rt_showcenter.action','');"  id="reportCenter">报表中心</a> 
	    | <a href="javascript:addTab('鹰眼检索','eaglesSearch_Index.action','')"  id="eaglesSearch"  target="_parent">鹰眼检索</a> 
	    | <a  href="javascript:showOnline()"  onclick="showOnline();"  target="_parent">在线用户</a> 
	    | <a href="javascript:addTab('个人设置','syspersion_info.action','')"   target="_parent">设置</a> 
	    | <a href="javascript:exit()"  id="loginOut"  target="_parent">退出平台</a></div>
	  </div>
	  <div class="head_info_font">
	     <div class="head_font_bottom">
	     <span onclick="addTab('流程处理中心','process_desk_index.action','')"><img src="iwork_layout/bpm2013/img/hire-me.png" width="16" height="16" style="vertical-align:text-bottom; margin-bottom:2px; margin-bottom:-2px\9; margin:0px 3px 0px 10px;"/><a class="basic" href="#" rel="user_tip.action?userid=<s:property value='userid'  escapeHtml='false'/>"><s:property value="currentUserStr"  escapeHtml="false"/></a></span>
	     <img src="iwork_layout/bpm2013/img/exclamation_octagon_fram.png" width="16" height="16" style="vertical-align:text-bottom; margin-bottom:2px; margin-bottom:-2px\9; margin:0px 3px 0px 10px;"/><span id="workflow">(<s:property value="todoCount"  escapeHtml="false"/>)</span>
	     <span  id="sysmsg" onclick="addTab('系统消息','sysmsg_index.action','')"><img src="iwork_layout/bpm2013/img/email.png" width="16" height="16" style="vertical-align:text-bottom; margin-bottom:2px; margin-bottom:-2px\9; margin:0px 3px 0px 10px;" />(<s:property value="unreadCount"  escapeHtml="false"/>)</span></div>
	  </div>
	</div>
	</div>
</div>
	<div data-options="region:'west',split:true" style="width:200px;padding:0px;overflow:hidden;border-top:0px;border-bottom:0px;border-left:0px;border-right:1px solid #efefef;">
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
				<div region="south" split="true" style="height:42px;border:0px;border-top:1px solid #efefef;padding:5px;">
					<div class="tree_sr_box">
				       <input type="button" class="search_btn1" id="search_btn1" onclick="this.className='search_btn1'" >
 					   <input id="input_focus" class="tree_sr_input" value="输入菜单名搜索">
  					   <input type="button" class="search_btn3"  id="search_btn"> 
				    </div>
				</div>
		</div>
	</div>
	<div data-options="region:'south',border:false"  style="border-top:1px solid #ccc;padding:2px;height:25px;padding-left:10px;">
		<table width="100%" border="0">
			<tr> 
				<td><div id="bgclock"></div></td>
				<td style="text-align:right;padding-right:5px;"><a id="userOnLineLink" href="###" onclick="showOnline();"><img src="iwork_img/user.png" border="0"/>在线用户</a></td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',border:false">
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
</body>
</html>
<script>
//setInterval("popUpSysMsg();",10000);
</script>