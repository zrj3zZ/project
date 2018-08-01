<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh">  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title><s:property value="systemTitle"/></title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css" /> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link href="iwork_layout/default/css/base.css" rel="stylesheet" type="text/css" /> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jquery.messager.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>	
	<script type="text/javascript" src="iwork_js/locale/<s:property value="#session.WW_TRANS_I18N_LOCALE"/>.js"></script>    
	<script type="text/javascript" src="iwork_layout/default/js/index.js"></script>    
</head>
<body  class="easyui-layout" >
    <!-- 导航区 -->
	<div region="west"  id="layoutWest" region="west"  border="false" split="false"  style="display:none;width:200px;padding:0px;overflow:hidden;border-right:0px solid #efefef;">
		<div class="easyui-layout" fit="true" style="background:#ccc;border:0px">
				<div region="center" style="background:#ccc;border:0px">
					<div class="easyui-tabs" fit="true" border="false" style="border:0px"> 
						<div title="<s:text name="portal.nav.menuTab1"/>" closable="false" style="overflow:auto;padding:0px;background-image:url(iwork_img/noise.png)">
						<div style="width:100%;border-bottom:1px solid #EFEFEF;background:#fff">
			            <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" title="<s:text name="portal.nav.btn.addfav"/>" onclick="addcoll();"><s:text name="portal.nav.btn.addfav"/></a>
			            <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-reload" title="<s:text name="portal.nav.btn.refresh"/>" onclick="reloadnav();"><s:text name="portal.nav.btn.refresh"/></a>
			            </div>
							 <ul id="navtree" class="ztree" ></ul>
						</div>
						<div title="<s:text name="portal.nav.menuTab2"/>" closable="false" style="overflow:auto;padding:0px;background-image:url(iwork_img/noise.png)">
	                    <div style="width:100%;border-bottom:1px solid #EFEFEF;background:#fff">
			            <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" title="<s:text name="portal.nav.btn.setfav"/>" onclick="arrcoll();"><s:text name="portal.nav.btn.setfav"/></a>
			            <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-reload" title="<s:text name="portal.nav.btn.refresh"/>" onclick="reloadfav();"><s:text name="portal.nav.btn.refresh"/></a>
			            </div>				
						     <ul id="favtree" class="ztree"></ul>
						</div>
					</div>
				</div>
				<div region="south"  style="height:50px;border:0px;border-top:1px solid #efefef;padding:0px;padding-left:2px;padding-top:10px;">
					<div class="tree_sr_box">
				       <input type="button" class="search_btn1" id="search_btn1" onclick="this.className='search_btn1'" >
 					   <input id="input_focus" class="tree_sr_input" value="输入菜单名搜索">
  					   <input type="button" class="search_btn3"  id="search_btn"> 
				    </div>
				</div>
		</div> 
			</div>
	<div region="south"  id="layoutSouth"  border="false" style="border-top:1px solid #ccc;padding:2px;height:25px;padding-left:10px;display:none">
		<table width="100%" border="0">
			<tr>
				<td><div id="bgclock"></div></td>
				<td style="text-align:right;padding-right:5px;"><a id="userOnLineLink" href="###" onclick="showOnline();"><img src="iwork_img/user.png" border="0"/><s:text name="portal.topbar.online"/></a></td>
			</tr>
		</table>
	</div>
	<div region="center" border="false" style="border-left:1px solid #efefef;display:none" id="layoutCenter" >
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
	<div region="north" border="false" split="false" style="display:none"  id="layoutNorth">
	<div class="head_bg">
	<div class="head"> 
	  <div class="logo" onclick="showMainPage();"></div>
	  <div class="head_font">
	    <div class="head_font_top">
	    <a href="#"  id="createCenter" target="_parent"><s:text name="portal.topbar.createcenter"></s:text></a> 
	    | <a href="#"  id="reportCenter" target="_parent"><s:text name="portal.topbar.reportcenter"></s:text></a> 
	    | <a href="#"  id="eaglesSearch"  target="_parent"><s:text name="portal.topbar.search"/></a> 
	    | <a href="#"  id="userOnLine"   target="_parent"><s:text name="portal.topbar.online"/></a> 
	    | <a href="#"  id="personConfig"  target="_parent"><s:text name="portal.topbar.set"/></a> 
	    | <a href="#"  id="loginOut"  target="_parent"><s:text name="portal.topbar.exit"/></a></div>
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
				<div id="mm-tabclose"><s:text name="portal.tab.btn.close"/></div>
				<div id="mm-tabcloseall"><s:text name="portal.tab.btn.closeall"/></div>
				<div id="mm-tabcloseother"><s:text name="portal.tab.btn.closeother"/></div> 
				<div id="mm-tabcloseright"><s:text name="portal.tab.btn.closeright"/></div>
				<div id="mm-tabcloseleft"><s:text name="portal.tab.btn.closeleft"/></div>
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
	$("#layoutSouth").show().animate(); 
	$("#layoutCenter").show().animate();
	$("#layoutWest").show().animate();
	$("#layoutNorth").show().animate();
//setInterval("popUpSysMsg();",10000);
</script>