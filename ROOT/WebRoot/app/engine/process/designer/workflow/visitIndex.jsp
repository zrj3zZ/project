<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>项目管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">  
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
     
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
     <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
    <script type="text/javascript">
   		$(function(){ 
			//加载导航树
			var setting = { 
					async: {
						enable: true, 
						url:"zqb_workflow_loadJson.action",
						dataType:"json" 
					},
					callback: {
						onClick:onClick 
					}
				};  
		var treeObj = $.fn.zTree.init($("#mapTree"), setting);
		})
		//点击树视图事件

		function onClick(event, treeId, treeNode, clickFlag){
			var zTree = $.fn.zTree.getZTreeObj("mapTree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0; 
			if(nodes.length>0){ 
				parentid = nodes[0].id;
				var type = nodes[0].nodeType;
				
				if(type=='group'){
					zTree.expandNode(treeNode, true, null, null, true);
				}else if(type=='item'){
					var instanceid =  nodes[0].instanceid;
					submitMap(instanceid);
				}
			}
		}
   		
   		function submitMap(instanceid){  
			var url = "zqb_workflow_visit.action?instanceid="+instanceid;
			$('#mapIndex').attr('src',url); 
		}
   		function showMax(){
   			var zTree = $.fn.zTree.getZTreeObj("mapTree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0; 
			if(nodes.length>0){ 
				parentid = nodes[0].id;
				var type = nodes[0].nodeType;
				if(type=='item'){
					var instanceid =  nodes[0].instanceid;
					var pageurl = "zqb_workflow_visit.action?instanceid="+instanceid;
					showMaxWin(pageurl);
				}
			}
   		}
   		function showMaxWin(pageurl){
			var title = "";
			 var height=580;
			 var width = 900;
			 var dialogId = "projectItem"; 
			 parent.openWin(title,height,width,pageurl,this.location,dialogId);
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
			height:200px;
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
	</style>	
  </head> 
    <body class="easyui-layout">
  
      <div region="north" border="false" >
      	<div class="tools_nav">
      		<a href="javascript:showMax();" class="easyui-linkbutton" plain="true" iconCls="icon-max">全屏</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
        </div>
      </div>
      <div region="west" border="false" style="width:200px;border-right:1px solid #efefef">
      	<div id="mapTree" class="ztree"></div>
      </div>
      <div region="center" style="padding:5px;"  border="false" >
      <iframe style="width:780px;height:535px" src="" id="mapIndex" frameborder="no" border="0" MARGINWIDTH="0" MARGINHEIGHT="0"  scrolling="no" ></iframe>
      </div>
 
  </body>
</html>
		
		
		
		
		
		
		
		
		
		
		
		
		