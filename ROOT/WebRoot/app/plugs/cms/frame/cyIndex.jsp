<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=8" >
		<title>首页</title>
		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="../assets/css/bootstrap.min.css" />
		<link rel="stylesheet" href="../assets/css/font-awesome.min.css" />

		<!-- page specific plugin styles -->
		<link rel="stylesheet" href="../assets/css/jquery-ui.custom.min.css" />

		<!-- text fonts -->
		<link rel="stylesheet" href="../assets/css/ace-fonts.css" />

		<!-- ace styles -->
		<link rel="stylesheet" href="../assets/css/mainpage.css"/>

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="../assets/css/ace-part2.min.css" />
		<![endif]-->
		<link rel="stylesheet" href="../assets/css/ace-skins.min.css" />
		<link rel="stylesheet" href="../assets/css/ace-rtl.min.css" />

		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="../assets/css/ace-ie.min.css" />
		<![endif]-->

		<!-- inline styles related to this page -->

		<!-- ace settings handler -->
		<script src="../assets/js/ace-extra.min.js"></script>

		<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

		<!--[if lte IE 8]>
		<script src="../assets/js/html5shiv.min.js"></script>
		<script src="../assets/js/respond.min.js"></script>
		<![endif]-->
		<style type="text/css">
		.tr_bg1{
			background-color: #F0F0F0;
		}
		.tr_bg2{
			background-color:#ffffff;
		}
			 table
          {
              table-layout: fixed;
             width: 100%;
          }
         td
         {
             white-space: nowrap;
             overflow: hidden;
             text-overflow: ellipsis;
         }
		</style>
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
		<script type="text/javascript">
	
		///会议计划点击事件
		function showItem(id) {
			var title = "";
			var height = 580;
			var width = 900;
			var pageurl = "loadVisitPage.action?formid=96&instanceId=" + id + "&demId=26";
			var dialogId = "meetItem";
			parent.parent.openWin(title, height, width, pageurl, this.location,dialogId);
		}
		
			function ReadOne(id) {
				/* var obj = {id:id,cmd:"READ_ONE"};
			     $.post("sysmsg_setsutaus.action",obj,function(data){
			    	 window.location.reload();
			     }); */
			}
			function addTab(subtitle,url,icon){
				var pageUrl = url;
	    		var target = "_blank";
	    		var win_width = window.screen.width;
	    		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	    		page.location = pageUrl;
			}
			//打开新闻窗口
			function openCms(infoid){
			    var url = "cmsOpen.action?infoid="+infoid;
			    var newWin="newVikm"+infoid;
			    var win_width = window.screen.width-150;
			    var win_height = window.screen.height-150;
			    var page = window.open('form/waiting.html',newWin,'width='+win_width+',height='+win_height+',top=30,left=50,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			    page.location=url;
			}
	
			function changeTRbgcolor(obj){
				obj.className='tr_bg1';
			}
	
			function dorpTRbgcolor(obj){
				obj.className='tr_bg2';
			}
	
			/*//切换问题tab
			function changeKnowTab(type){
				var tabName;
				if(type==0)tabName="todo";
				if(type==1)tabName="finish";
				if(type==2)tabName="question";
				if(type==3)tabName="answer";
				$.post('showKnowDiv.action',{type:type},function(data){
	           		$("#"+tabName).html(data);
	       		});
			}*/
			//待办事宜
			function changeDBSY(type){
				var tabName;
				if(type==0)tabName="todo";
				if(type==1)tabName="finish";
				if(type==2)tabName="question";
				$.post('fsp_plxxgx_index.action',{type:type},function(data){
	           		$("#"+tabName).html(data);
	       		});
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
			//周期日程提醒完成事件
			/* function rctxWczq11(type){
				if(window.confirm("确认完成后该日程将不显示，确认完成吗？")){
					$.post('rctx_zqwcsj.action',{type:type},function(data){
						window.location.reload();
		       		}); 
				}
			} */
			/* function rctxWczq(type){
				
				window.parent.setRedirect('nd_137','process_desk_index.action','流程处理中心'); 
			} */
			function addAdvance(id){
				
				var pageUrl ="open_Advance.action?id="+id;
					art.dialog.open(pageUrl,{
						title:'查看事项',
						loadingText:'正在加载中,请稍后...',
						rang:true,
						width:550, 
						cache:false,
						lock: true,
						height:500, 
						
					
					});
				

			}
			function openMoreWd(){
				window.parent.setRedirect('nd_136','know_index.action','问答');
				

			}
			//打开问题窗口
			function openQuestionWin(qid){  
			    var url = 'know_open_question.action?qid='+qid;  
			    window.open(url);
			}

			//打开更多知道
			function openMoreKnow(){
				window.parent.setRedirect('nd_77','know_index.action','知道');
			}
			//打开更多待办
			function openMoreTodo(){
				window.parent.setRedirect('nd_137','process_desk_index.action','流程处理中心');
			}
			function openQuestionWin(qid){  
			    var url = 'know_open_question.action?qid='+qid;  
			    window.open(url);
			}
			function openMoreHtml(showId){
				$.post("getMoreTitleStr.action",{portletId:showId},function(data){
					var title = data;
					var url = "getMoreCommonTypeHtmlStr.action?showId=" + showId + "&pageSize=10&pageNo=1";
					window.parent.setRedirect('',url,title); 
				});
			}
			function openMoreAppkm(){
				var title = "常用工具";
				var url = "getMoreAppkmHtmlStr.action?pageSize=10&pageNo=1";
				window.parent.setRedirect('',url,title); 
			}
			//切换信息咨询tab
			function changeHomeTab(channelid){
				$.post('showHomeDiv.action',{channelId:channelid},function(data){
					$("#todo").html(data);
					$("#finish").html(data);
					$("#question").html(data);
            		$("#homeDiv").html(data);
        		});
			}


			function openNoticePage(title,actDefId,instanceId,excutionId,taskId,dataid){
				//var url = 'loadNoticeFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId+'&dataid='+dataid;
				var url = "process_desk_notice.action";
				this.location = url;
//				var target = "_self"; 
//				var win_width = window.screen.width-50;
//				var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				//page.location=url;
//				page.document.title = title;  
//				$("#notice_"+taskId).hide(); 
			}
			function openSignsPage(title,actDefId,instanceId,excutionId,taskId){
				var url = 'loadSignsPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId;
				var target = "iform_"+taskId;
				var win_width = window.screen.width-50;
				var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				//page.location=url;ssss
				page.document.title = title; 
			}
			function openTaskPage(title,actDefId,instanceId,excutionId,taskId){
				var url = 'loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId;
				var target = "iform_"+taskId;
				var win_width = window.screen.width-50;
				var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				//page.location=url;
				page.document.title = title; 
			}
			function showCustomer(customerno,ggid,dqrq){
				var url = 'zqb_vote_customerList.action?khbh='+customerno+'&ggid='+ggid+'&dqrq='+dqrq;
				var target = "_blank";
				var win_width = window.screen.width-50;
				var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				page.document.location = url;
				return;
			}
			function showWJDC(ggid){
				var url = 'zqb_vote_wjdc.action?ggid='+ggid;
				var target = "_blank";
				var win_width = window.screen.width-50;
				var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				page.document.location = url;
				return;
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
					height:600, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false,
					close:function(){
						window.location.reload();
					}
				});
				/* var target = "_blank";
				var win_width = window.screen.width;
				var page = window.open('form/loader_frame.html',target,'width='+ win_width+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				page.location = pageUrl; */
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
					autoSize:false ,
					close:function(){
						window.location.reload();
					} 
				});
				/* var target = "_blank";
				var win_width = window.screen.width;
				var page = window.open('form/loader_frame.html',target,'width='+ win_width+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				page.location = pageUrl; */
			}
			function addTalk(formid,demid,instanceid,ggid,hfqkid) {
			    var pageUrl ="";
			    $.ajax({
					type: "POST",
					url: "zqb_announcement_notice_tzlx.action",
					data: {ggid:ggid},
					success: function(data){
						updckqk(ggid);
						if(data=="培训通知"){
							if(instanceid==0){
								pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid+"&ggid=" + ggid;
							}else{
							    pageUrl = "openTZGG.action?demId="+demid+"&ggid=" + ggid + "&formid="+formid+"&hfqkid="+hfqkid;
							}
							var target = "_blank";
							var win_width = window.screen.width;
							var page = openWin(pageUrl,target,'width='+ win_width+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
							page.location = pageUrl;
							/*art.dialog.open(pageUrl,{
								title : '通知公告回复',
								loadingText : '正在加载中,请稍后...',
								bgcolor : '#999',
								rang : true,
								width : 1100,
								cache : false,
								lock : true,
								height : 580,
								iconTitle : false,
								extendDrag : true,
								autoSize : false,
								close:function(){
									window.location.reload();
								}
							});*/
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
								height : 580,
								iconTitle : false,
								extendDrag : true,
								autoSize : false,
								close:function(){
									window.location.reload();
								}
							});
						}else{
							pageUrl = "zqb_announcement_notice_reply_add.action?ggid=" + ggid + "&hfqkid="+hfqkid;
							/*var target = "_blank";
							var win_width = window.screen.width;
							var page = window.open('form/loader_frame.html',target,'width='+ win_width+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
							page.location = pageUrl;*/
							art.dialog.open(pageUrl,{
								title : '通知公告回复',
								loadingText : '正在加载中,请稍后...',
								bgcolor : '#999',
								rang : true,
								width : 1100,
								cache : false,
								lock : true,
								height : 580,
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
			
				/*art.dialog.open(pageUrl,{
					title : '通知公告回复',
					loadingText : '正在加载中,请稍后...',
					bgcolor : '#999',
					rang : true,
					width : 1100,
					cache : false,
					lock : true,
					height : 580,
					iconTitle : false,
					extendDrag : true,
					autoSize : false,
					close:function(){
						window.location.reload();
					}
				});*/
				
			}
			//通知公告设置已查看
			function updckqk(ggid){
				$.ajax({
					type: "POST",
					url: "zqb_announcement_upd_ckqk.action",
					data: {ggid:ggid}
				});
			}
			function openWin(url,text,winInfo){  
			    var winObj = window.open(url,text,winInfo);  
			    var loop = setInterval(function() {       
			        if(winObj.closed) {      
			            clearInterval(loop);      
			            //alert('closed');      
			            parent.location.reload();   
			        }      
			    }, 0.1);     
			} 
			
			function addCustomer(tznr,tzggid,dqrq){
				var pageUrl = tznr+"&TZGGID="+tzggid+'&dqrq='+dqrq;
				art.dialog.open(pageUrl,{
					title:'日常工作反馈',
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
			function dealGzyjHF(gzyjid){
				var url = "zqb_nnouncement_dxyjtzr.action?gzyjid="+gzyjid;
				var target = "_blank";
				var win_width = window.screen.width;
				var page = window.open('form/loader_frame.html',target,'width='+ win_width+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				page.location = url;
			}
			function downloadTemplate(fjid){
				var url = 'uploadifyDownload.action?fileUUID='+fjid;
				window.location.href = url;
			}
			function updateClflag(instanceid,id){
				var result=confirm("确认更新为“已完成”?");
					if (result) {
						var updateUrl = "updateClFlag.action";
						$.post(updateUrl,{instanceid : instanceid,id:id},function(data) {
							alert(data);
							window.location.reload();
						});
					 }
			 }
			
		function updateGzsc(gzscformid,gzscdemid,cid,did,instanceid) {
			/*var gzscformid = $("#gzscformid").val();
			var gzscdemid = $("#gzscdemid").val();*/
			var pageUrl = "openFormPage.action?formid="+gzscformid+"&demId="+gzscdemid+"&instanceId="+instanceid+"&cid="+cid+"&did="+did+"&isDialogDisabled=1"+"&isHFRandHFNRdiaplsy=0";
			/*art.dialog.open(pageUrl,{
				title:'工作备查',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1100,
				cache:false,
				lock: true,
				height:580, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,
				close:function(){
					window.location.reload();
				}
			});*/
			var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = pageUrl;
		}
	
	function addGzschf(gzschfformid,gzschfdemid,cid,did){
		/*var gzschfformid = $("#gzschfformid").val();
		var gzschfdemid = $("#gzschfdemid").val();*/
		var pageUrl = "createFormInstance.action?formid="+gzschfformid+"&demId="+gzschfdemid+"&CID="+cid+"&DID="+did;
		/*art.dialog.open(pageUrl,{
			title:'工作备查',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:580, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		}); */
		var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = pageUrl;
	}
	
	function checkGzschf(gzschfformid,gzschfdemid,eins){
		/*var gzschfformid = $("#gzschfformid").val();
		var gzschfdemid = $("#gzschfdemid").val();*/
		var pageUrl = "openFormPage.action?formid="+gzschfformid+"&demId="+gzschfdemid+"&instanceId="+eins;
		/*art.dialog.open(pageUrl,{
			title:'查看',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:480, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		}); */
		var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = pageUrl;
	}
			
		</script>
	</head>

	<body class="no-skin">
		<!-- /section:basics/navbar.layout -->
		<div class="main-container" id="main-container">
			<!-- /section:basics/sidebar -->
			<div class="main-content">
				<!-- /section:basics/content.breadcrumbs -->
				<div class="page-content">
					<div class="page-content-area">
						<div class="row">
							<div class="col-xs-12">
								<div class="alert alert-block alert-success">
									<button type="button" class="close" data-dismiss="alert">
										<i class="ace-icon fa fa-times"></i>
									</button>
									<i class="ace-icon fa fa-check green"></i>
								<span>欢迎使用<s:property value="systemname" escapeHtml="false"/></span><span style="margin-left: 20px;font-size:12px"><s:property value="version" escapeHtml="false"/></span>
								</div>
								<!-- PAGE CONTENT BEGINS -->
								<div class="row">
								<div class="col-sm-6 widget-container-col">
										<div class="widget-box">
											<div class="widget-header style="border-bottom: 0px solid;">
												<h5 class="widget-title smaller">信息资讯</h5>

												<!-- #section:custom/widget-box.tabbed -->
												<div class="widget-toolbar no-border">
													<ul class="nav nav-tabs" id="myTab">
													</ul>
												</div>

												<!-- /section:custom/widget-box.tabbed -->
											</div>

											<div class="widget-body">
												<div class="widget-main no-padding">
													<div class="tab-content" 
													style="padding-top: 0px; padding-left: 0px; padding-right: 0px;padding-bottom: 0px;">
														<div id="home" class="tab-pane in active">
															<span id='homeDiv'>
  																市场新闻正在努力加载中...
															</span>
														</div>
														<div id="todo" class="tab-pane">
															<span id='todoDiv'>
  																信息披露更新正在努力加载中...
															</span>
														</div>

														<div id="finish" class="tab-pane">
															<span id='finishDiv'>
  																会议计划正在努力加载中...
															</span>
														</div>

														<div id="question" class="tab-pane">
															<span id='questionDiv'>
  																工作备查正在努力加载中...
															</span>
														</div>
														
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="col-xs-12 col-sm-6 widget-container-col">
										<!-- #section:custom/widget-box -->
										<div class="widget-box">
											<div class="widget-header" style="border-bottom: 0px solid;">
												<h5 class="widget-title">待审批</h5>

												<!-- #section:custom/widget-box.toolbar -->
												<div class="widget-toolbar">
												<!-- 
													<div class="widget-menu">
														<a href="#" data-action="settings" data-toggle="dropdown">
															<i class="ace-icon fa fa-bars"></i>
														</a>

														<ul class="dropdown-menu dropdown-menu-right dropdown-light-blue dropdown-caret dropdown-closer">
															<li>
																<a data-toggle="tab" href="#dropdown1">Option#1</a>
															</li>

															<li>
																<a data-toggle="tab" href="#dropdown2">Option#2</a>
															</li>
														</ul>
													</div>
												-->
												
													
													<a href="#" data-action="fullscreen" class="orange2">
														<i class="ace-icon fa fa-expand"></i>
													</a>

													<a href="#" data-action="reload">
														<i class="ace-icon fa fa-refresh"></i>
													</a>

													<a href="#" data-action="collapse">
														<i class="ace-icon fa fa-chevron-up"></i>
													</a>
												</div>

												<!-- /section:custom/widget-box.toolbar -->
											</div>

											<div class="widget-body">
												<div class="widget-main no-padding">
													<div class="tab-content" 
													style="padding-top: 0px; padding-left: 0px; padding-right: 0px;padding-bottom: 0px;">
														<div class="tab-pane in active">
															<span id='todoTable'>
																待办事项正在努力加载中...
															</span>
														</div>
													</div>
												</div>
											</div>
										</div>
										<!-- /section:custom/widget-box -->
									</div>
								</div><!-- /.row -->
								
								<div class="row">
								<div class="col-xs-12 col-sm-6 widget-container-col">
										<!-- #section:custom/widget-box -->
										<div class="widget-box">
											<div class="widget-header">
												<h5 class="widget-title smaller">快捷应用</h5>
 
												<!-- #section:custom/widget-box.toolbar -->
												<div class="widget-toolbar">
												<!-- 
													<div class="widget-menu">
														<a href="#" data-action="settings" data-toggle="dropdown">
															<i class="ace-icon fa fa-bars"></i>
														</a>

														<ul class="dropdown-menu dropdown-menu-right dropdown-light-blue dropdown-caret dropdown-closer">
															<li>
																<a data-toggle="tab" href="#dropdown1">Option#1</a>
															</li>

															<li>
																<a data-toggle="tab" href="#dropdown2">Option#2</a>
															</li>
														</ul>
													</div>
													 -->
												</div>
												<!-- /section:custom/widget-box.toolbar -->
											</div>
											<div class="widget-body">
												<div class="widget-main no-padding">
													<div class="tab-content">
														<div class="tab-pane in active">
															<span id='appkmTable'>
																常用资料正在努力加载中...
															</span>
														</div>
													</div>
												</div>
											</div>
										</div>
										<!-- /section:custom/widget-box -->
									</div>
								<div class="col-sm-6 widget-container-col">
										 <div class="widget-box">
											<div class="widget-header style="border-bottom: 0px solid;">
												<h5 class="widget-title smaller">问答</h5>

												<!-- #section:custom/widget-box.tabbed -->
												<div class="widget-toolbar no-border">
													<ul class="nav nav-tabs" id="myTab">
														
													</ul>
												</div>

												<!-- /section:custom/widget-box.tabbed -->
											</div>

											<div class="widget-body">
												<div class="widget-main no-padding">
													<div class="tab-content" 
													style="padding-top: 0px; padding-left: 0px; padding-right: 0px;padding-bottom: 0px;">
														<div id="rctx" class="tab-pane in active">
															<span id='rctx'>
  																问答正在努力加载中...
															</span>
														</div>

														
													</div>
												</div>
											</div>
										</div> 
									</div>
									
								</div><!-- /.row -->
							</div><!-- /.col -->
						</div><!-- /.row -->
					</div><!-- /.page-content-area -->
				</div><!-- /.page-content -->
			</div><!-- /.main-content -->
		</div><!-- /.main-container -->
		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='../assets/js/jquery.min.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='../assets/js/jquery1x.min.js'>"+"<"+"/script>");
</script>
<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='../assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
		</script>
		<script src="../assets/js/bootstrap.min.js"></script>

		<!-- page specific plugin scripts -->
		<script src="../assets/js/jquery-ui.custom.min.js"></script>
		<script src="../assets/js/jquery.ui.touch-punch.min.js"></script>

		<!-- ace scripts -->
		<script src="../assets/js/ace-elements.min.js"></script>
		<%-- <script src="../assets/js/ace.min.js"></script> --%>

		<!-- inline scripts related to this page -->
		<script type="text/javascript">
			jQuery(function($) {
				//首页加载前判断当前登录人密码是否为默认密码
				$.post("zqb_Verification_password.action",{},function(data){
					if(data=="true"){
						art.dialog.open("login_pwd_update.action",{
							title:'您的密码为默认密码，请先修改密码！',
							loadingText:'正在加载中,请稍后...',
							bgcolor:'#999',
							rang:true,
							width:900,
							cache:false,
							lock: true,
							height:600, 
							iconTitle:false,
							extendDrag:true,
							autoSize:false ,
							close:function(){
								window.location.reload();
							} 
						});
					}
				});
				
				// 初始化信息咨询tab
				$.post('getHomeTab.action',{},function(data){
            		$("#myTab").html(data);
        		});
				// 初始化公告
				$.post('showHomeDiv.action',{},function(data){
            		$("#homeDiv").html(data);
        		});
				// 初始化待审批
				$.post('getToDoHtml.action',{},function(data){
            		$("#todoTable").html(data);
        		});
				// 初始待办事宜
				$.post('fsp_plxxgx_index.action',function(data){
            		$("#todo").html(data);
        		});
				// 初始化常用资料
				$.post('getAppkmHtml.action',{},function(data){
            		$("#appkmTable").html(data);
        		});
				$.post('fsp_db_index.action',{},function(data){
					var jso = data.split(",");
					$("#sxcount").html(jso[0]);
					$("#hycount").html(jso[1]);
					$("#gzsccount").html(jso[2]);
        		});
				
				$.post('rctx_index.action',{},function(data){
            		$("#rctx").html(data);
        		});
				
				$('#simple-colorpicker-1').ace_colorpicker({pull_right:true}).on('change', function(){
					var color_class = $(this).find('option:selected').data('class');
					var new_class = 'widget-box';
					if(color_class != 'default')  new_class += ' widget-color-'+color_class;
					$(this).closest('.widget-box').attr('class', new_class);
				});
			
			
				// scrollables
				$('.scrollable').each(function () {
					var $this = $(this);
					$(this).ace_scroll({
						size: $this.data('size') || 100,
						//styleClass: 'scroll-left scroll-margin scroll-thin scroll-dark scroll-light no-track scroll-visible'
					});
				});
				$('.scrollable-horizontal').each(function () {
					var $this = $(this);
					$(this).ace_scroll(
					  {
						horizontal: true,
						styleClass: 'scroll-top',//show the scrollbars on top(default is bottom)
						size: $this.data('size') || 500,
						mouseWheelLock: true
					  }
					).css({'padding-top': 12});
				});
				
				$(window).on('resize.scroll_reset', function() {
					$('.scrollable-horizontal').ace_scroll('reset');
				});
			
			
				/**
				//or use slimScroll plugin
				$('.slim-scrollable').each(function () {
					var $this = $(this);
					$this.slimScroll({
						height: $this.data('height') || 100,
						railVisible:true
					});
				});
				*/
				
			
				/**$('.widget-box').on('setting.ace.widget' , function(e) {
					e.preventDefault();
				});*/
			
				/**
				$('.widget-box').on('show.ace.widget', function(e) {
					//e.preventDefault();
					//this = the widget-box
				});
				$('.widget-box').on('reload.ace.widget', function(e) {
					//this = the widget-box
				});
				
			
				//$('#my-widget-box').widget_box('hide');
			
				
			
				// widget boxes
				// widget box drag & drop example
			    $('.widget-container-col').sortable({
			        connectWith: '.widget-container-col',
					items:'> .widget-box',
					handle: ace.vars['touch'] ? '.widget-header' : false,
					cancel: '.fullscreen',
					opacity:0.8,
					revert:true,
					forceHelperSize:true,
					placeholder: 'widget-placeholder',
					forcePlaceholderSize:true,
					tolerance:'pointer',
					start: function(event, ui) {
						//when an element is moved, it's parent becomes empty with almost zero height.
						//we set a min-height for it to be large enough so that later we can easily drop elements back onto it
						ui.item.parent().css({'min-height':ui.item.height()})
						//ui.sender.css({'min-height':ui.item.height() , 'background-color' : '#F5F5F5'})
					},
					update: function(event, ui) {
						ui.item.parent({'min-height':''})
						//p.style.removeProperty('background-color');
					}
			    });
			*/
			});
		</script>
		<input name="channelid" id="channelid" type="hidden" value="<s:property value="channelId" />" />
		
	</body>
</html>