<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>召开会议</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
    <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default&self=true"></script> 
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    
    <style type="text/css">
		 body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
		.groupTitle{
			font-family:黑体;
			font-size:16px;
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
		.tips{
			margin:15px;
			padding:10px;
			background:#f6ffcb;
			border:1px solid #efefef;
			color:#666;
			line-height:16px;
		}
		.fileListArea{
			padding:5px;
			padding-left:15px;
		}
		.filelist{
			border:1px solid #efefef;
			
		}
		.filelist tr:hover{
			background-color:#efefef;
			cursor:pointer
		}
		.filelist td{
			padding:5px;
			border-bottom:0px solid #efefef;
			
		}
		.filelist td span{
			padding-right:5px;
		} 
		.titlefilelist{
			padding:5px;
		}
		.titlefilelist a{
			font-size:12px;
		}
	</style>
	<script type="text/javascript">
		var api,W;
		var mainFormValidator;
		$().ready(function() {
		try{
			api= frameElement.api;
			W = api.opener;	
		}catch(e){}
		});
		
		function uploadPage(){
			var hybh = $("#hybh").val();
			var pageUrl = "zqb_meeting_uploadPage.action?sizeLimit=62914520&fileMaxNum=300&fileMaxSize=629145200&parentid="+hybh;
			art.dialog.open(pageUrl,{
				id:'fav_show1',
				cover:true,
				title:'会议资料批量上传',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:680,
				cache:false,
				lock: true,
				height:480, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,
				close:function(){
				   location.reload();
				}
			});
		}
		function close(){
			api.close();
		}
		function removeFile(uuid){
			if(confirm("确认删除吗？")){
				$.post('uploadifyRemove.action',{fileUUID:uuid},function(response){  
	              		window.location.reload();
		    });	
			}
				
		}
	</script>	
  </head> 
    <body class="easyui-layout">
      <div region="center"  border="false" >
     <div class="fileListArea">
     	<s:property value="filehtml" escapeHtml="false"/>
     </div>
  </div>
    <div  region="south" border="false" style="padding:5px;">
      	<div style="text-align:right;">
        </div>
      </div>
 
  </body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{},;'[\]]/im;
    	    if(patrn.test(value)){
        	}else{
            	var flag = false;
            	var tmp = value.toLowerCase();
            	for(var i=0;i<sqlstr.length;i++){
                	var str = sqlstr[i];
					if(tmp.indexOf(str)>-1){
						flag = true;
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>