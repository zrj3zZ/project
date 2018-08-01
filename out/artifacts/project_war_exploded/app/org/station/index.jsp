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
		.delbtn{
			float:right;
			padding-top:10px;
			padding-right:5px;
		}
	</style>
	<script>
	$(function(){
		$('.tablist li').click(function(obj){
			 $(".tablist li").each(function(item){
				    $(this).removeClass("current");
			  });
			 $(this).addClass("current");
			  var stationId = $(this).attr('id');
			  var url ="org_station_ins_list.action?stationId="+stationId;
			   $("#ifm").attr("src",url);
	    });
	});
	function addStation(){
		var userid = $('#userid').val();
		var pageUrl = "org_station_add.action";
		 art.dialog.open(pageUrl,{
						id:'dg_addStation',  
	       				title:'新增岗位',
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
	
	
	function deleteItem(id){
		 art.dialog.confirm('确认是否删除当前岗位', function(){
					var pageurl = "org_station_delete.action";
					 $.post(pageurl,{id:id},function(msg){ 
						 if(msg=='success'){
							 location.reload(); 
						 }else{
							 alert('权限不足，无法执行删除操作!');
						 }
						 }); 
					return;
			}); 
	}
	</script>
</head>
<body class="easyui-layout">
		 <div region="north" border="false"  style="overflow:hidden;" > 
		 	<div class="topHead">
		 		<table width="100%" cellspacing="0" cellpadding="0" border="0">
		 			<tr>
		 				<td>
		 					<div class="maintitle"><img alt="" src="iwork_img/hr_icon.png" style="width:35px;">岗位设置</div>
		 				</td>
		 				<td style="text-align:right;padding-right:30px;">
		 					岗位是为流程审批服务的，针对专业岗位对组织机构进行划分
		 				</td>
		 			</tr>
		 		</table>
		 	</div>
        </div>
		 <div region="west"  class="leftDiv" border="false" style="border-right:1px solid #fff;background-color:#efefef;width:200px;padding-top:5px;text-align:center;">
		 		<div class="addItemBtn" onclick="javascript:addStation();" ><span>新建岗位</span></div>
		 		<s:property value="stationHtml" escapeHtml="false"/>
        </div>  
            <div region="center" border="false" 	style="background: #fff; border-top:1px solid #efefef;">
            	<iframe width='100%' height='99%' src ="" frameborder="0" marginheight="0" marginwidth="0" frameborder="0" scrolling="auto" id="ifm" name="ifm"\>
            </div>
</body>
</html>
