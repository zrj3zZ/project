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
	<link rel="stylesheet" type="text/css" href="iwork_layout/outlook/css/base.css" />
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jquery.messager.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.cluetip.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=iblue"></script>    
	<script type="text/javascript" src="iwork_layout/outlook/js/index.js"></script>    
	<script type="text/javascript" src="iwork_layout/outlook/js/jquery.outlook.js"></script>    
	
</head>
<body  class="easyui-layout" >
<div data-options="region:'north',border:false" style="height:75px;">
	<div class="head_bg"> 
	<div class="head"> 
	  <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
	  	<tr>
	  	<td rowspan="2" style="width:200px;height:100%">
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
	  		<td class="head_nav_menu" style="padding-left:200px;">
	  		<table>
	  		<tr>
			  		<s:iterator value="systemList">
			  		<td>
			  			<div class="menuItem" onclick="showContent(<s:property value="id"/>)">
			  			<div class="menuImg"><img onerror="javascript:this.src='iwork_img/menulogo_baibaoxiang.gif'" src="iwork_img/menulogo_baibaoxiang.gif"></div>
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
</div>
	<div data-options="region:'west',split:true" style="width:200px;padding:0px;overflow:hidden;border-top:0px;border-bottom:0px;border-left:0px;border-right:1px solid #efefef;">
		<div class="easyui-layout" fit="true" style="background:#ccc;border:0px">
				<div region="center" style="background:#ccc;border:0px">
					<div id="sysTabs" class="easyui-tabs" fit="true" border="false" style="border:0px"> 
						<div title="我的菜单" closable="false" style="overflow:auto;padding:0px;">
							<div id="outlook"></div>
						</div>
						<div title="收藏夹" closable="false" selected="true" style="overflow:auto;padding:0px;background-image:url(iwork_img/noise.png)">
	                    	<div style="width:100%;border-bottom:1px solid #EFEFEF;background:#fff">
				            <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" title="整理收藏夹" onclick="arrcoll();">整理收藏夹</a>
				            <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-reload" title="刷新" onclick="reloadfav();">刷新</a>
				            </div>				
						     <ul id="favtree" class="ztree"></ul>
						</div>
						</div>
					</div>	
				</div>
				<div region="south" split="true"  style="height:42px;border:0px;border-top:1px solid #efefef;padding:5px;">
					<div class="tree_sr_box">
				       <input type="button" class="search_btn1" id="search_btn1" onclick="this.className='search_btn1'" >
 					   <input id="input_focus" class="tree_sr_input" value="输入菜单名搜索">
  					   <input type="button" class="search_btn3"  id="search_btn"> 
				    </div>
				</div>
		</div>
	</div>
	<!-- <div data-options="region:'east',split:true,collapsed:true,title:'East'" style="width:100px;padding:10px;">east region</div> -->
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