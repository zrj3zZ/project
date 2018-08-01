<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><s:property value="moreTitle"/></title>
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/process/process_desk.js"></script> 
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/process_center.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/message/sysmsgpage.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css"/>
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css"/>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
		$(function() {
			$('#pp').pagination({
				pageList:[10,15,20,30,50],
				total : <s:property value="dataCount"/>,
				pageNumber : <s:property value="pageNo"/>,
				pageSize : <s:property value="pageSize"/>,
				onSelectPage : function(pageNo, pageSize) {
					submitMsg(pageNo, pageSize);
				}
			});
			
		});
		
		function submitMsg(pageNo, pageSize) {
			$("#pageNo").val(pageNo);
			$("#pageSize").val(pageSize);
			$("#editForm").submit();
			return;
		}
function reloadPage() {
	window.location.reload();
}
	//日程提醒完成事件
		function rctxWc(id,type,flag,sj){
				if(flag==1){
					if(window.confirm("确认完成后该日程将不显示，确认完成吗？")){
						$.post('rctx_wcsj.action',{type:type},function(data){
							window.location.reload();
			       		}); 
					}
				}else{
					if(window.confirm("确认完成后该日程将不显示，确认完成吗？")){
						$.post('rctx_zqwcsj.action',{type:id,sj:sj},function(data){
							window.location.reload();
			       		}); 
					}
				}
				
			}
	
		function showWJDC(ggid){
			var url = 'zqb_vote_wjdc.action?ggid='+ggid;
			var target = "_blank";
			var win_width = window.screen.width-50;
			var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.document.location = url;
			return;
		}
			   function openCms(infoid){
					var url = "cmsOpen.action?infoid="+infoid;
					var newWin="newVikm"+infoid;
					var page = window.open('form/waiting.html',newWin,'width=700,height=500,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
					page.location=url;
				}
		
				function changePage(url){
					location.href = url;
				}
				function showCustomer(customerno,ggid,dqrq){
					var url = 'zqb_vote_customerList.action?khbh='+customerno+'&ggid='+ggid+'&dqrq='+dqrq;
					var target = "_blank";
					var win_width = window.screen.width-50;
					var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
					page.document.location = url;
					return;
				}
				
				function expWord(customerno,id,dqrq) {
					var encodeUrl = encodeURI("zqb_vote_expWord.action?khbh="+customerno+"&ggid="+id+"&dqrq="+dqrq);
					window.location.href = encodeUrl;
				}
				function showCommunicate(customerno,ggid){
					var pageUrl = "showCommunicate.action?customerno="+customerno+"&ggid="+ggid;
					art.dialog.open(pageUrl,{
						title:'信披自查反馈沟通',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:900,
						cache:false,
						lock: true,
						height:500, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false,
						close:function(){
							window.location.reload();
						}
					});
				}
			function addTalk(formid,demid,instanceid,ggid,hfqkid) {
			    var pageUrl ="";
				    $.ajax({
						type: "POST",
						url: "zqb_announcement_notice_tzlx.action",
						data: {ggid:ggid},
						success: function(data){
							if(data=="培训通知"){
								if(instanceid==0){
									pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid+"&ggid=" + ggid;
								}else{
								    pageUrl = "openTZGG.action?demId="+demid+"&ggid=" + ggid + "&formid="+formid+"&hfqkid="+hfqkid;
								}
								var target = "_blank";
								art.dialog.open(pageUrl,{
									title : '通知公告回复',
									loadingText : '正在加载中,请稍后...',
									bgcolor : '#999',
									rang : true,
									width : 1100,
									cache : false,
									lock : true,
									height : 500,
									iconTitle : false,
									extendDrag : true,
									autoSize : false,
									close:function(){
										window.location.reload();
									}
								});
							}else if(data=="问卷调查"){
								pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid+"&TZGGID=" + ggid;
								art.dialog.open(pageUrl,{
									title : '问卷调查',
									loadingText : '正在加载中,请稍后...',
									bgcolor : '#999',
									rang : true,
									width : 1100,
									cache : false,
									lock : true,
									height : 500,
									iconTitle : false,
									extendDrag : true,
									autoSize : false,
									close:function(){
										window.location.reload();
									}
								});
							}else{
								pageUrl = "zqb_announcement_notice_reply_add.action?ggid=" + ggid + "&hfqkid="+hfqkid;
								art.dialog.open(pageUrl,{
									title : '通知公告回复',
									loadingText : '正在加载中,请稍后...',
									bgcolor : '#999',
									rang : true,
									width : 1100,
									cache : false,
									lock : true,
									height : 500,
									iconTitle : false,
									extendDrag : true,
									autoSize : false,
									close:function(){
										window.location.reload();
									}
								});
							}
						}
					});
				}
				function updateCommunicate(customerno,ggid){
					var pageUrl = "updateCommunicate.action?customerno="+customerno+"&ggid="+ggid;
					art.dialog.open(pageUrl,{
						title:'信披自查反馈沟通',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:900,
						cache:false,
						lock: true,
						height:600, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false,
						close:function(){
							window.location.reload();
						}
					});
				}
				function showCxddfkgzxx(ggid){
				var pageUrl = "zqb_cxdd_customerList.action?ggid="+ggid;
				/*art.dialog.open(pageUrl,{
					title:'持续督导日常工作反馈',
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:1100,
					cache:false,
					lock: true,
					height:800, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false,
					close:function(){
						window.location.reload();
					}
				});*/
				var target = "_blank";
				var win_width = window.screen.width;
				var page = window.open('form/loader_frame.html',target,'width='+ win_width+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				page.location = pageUrl;
			}
			function checkDaily(instanceid){
				var pageUrl = "zqb_project_checkDaily.action?instanceid="+instanceid;
				art.dialog.open(pageUrl,{
					title:'查看项目日志',
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:1100,
					cache:false,
					lock: true,
					height:680, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false,
					close:function(){}
				});
			}
			function addCustomer(tznr,tzggid){
				var pageUrl = tznr+"&TZGGID="+tzggid;
				art.dialog.open(pageUrl,{
					title:'持续督导日常工作反馈',
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:1100,
					cache:false,
					lock: true,
					height:800, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false,
					close:function(){
						window.location.reload();
					}
				});
			}
	
</script>
<style>
.header td {
	height: 30px;
	font-size: 12px;
	padding: 3px;
	white-space: nowrap;
	padding-left: 5px;
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left
		bottom;
	border-top: 1px dotted #ccc;
	border-right: 1px solid #eee;
}
.content td {
	border: 1px solid #efefef;
	align:center;
	valign:middle;
}
.wr_table td
         {
             white-space: nowrap;
             overflow: hidden;
             text-overflow: ellipsis;
		    padding-right: 8px;
		    padding-bottom: 8px;
		    padding-top: 8px;
		    padding-left: 8px;
         }
         
	
</style>
</head>
<body class="easyui-layout">
	
	<div region="center" border="false" id="layoutCenter" style="overflow:auto">
		<div class="wr_table" style="padding-left: 10px;">
			<table id="tab1" border="0" cellspacing="0" cellpadding="0">
				
					<tbody>
						<s:property value="htmlShowStr" escapeHtml="false" />
					</tbody>
				
			</table>
		</div>
		
		 <div region="south" style="padding-left: 10px;vertical-align:bottom;height:40px;color:#0000FF;font-size:12px;padding-top: 0px;padding-left:10px;" border="false" >
				<div style="padding-top: 5px;padding-right: 3px;">
			
			<div id="pp" style="border-spacing:2px;width: 99%;margin-left: 5px;background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			
			</div>
	</div>
	</div>
	 <s:form id="editForm" name="editForm" action="getMoreCommonTypeHtmlStr.action">
			<s:hidden name="pageNo" id="pageNo"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
			<s:hidden name="showId" id="showId"></s:hidden>
		</s:form>
		<script type="text/javascript">
	
	$("#tab1 tr:even").css("background","#F0F0F0");
	
	</script>
</body>
</html>