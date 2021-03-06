<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8"> 
  <title>待办流程</title>
  <meta name="viewport" content="width=device-width, initial-scale=0.8, user-scalable=0">
  <link rel="stylesheet" href="iwork_css/weixin/process/style.css">
  	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.layout-latest.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script  type="text/javascript" src="iwork_js/weixin/weixin_tools.js"></script> 
<script type="text/javascript">
	   <s:property value="initWeiXinScript" escapeHtml="false"/>
  $(document).on("pageinit","#pageone",function(){
	  $("#formpage li").on("tap",function(){
	  		var taskId = $(this).attr("id");
	  		var actDefId = $(this).attr("actDefId");
	  		var instanceId = $(this).attr("instanceId");
	  		var excutionId = $(this).attr("excutionId");
	  		var taskType = $(this).attr("taskType");
	  		if(taskType=='Signs'){
	  			window.location ="weixin_loadSignsPage.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+excutionId+"&taskId="+taskId;
	  		}else if(taskType=='Notice'){
	  			 selectUrl('weixin_processdeskNotice.action');
	  		}else{
	  			window.location ="weixin_processFormPage.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+excutionId+"&taskId="+taskId;
	  		}
	  }); 
	  $('#historyTab').on('tap', function(e) { 
        selectUrl('weixin_processdeskHistory.action');
    });
    $('#noticeTab').on('tap', function(e) {
        selectUrl('weixin_processdeskNotice.action');
    });
}); 

function selectUrl(url){
	window.location.href = url;
}

function run2Code(){
	wx.scanQRCode({
    desc: '扫描流程单据号',
    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
    success: function (res) {
	    var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果 
	    	$.post('iwork_doScanCode.action',{type:'fbc',codestr:result}, function (text, status) {
			       if(text.indexOf('url:')>-1){ 
			       		var pageUrl = text.replace('url:','');
			       		selectUrl(pageUrl);
			       }else{
			       		alert('条码/二维码非流程相关，请确认扫描的二维码是否正确!');
			       }
	      		});
	}
	});
}
</script>
</head>
<body ontouchstart="">

<div data-role="page" id="pageone">
<!-- /header --> 
<div data-role="header" style="height:45px;" data-position="fixed" >
     <a href="javascript:run2Code()" class="ui-btn-right" data-icon="bars" >二维码</a>
</div>
    <ul id="formpage" data-role="listview" data-inset="true">
      <s:property value="listHtml" escapeHtml="false"/>
    </ul>
    <div data-role="footer"  data-position="fixed">
	   <div data-role="navbar">  
        <ul >
            <li><a id="todoTab" href="#" data-icon="grid" class="ui-btn-active">待办流程</a></li>
            <li><a id="historyTab" data-icon="star">已办跟踪</a></li>
            <li><a id="noticeTab"  data-icon="gear">传阅/通知</a></li>
        </ul>   
	</div><!-- /footer -->
</div>
</div> 
</body>
</html>
<script>
function hideOption(){
	 wx.hideOptionMenu();
}
setTimeout("hideOption()",500);
   
</script>