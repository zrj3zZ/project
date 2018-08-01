<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>项目管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/process_center.css" />
     <link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>  
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
   		function openInfo(instanceid){
   			var formid = 88;
   			var demId = 21;
   			var url ="loadVisitPage.action?formid="+formid+"&instanceId="+instanceid+"&demId="+demId;
   			var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = url;
			return; 
   		}
   		
   		function showCompany(customerno){
   				var pageurl="zqb_search_company.action?customerno="+customerno;
			var dialogId = "subComapny"; 
			var title = "分子公司信息";
			art.dialog.open(pageUrl,{
				   id:dialogId, 
					cover:true, 
					title:title,  
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					width:700,
					cache:false, 
					lock: true,
					esc: true, 
					height:550,   
					iconTitle:false,  
					extendDrag:true,
					autoSize:true,
					close:function(){
					   location.reload();
					}
				});
   		}
   		function showOutUser(customerno){
   				var pageurl="zqb_search_outuser.action?customerno="+customerno;
			var dialogId = "subComapny"; 
			var title = "外部人员信息";
			art.dialog.open(pageUrl,{
				   id:dialogId, 
					cover:true, 
					title:title,  
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					width:700,
					cache:false, 
					lock: true,
					esc: true, 
					height:550,   
					iconTitle:false,  
					extendDrag:true,
					autoSize:true,
					close:function(){
					   location.reload();
					}
				});
   		}
   		function showInUser(customerno){
   				var pageurl="zqb_search_inuser.action?customerno="+customerno;
			var dialogId = "subComapny"; 
			var title = "内部人员信息";
			art.dialog.open(pageUrl,{
				   id:dialogId, 
					cover:true, 
					title:title,  
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					width:700,
					cache:false, 
					lock: true,
					esc: true, 
					height:550,   
					iconTitle:false,  
					extendDrag:true,
					autoSize:true,
					close:function(){
					   location.reload();
					}
				});
   		}
   		function showStock(customerno){
   				var pageurl="zqb_search_stock.action?customerno="+customerno;
			var dialogId = "subSc";  
			var title = "股份信息";
			art.dialog.open(pageUrl,{
				   id:dialogId, 
					cover:true, 
					title:title,  
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					top:100,
					width:700,
					cache:false, 
					lock: true,
					esc: true, 
					height:550,   
					iconTitle:false,  
					extendDrag:true,
					autoSize:true,
					close:function(){
					   location.reload();
					}
				});
   		}
   		function showInfo(customerno){
   				var pageurl="zqb_search_event.action?customerno="+customerno;
			var dialogId = "subSc";  
			var title = "重大事项申报信息";
			art.dialog.open(pageUrl,{
				   id:dialogId, 
					cover:true, 
					title:title,  
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					top:100,
					width:700,
					cache:false, 
					lock: true,
					esc: true, 
					height:550,   
					iconTitle:false,  
					extendDrag:true,
					autoSize:true,
					close:function(){
					   location.reload();
					}
				});
   		}
    </script>
    <style type="text/css">
		 body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
		.groupTitle{
			font-family:黑体;
			font-size:12px;
			text-align:left;
			color:#666;
			height-line:20px;
			padding:5px;
			padding-left:15px;
		}
		.itemList{  
			font-family:宋体;
			font-size:12px;
			padding-left:15px;
		}
		.itemList td{
			list-style:none;
			height:20px;
			padding:2px;
			padding-left:20px;
		}
		.itemList tr:hover{
			color:#0000ff;
			cursor:pointer;
		}
		.itemList  td{
			font-size:12px;
			line-height:30px;
		}
		
		.itemicon{
			padding-left:25px;
			background:transparent url(iwork_img/pin.png) no-repeat scroll 0px 3px;
		}
		.selectBar{
			border:1px solid #efefef;
			margin:5px;
			background:#FDFDFD;
		}
		.selectBar td{
			vertical-align:middle;
			height:20px;
		}
		
		.selectBar td linkbtn{
			color:#0000FF;
			text-decoration: none;
		}
		.listTitle td{
			 background-color:#efefef;
		 	font-weight:bold; 
			 color:#666;
		}
	</style>	
  </head> 
    <body class="easyui-layout">
    <div region="north" border="false" split="false"  style="height:35px;" id="layoutNorth">
	<div class="process_header">
	  <div class="process_head_tab_box"> 
	 	 <a class="process_head_tab_active"  href="zqb_search_center.action">客户信息查询</a>
	      </div>
	</div>
	</div>
      <div region="center"  border="false" style="padding:5px;">
      	  <div >
      	  	<table width="100%"   class="itemList"  cellpadding="0" cellspacing="0">
      	  	<tr class="listTitle">
      			<td>客户名称</td>
      			<td>挂牌时间</td>
      			<td>客户联系人</td>
      			<td>电话</td>
      			<td>邮箱</td>
      		</tr>
      	  	<s:iterator value="customerList">
      	 	<tr>
      	  			<td><a href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property value="CUSTOMERNAME"/></a></td>
      	  			<td>
      	  				<a href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property value="GPSJ"/></a>
      	  			</td>
      	  			<td>
      	  				<a href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property value="USERNAME"/></a>
      	  			</td>
      	  			<td>
      	  				<a href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property value="TEL"/></a>
      	  			</td>
      	  			<td>
      	  				<a href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property value="EMAIL"/></a>
      	  			</td>
      	  		</tr>
      	  		<tr>
      	  		<td style="text-align:right;border-bottom:1px solid #efefef"></td>
      	  			<td colspan="5" style="text-align:right;border-bottom:1px solid #efefef">
      	  				<a href="javascript:showCompany('<s:property value="CUSTOMERNO"/>');" class="easyui-linkbutton" plain="true" iconCls="icon-sysbtn">分子公司信息</a>
      	  				<a href="javascript:showOutUser('<s:property value="CUSTOMERNO"/>');" class="easyui-linkbutton" plain="true" iconCls="icon-sysbtn">外部人员信息</a>
      	  				<a href="javascript:showInUser('<s:property value="CUSTOMERNO"/>');" class="easyui-linkbutton" plain="true" iconCls="icon-sysbtn">内部人员信息</a>
      	  				<a href="javascript:showStock('<s:property value="CUSTOMERNO"/>');" class="easyui-linkbutton" plain="true" iconCls="icon-sysbtn">股份信息</a>
      	  				<a href="javascript:showInfo('<s:property value="CUSTOMERNO"/>');" class="easyui-linkbutton" plain="true" iconCls="icon-sysbtn">信息披露</a>
      	  			</td>
      	  		</tr>
      	  		
      	  	</s:iterator>
      	  	</table>
      	  </div>
 	 </div>
 	 
  </body>
</html>
		
		
		
		
		
		
		
		
		
		
		
		
		