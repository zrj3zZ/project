<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">
	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_skins/_def1/css/globle_v1.css" rel="stylesheet" type="text/css" />
	<link href="iwork_skins/_def1/css/skin_blue.css" rel="stylesheet" type="text/css" />
	
	<link href="iwork_css/system/openaddresstree.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/system/openaddresstree.js"></script>
	
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="background:#B3DFDA;height:68px;padding:0px;overflow:no">
		<DIV class=HeadWp>
				<DIV class=HeadThemeWp>
					<DIV class=HeadWpInner>
						<H1 class="Logo"><A href="mainAction.action" ><IMG class=imgLogo src="iwork_img/IWORK-LOGO.gif" border=0></A><div class='logotitle'>iWORK管理平台</div></H1>
						<div class="logodate"><s:property value="dateStr"  escapeHtml="false"/></div>
						<FORM name="search" action="#" method="post">
						<SPAN class=UserInfoBar>
						<table border="0" cellpadding="0" cellspacing="0">
					      <tr>
					        <td class="userleft"><span class="user"></span><s:property value="currentUserStr"  escapeHtml="false"/></td>
					        <!-- 
					        <td class="logomail">(<span class="ts"><a href="#">2</a></span>)</td>
					        <td class="logomessenger">(<span class="ts"><a href="#">5</a></span>)</td> -->
					        <td class="logotodo"><div class="ts" id="sysmsg">(<s:property value="unreadCount"  escapeHtml="false"/>)</div></td>
					        <td class="logodayarr"><div class="ts">(0)</div></td>
					      </tr>
					    </table>
					    </SPAN>
							<SPAN class=SearchBar><A class="SchMenuBtn fRi"title=高级搜索 href="#"></A>
								<INPUT class="SchBtn fRi" title=立即搜索 type=submit value="">
								<INPUT class="Ipt fRi" maxLength=50 value=搜索... name=keyword>
							</SPAN>
						</FORM>
						<SPAN class=Extra>
							<A href="#">设置</A>&nbsp;|&nbsp;<A href="#" id='changepwd'>修改密码</A>|&nbsp;<a id="loginOut"  target="_parent">退出平台</A>
						</SPAN>
					<DIV class="InfoTips_Wp InfoTips_Scs" id=dvSuccessMsg style="DISPLAY: none">
					<DIV class=InfoTips><B class="F1Img cnL"></B><SPAN class=Txt id=spnSuccessText></SPAN>\
					<B class="F1Img cnR"></B>
					</DIV>
				</DIV> 
				<DIV class="InfoTips_Wp InfoTips_War" id=dvErrorMsg style="DISPLAY: none">
				<DIV class=InfoTips>
					<B class="F1Img cnL"></B><SPAN class=Txt id=spnErrorText></SPAN><B class="F1Img cnR"></B>
				</DIV>
				</DIV>
				</DIV>
				</DIV>
				</DIV>

    </div>
    <!-- 导航区 -->
	<div region="west"  region="west" icon="icon-reload"  split="true" title="导航菜单" style="width:200px;padding:0px;overflow:hidden;">
		<div class="easyui-tabs" fit="true" border="false">
					
					<div title="我的菜单" icon="icon-reload" closable="false" style="overflow:auto;padding:5px;">
						 <ul id="navtree">
						 </ul> 
					</div>
					<div title="收藏夹" closable="false" style="padding:20px;">我的收藏夹</div>
				</div>	
    </div>
	<div region="east" split="true" title="East" style="width:100px;padding:10px;">east region</div>
	<div region="south" border="false" style="height:20px;background:#A9FACD;">
		<div id="bgclock"></div>
	</div>
	<div region="center" >
			<div id="mainFrameTab"  class="easyui-tabs" fit="true" border="false">
				<div title="首页" style="width:100%;height:100%;"> 
							<div style="height:20px;padding:1px;background:#A9FACD;text-align:right">
								<a id="add" href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
								<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true">修改布局</a>
								<a href="#" class="easyui-linkbutton" onclick="javascript:alert('ttt')" plain="true">恢复默认设置</a>
							</div>
							<div id="pp" style="position:relative">
									<div style="width:30%;">
										<div title="Clock" style="text-align:center;background:#f3eeaf;height:150px;padding:5px;">
											<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0" width="100" height="100">
										      <param name="movie" value="http://www.respectsoft.com/onlineclock/analog.swf">
										      <param name=quality value=high>
										      <param name="wmode" value="transparent">
										      <embed src="http://www.respectsoft.com/onlineclock/analog.swf" width="100" height="100" quality=high pluginspage="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash" type="application/x-shockwave-flash" wmode="transparent"></embed>
										    </object>
									    </div>
										<div style="width:40%;">
											<div id="pgrid" title="DataGrid" closable="true" style="height:200px;">
												<table class="easyui-datagrid" style="width:650px;height:auto"
														fit="true" border="false"
														singleSelect="true"
														idField="itemid" url="datagrid_data.json">
													<thead>
														<tr>
															<th field="itemid" width="60">Item ID</th>
															<th field="productid" width="60">Product ID</th>
															<th field="listprice" width="80" align="right">List Price</th>
															<th field="unitcost" width="80" align="right">Unit Cost</th>
															<th field="attr1" width="120">Attribute</th>
															<th field="status" width="50" align="center">Status</th>
														</tr>
													</thead>
												</table>
											</div>

										</div>
										<div style="width:30%;">
											<div title="Searching" iconCls="icon-search" closable="true" style="height:80px;padding:10px;">
												<input>
												<a href="#" class="easyui-linkbutton">Go</a>
												<a href="#" class="easyui-linkbutton">Search</a>
											</div>
											<div title="Graph" closable="true" style="height:200px;text-align:center;">
												<img height="160" src="http://knol.google.com/k/-/-/3mudqpof935ww/ip4n5y/web-graph.png"></img>
											</div>

										</div>
								
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
			
			<div id="iwindow" class="easyui-dialog" title="My Window" iconCls="icon-save" style="width:200px;height:200px;padding:5px;background: #fafafa;">
				<div class="easyui-layout" fit="true">
					<div id="content" region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
						
					</div>
				</div>
			</div>
			

			
</body>
</html>
