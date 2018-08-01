<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="iwork" uri="/WEB-INF/sys-commonsTags.tld" %>
 
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script> 
	<style>
		.topHead{
			height:40px;
			background:#fff;
			border-bottom:3px solid #677e9b;
		}
		.maintitle{
			font-size:25px;
			font-family:黑体;
			font-werght:bold;
			padding:8px;
			color:#304d79;
		}
		.maintitle span{
			font-size:26px;
			color:#304d79;
			padding:2px;
		}
		.maintitle span span{
			color:red;
		}
		.tablist{
			list-style-type:none;
			 padding:0px;
			 margin-left:5px;
			 margin-top:5px;
			  border-top:1px solid #ccc;
		}
		.tablist li{
			 line-height:30px;
			 border-bottom:1px solid #ccc;
			 border-right:1px solid #ccc;
			 border-left:1px solid #ccc;
			 padding-left:25px;
			 font-family:微软雅黑;
			  background:#f7f7f7;
			  color:#666;
			  font-weight:bold;
			  text-align:left;
		}
		.tablist li:hover{
			   background-color:#fff;
			   background-image:url(iwork_img/arrow.png);
			   background-repeat:no-repeat;
			   background-position:5px 10px;
			   color:#333;
			 cursor:pointer;
		}
		.tablist .current{
			  background:#fff;
			   color:#333;
			   background-image:url(iwork_img/zoom.png);
			   background-repeat:no-repeat;
			   background-position:5px 7px;
			   border-right:1px solid #fff;
		}
		.tablist .current:hover{
		 background-image:url(iwork_img/zoom.png);
		}
		.addItemBtn{ 
			line-height:30px;
			text-align:left;
			padding-left:40px;
			font-size:14px;
			 font-family:微软雅黑;
			 cursor:pointer;
		}
		.addItemBtn span{
			background-image:url(iwork_img/add_obj.gif);
			background-repeat:no-repeat;
			 background-position:20px 5px;
			padding:5px;
			padding-left:40px;
		}
		.addItemBtn:hover span{
			border:1px solid #efefef;
			color:red;
		}
		.mainTable{
			width:100%;
		}
		.infoTable{
		  width:100%;
		}
		.infoTable td{
			vertical-align:top;
			padding:3px;
			padding-left:20px;
			width:30xp;
			text-align:left;
			font-size:14px;
			font-family:微软雅黑;
			
		}
		.infoTable td span{
			color:#0000ff;
		}
		.data_title{
			background:#f2f4f6;
			border-bottom:1px solid #c6c9ca;
			border-right:1px solid #c6c9ca;
			padding:2px;
			padding-left:5px;
			font-family:微软雅黑;
			line-height:25px;
		}
		.data_item{
			height:30px;
			border-bottom:1px solid #efefef;
		}
		.data_item td{
			font-family:微软雅黑;
			padding-left:5px;
		}
		.data_item .mailTitle span{
			color:#a0a0a0;
			font-weight:500;
		}
		.data_item:hover{
			height:30px;
			background:#f3f3f3;
			cursor:pointer;
		}
		.data_title{
			background:#f2f4f6;
			border-bottom:1px solid #c6c9ca;
			border-right:1px solid #c6c9ca;
			padding:2px;
			padding-left:5px;
			font-family:微软雅黑;
			line-height:25px;
			color:#666; 
		}
		
		.groupTitle{
			background:#efefef;
			padding:5px;
			padding-left:25px;
			font-family:微软雅黑;
			font-size:14px;
			 background-image:url(iwork_img/orange_grid.png);
			   background-repeat:no-repeat;
			   background-position:5px 5px;
		}
	</style>
	<script>
	$(function(){
		$('.tablist li').click(function(obj){
			 $(".tablist li").each(function(item){
				    $(this).removeClass("current");
			  });
			 $(this).addClass("current");
			  var label = $(this).attr('id');
			  var url ="iwork_hr_arc_index.action?tabkey="+label;
			  location.href = url;
	    });
	});
	function openSet(){
		 var pageUrl = "iwork_hr_self_set.action";
			 art.dialog.open(pageUrl,{
			    	id:"planlistDlg",
			    	title:"员工自助设置", 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:380,
				    height:450,
					close:function(){
						location.reload();
					}
				 });
		
	}
	//弹出上传头像窗口
	function add_image(){
		var userid = $('#userid').val();
		var pageUrl = "syspersion_photo.action?userid="+userid;
		 art.dialog.open(pageUrl,{
						id:'dg_addImage',  
	       				title:'上传头像',
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:500,
					    height:510,
				        close:function(){
				        	window.location.reload();
				        }
					 });
	}
	
	 <s:property value="script" escapeHtml="false"/>
	</script>
</head>
<body class="easyui-layout">
		 <div region="north" border="false"  style="overflow:hidden;" > 
		 	<div class="topHead">
		 		<table width="100%" cellspacing="0" cellpadding="0" border="0">
		 			<tr>
		 				<td>
		 					<div class="maintitle"><img alt="" src="iwork_img/hr_icon.png" style="width:35px;">员工自助</div>
		 				</td>
		 				<td style="text-align:right;padding-right:30px;">
		 					<s:property value="btnHtml" escapeHtml="false"/>
		 				</td>
		 			</tr>
		 		</table>
		 	</div>
        </div>
		 <div region="west"  class="leftDiv" border="false" style="border-right:1px solid #fff;background-color:#efefef;width:200px;padding-top:5px;text-align:center;">
		 		<div id="user_image_div">
						<img width="150" id="user_image" src='<s:property value='userImgPath'/>' alt='用户照片' title='用户照片' name='photoUpload' id='photoUpload' style="border:1px solid #e5e5e5;margin:3px;">
				</div>
				<div style="padding:5px;">
					<table width="100%">
						<tr>
							<td><a href="javascript:openSet()"><img border="0"  alt="" src="iwork_img/cog1.png">自助设置</a></td>
							<td><a href="javascript:add_image()"><img  border="0"  alt="" src="iwork_img/u_forward.gif">上传图片</a></td>
						</tr>
					</table>
					
				</div>
		 		<s:property value="toolbar" escapeHtml="false"/>
		 		<div>
		 			
		 		</div>
        </div>  
            <div region="center" border="false" 	style="background: #fff; border-top:1px solid #efefef;">
            	 <s:property value="content" escapeHtml="false"/>
            </div>
            <input type="hidden" name="recNum" value="" id="recNum"/>
            <input type="hidden" name="draftNum" value="" id="draftNum"/>
            <input type="hidden" name="issupport" value="" id="issupport"/>
            <s:hidden name="userid" id="userid"></s:hidden>
</body>
</html>
