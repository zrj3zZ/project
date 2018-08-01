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


        <link rel="stylesheet" href="../assets/css/liMarquee.css">
        <script src="../assets/js/jquery-1.8.3.min.js"></script>
        <script src="../assets/js/jquery.liMarquee.js"></script>
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
		
		</style>
			<% 
			int size=(int)request.getAttribute("size");
		%>
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
			
			function openGGMB(){
					var url = "zqb_template_list.action";
					window.parent.setRedirect('',url,'公告模板'); 
			}
			
			function openLCZY(){
				var url = "cjywlczy.action";
				window.parent.setRedirect('',url,'流程指引'); 
			}
			
			function openYWPX(){
				var url="kmdoc_Index.action?flag=1&dir=ywpxDir";
				window.parent.setRedirect('',url,'业务培训'); 
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
            		$("#tab9 tr:even").css("background","#F0F0F0");
        		});
			}


			function openNoticePage(title,actDefId,instanceId,excutionId,taskId,dataid){
				var url = "process_desk_notice.action";
				this.location = url;

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
			
			var pageUrl = "openFormPage.action?formid="+gzscformid+"&demId="+gzscdemid+"&instanceId="+instanceid+"&cid="+cid+"&did="+did+"&isDialogDisabled=1"+"&isHFRandHFNRdiaplsy=0";
			
			var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = pageUrl;
		}
	
	function addGzschf(gzschfformid,gzschfdemid,cid,did){
		
		var pageUrl = "createFormInstance.action?formid="+gzschfformid+"&demId="+gzschfdemid+"&CID="+cid+"&DID="+did;
		
		var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = pageUrl;
	}
	
	function checkGzschf(gzschfformid,gzschfdemid,eins){
		
		var pageUrl = "openFormPage.action?formid="+gzschfformid+"&demId="+gzschfdemid+"&instanceId="+eins;
		
		var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = pageUrl;
	}
		
	//切换信息咨询tab
	function changeHomeTabdm(channelid){
		$.post('showHomeDiv.action',{channelId:channelid},function(data){
			$("#scxwDiv").html(data);
			$("#qyyqDiv").html(data);
			
		});
	}
	$(function() {
	    var num = 0;
	    function goLeft() {
	        //750是根据你给的尺寸，可变的
	        if (num == -<%=size %>) {
	            num = 0;
	        }
	        num -= 1;
	        $(".scroll").css({
	            left: num
	        })
	    }
	    //设置滚动速度
	    var timer = setInterval(goLeft, 20);
	    //设置鼠标经过时滚动停止
	    $(".box").hover(function() {
	        clearInterval(timer);
	    },
	    function() {
	        timer = setInterval(goLeft, 20);
	    })
	})
		</script>
	</head>
 <style type="text/css">
/*border-bottom: 1px solid #F0F0F0;*/
        li {
	list-style:none;
}

.winBox {
	width:100%;
	height:45px;
	overflow:hidden;
	position:relative;
	background:#dff0d8;
	margin-bottom:15px;
	
}
.scroll {
	/*width的大小是根据下面li的长度和多少个li而定的，需注意！*/
			width:<%=size %>px;
	position:absolute;
	left:0px;
	top:0px;
}
.scroll li {
	width:420px;
	float:left;
	line-height:45px;
	text-align:left;
	font-size: 15px;
}

  .scroll li span{
	  overflow: hidden; 
        text-overflow: ellipsis; 
        -o-text-overflow: ellipsis;
        white-space:nowrap;
        width:380px;
        display:block;
	
	
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

	<body class="no-skin" >
		<!-- /section:basics/navbar.layout -->
		<div class="main-container" id="main-container">
			<!-- /section:basics/sidebar -->
			<div class="main-content">
				<div class="page-content">
					<div class="page-content-area">
						<div class="row">
							<div class="col-xs-12">
								

						  <div class="box">
  <div class="winBox" id="gdt">
      <div class="dowebok" id="dwork"></div>
  </div>
</div>



							<!-- PAGE CONTENT BEGINS -->
								<div class="row">
								<div class="col-sm-8 widget-container-col">
								<div class="widget-box" style="height: 196px;">
											<div class="widget-header" style="border-bottom: 0px solid;">
												<h5 class="widget-title">待处理事项</h5>

												
											</div>

											<div class="widget-body">
												<div class="widget-main no-padding">
													<div class="tab-content"
													style="padding-top: 0px; padding-left: 0px; padding-right: 0px;padding-bottom: 0px;"
													>
														<div class="tab-pane in active">
															<span id='todoTable'>
																待办事项正在努力加载中...
															</span>
														</div>
													</div>
												</div>
											</div>
										</div>
										
									</div>
									<div class="col-xs-12 col-sm-4 widget-container-col">
									<div class="widget-box" style="height: 196px;">
											<div class="widget-header" style="border-bottom: 0px solid;">
												<h5 class="widget-title smaller" style="font-size: 15px;">知识与培训</h5>
												<div class="widget-toolbar">
												</div>
											</div>
											<div class="widget-body">
												<div class="widget-main no-padding">
													<div class="tab-content" 
													style="padding-top: 0px; padding-left: 0px; padding-right: 0px;padding-bottom: 0px;">
														<div class="tab-pane in active">
														<table id="tab6" class="table table-condensed table-hover table-striped table-borderless table-fixed " style="font-size:15px;">	<tbody>
																<tr>
																	<td width="100%" height="38px" ><a
																		style="cursor: pointer;" onclick="openGGMB()"
																		title='公告模板 '>公告模板 </a></td>
																</tr>
																<tr>
																	<td width="100%" height="38px"><a
																		style="cursor: pointer;" onclick="openLCZY()"
																		title='流程指引'>流程指引 </a></td>
																</tr>
																<tr>
																	<td width="100%" height="38px"><a
																		style="cursor: pointer;" onclick="openYWPX()"
																		title='业务培训 '>业务培训 </a></td>
																</tr>
																<tr>
																	<td width="100%" height="38px"></td>
																</tr>
															</tbody>
														</table>
														
														</div>
													</div>
												</div>
											</div>
										</div>
										
									</div>
								</div><!-- /.row -->
								
								<div class="row">
								
								<div class="col-xs-12 col-sm-8 widget-container-col">
										<div class="widget-box" style="height: 200px;">
											<div class="widget-header" style="border-bottom: 0px solid;">
												<h5 class="widget-title smaller" style="font-size: 15px;">业务通知</h5>

												<!-- #section:custom/widget-box.tabbed -->
												<div class="widget-toolbar no-border">
													<ul class="nav nav-tabs" id="myTab">
													</ul>
												</div>

											</div>

											<div class="widget-body">
												<div class="widget-main no-padding">
													<div class="tab-content" 
													style="padding-top: 0px; padding-left: 0px; padding-right: 0px;padding-bottom: 0px;">
														<div id="home" class="tab-pane in active">
															<span id='homeDiv'>
  																公告信息正在努力加载中...
															</span>
														</div>
														
														<div id="finish" class="tab-pane">
															<span id='finishDiv'>
  																会议计划正在努力加载中...
															</span>
														</div>

														
														
													</div>
												</div>
											</div>
										</div>
										<br/>
										
										<div class="widget-box" style="height: 200px;">
											<div class="widget-header" style="border-bottom: 0px solid;">
												<h5 class="widget-title smaller" style="font-size: 15px;">行业动态</h5>

												<!-- #section:custom/widget-box.tabbed -->
													<div class="widget-toolbar no-border">
													<ul class="nav nav-tabs" id="hydtTab">
													</ul>
												</div>

												<!-- /section:custom/widget-box.tabbed -->
											</div>

											<div class="widget-body">
												<div class="widget-main no-padding">
													<div class="tab-content" 
													style="padding-top: 0px; padding-left: 0px; padding-right: 0px;padding-bottom: 0px;">
														<div id="scxw" class="tab-pane in active">
															<span id='scxwDiv'>
  																市场新闻正在努力加载中...
															</span>
														</div>
														<div id="qyyq" class="tab-pane">
															<span id='qyyqDiv'>
  																企业舆情正在努力加载中...
															</span>
														</div>
														
													</div>
												</div>
											</div>
										</div>
									</div>
								<div class="col-sm-4 widget-container-col">
										 <div class="widget-box" style="height: 425px;">
											<div class="widget-header" style="border-bottom: 0px solid;">
												<h5 class="widget-title smaller" style="font-size: 15px">问答</h5>
												<div class="widget-toolbar no-border">
													<ul class="nav nav-tabs" id="myTab">
														
													</ul>
												</div>
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

           $(function(){
               // 初始化滚动信息
               $.post('showGdtDiv.action',{},function(data){
                   $("#dwork").html(data);
                   $("#tab6 tr:even").css("background","#F0F0F0");
                  $('.dowebok').liMarquee();
               });

           });
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
				// 初始化  董秘行业动态tab
				$.post('getHydtTab.action',{},function(data){
            		$("#hydtTab").html(data);
            		$("#tab1 tr:even").css("background","#F0F0F0");
        		});
				// 初始化公告
				$.post('showHomeDiv.action',{channelId:0 },function(data){
            		$("#scxwDiv").html(data);
            		$("#tab1 tr:even").css("background","#F0F0F0");
        		});
				// 初始化公告
				$.post('showHomeDiv.action',{},function(data){
					$("#homeDiv").html(data);
					$("#tab9 tr:even").css("background","#F0F0F0");
            		
        		});
				// 初始化待审批
				$.post('getToDoHtml.action',{},function(data){
            		$("#todoTable").html(data);
            		$("#tab2 tr:even").css("background","#F0F0F0");
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
            		$("#tab8 tr:even").css("background","#F0F0F0");
            		
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
			
			
			});
		</script>
		<input name="channelid" id="channelid" type="hidden" value="<s:property value="channelId" />" />
		
	</body>
</html>