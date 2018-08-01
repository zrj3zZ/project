<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  
    <title>脚本触发器测试（测试完之后可以删掉该文件）</title>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script>
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script> 
    <script type="text/javascript">
    //脚本触发器测试窗口
      function jsTrigger(){
         var actDefId='actdefid';
         var actStepId='actstepid';
         var prcDefId=2;
         var pageUrl =  "step_jsTrigger_load.action?actDefId="+encodeURI(actDefId)+"&actStepId="+encodeURI(actStepId)+"&prcDefId="+prcDefId;
		
         art.dialog.open(pageUrl,{
					id:'js_test',
					cover:true,
					title:'脚本触发器测试',
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,						
					cache:false,
					lock: true,
					width:1000,
					height:500,
					iconTitle:false,
					extendDrag:true,
					autoSize:false
				});
      }
    //自定义按钮测试窗口
    function defButton(){
         var actDefId='actdefid';
         var actStepId='actstepid';
         var proDefId=2;
         var pageUrl =  "step_defButton_load.action?actDefId="+encodeURI(actDefId)+"&actStepId="+encodeURI(actStepId)+"&proDefId="+proDefId;
		
         art.dialog.open(pageUrl,{
					id:'db_test',
					cover:true,
					title:'自定义按钮测试',
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,						
					cache:false,
					lock: true,
					width:1000,
					height:500,
					iconTitle:false,
					extendDrag:true,
					autoSize:false
				});
    }  
    </script>
  </head>
  
  <body>
    <input type="button" onclick="javascript:jsTrigger();" value="脚本触发器测试"/>
    <br/>
    <input type="button" onclick="javascript:defButton();" value="自定义按钮测试"/>
  </body>
</html>
