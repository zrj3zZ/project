<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" /> 
		<title><s:text name="system.name"/></title> 
		<meta name="description" content="overview &amp; stats" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="../assets/css/bootstrap.css" />
		<link rel="stylesheet" href="../assets/css/font-awesome.css" />
		<link rel="stylesheet" href="../assets/css/jquery-ui.custom.css" />
		<link rel="stylesheet" href="../assets/css/jquery.gritter.css" />
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css" /> 
		<!-- page specific plugin styles -->
		<!-- text fonts -->
		<link rel="stylesheet" href="../assets/css/ace-fonts.css" />
		<!-- ace styles -->
		<link rel="stylesheet" href="../assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="../assets/css/ace-part2.css" class="ace-main-stylesheet" />
		<![endif]-->

		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="../assets/css/ace-ie.css" />
		<![endif]-->
		<!-- inline styles related to this page -->
		<!-- ace settings handler -->
		<script src="../assets/js/ace-extra.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>	
		<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->
		<!--[if lte IE 8]>
		<script src="../assets/js/html5shiv.js"></script>
		<script src="../assets/js/respond.js"></script>
		<![endif]-->
		<script type="text/javascript">
			function setCwinHeight(obj){ 
			 var height =   $(window).height()-120; 
			 	$(obj).css("height",height);
			}
			function showTips(title){
				$.gritter.add({
						title:title,
						class_name: 'gritter-diy gritter-center'
					});
				 setTimeout(function () {
			            $.gritter.removeAll();
			        }, 1000);
			}
			function setRedirect(id,url,title){
					$.gritter.add({
						title: '正在加载中...',
						class_name: 'gritter-diy gritter-center'
					});
				//初始化
				 $(".nav-list .active").each(function(i) {
				 	  $(this).removeClass("active");
				 });
				 if(id!=''){
					 $("#"+id).addClass("active");
				 }
				$("#ifm").attr("src",url);
			        setTimeout(function () {
			            $.gritter.removeAll();
			        }, 1000);
			        $("#meunTitle").text(title);
			    return ;
			}
			function openTaskPage(title,actDefId,instanceId,excutionId,taskId){
				var url = 'loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId;
				var target = "iform_"+taskId;
				var win_width = window.screen.width-50;
				var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				//page.location=url;
				page.document.title = title; 
			}
			function openEmailDetailInfo(id,taskid,title,emailType){
		        var pageurl="iwork_email_read.action?mailId="+id+"&taskid="+taskid+"&emailType="+emailType;
				var target = "iform_"+taskid;
                var win_width = window.screen.width-100;
                var win_heitht = window.screen.height-150;
                var page = window.open(pageurl,target,'width='+win_width+',height='+win_heitht+',top=30,left=50,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
                page.document.title = title; 
                loademailTip();
	      	}
	      	
			function loadtodoTip(){
				$.ajax({
		             type: "GET",
		             url: "mainpage_todolist.action",
		             dataType: "json",
		             success: function(data){
		             			 var num = data[0].total;
		                   		 var html = '';
				                   html+='<ul class="dropdown-menu dropdown-navbar">';
				                   var dataRows = data[0].dataRows;
				                   var i = 0;
				                   for (var row in dataRows){
				                   		i++;
				                   		html+='<li>';
				                   				if(dataRows[row].TYPE=='会签抄送列表'){
				                   					html+='<a href="javascript:setRedirect(\'\',\'process_desk_notice.action\')" class="clearfix">';
					                   			}else{
													html+='<a href="javascript:openTaskPage(\''+dataRows[row].TITLE+'\',\''+dataRows[row].actDefId+'\','+dataRows[row].instanceId+','+dataRows[row].excutionId+','+dataRows[row].taskId+')" class="clearfix">';
						                   		}
													html+='<img   onerror="this.src=\'iwork_img/nopic.gif\'" src="iwork_file/USER_PHOTO/'+dataRows[row].OWNERUSERID+'.jpg"  class="msg-photo" />';
													html+='<span class="msg-body">';
													html+='<span class="msg-title">';
													html+=dataRows[row].TITLE;
													html+='</span>';
													html+='<span class="msg-time">';
													html+='<i class="ace-icon fa fa-clock-o"></i>';
													html+='<span>'+dataRows[row].DATETIME+'</span>';
													html+='</span>';
													html+='</span>';
													html+='</a>';
										html+='</li>';
										if(i>=10){
											break;
										}
				                   }
				                   html+='</ul>';
				                   $("#todoNumTips").text(num);
				                   $("#todoNum").text(num);
				                   $("#todolist").html(html);
		                  
		             }
		         });
			}
			function loadsysmsgTip(){
				//获取消息列表
		          $.ajax({
		             type: "GET",
		             url: "mainpage_sysmsglist.action",
		             dataType: "json",
		             success: function(data){
		             			 var num = data[0].TOTAL;
		             			 var processNum = data[0].PROCESSMSG;
		             			  var sysmsgNum = data[0].SYSMSG;
		                   		 var html = '';
				                   html+='<ul class="dropdown-menu dropdown-navbar">';
				                   		html+='<li>';
													html+='<a href="javascript:setRedirect(\'\',\'process_desk_notice.action\')" class="clearfix">';
													html+='<div class="clearfix">';
													html+='<span class="pull-left">';
													html+='<i class="btn btn-xs no-hover btn-pink fa fa-bullhorn"></i>';
													html+='　<b><s:text name="home.mini.notice.process"/></b>';
													html+='</span>';
													html+='<span class="pull-right badge badge-info">+'+processNum+'</span>';
													
													html+='</div">';
										html+='</li>';
										html+='<li>';
										html+='<a href="javascript:setRedirect(\'\',\'sysmsg_index.action\')" class="clearfix">';
													html+='<div class="clearfix">';
													html+='<span class="pull-left">';
													html+='<i class="btn btn-xs no-hover btn-pink fa fa-bell-o"></i>';
													html+='　<b><s:text name="home.mini.notice.sysmessage"/></b>';
													html+='</span>';
													html+='<span class="pull-right badge badge-info">+'+sysmsgNum+'</span>';
													html+='</div">';
										html+='</li>';
				                   html+='</ul>';
				                   $("#sysmsgNumTips").text(num);
				                   $("#sysmsgNum").text(num);
				                   $("#sysmsgList").html(html);
		                  
		             }
		         });
		         //获取内部邮件列表
			}
			function loademailTip(){
				 //获取内部邮件列表
				$.ajax({
					type: "GET",
					url: "mainpage_emaillist.action",
		            dataType: "json",
		            success: function(data){
		        		var mailNum = data[0].MAILNUM;
		        	 	var html = '';
		        	 	html+='<ul class="dropdown-menu dropdown-navbar">';
		                var dataRows = data[0].DATAROWS;
		                var i = 0;
		                for (var row in dataRows){
		                	i++;
		                   	html+='<li>';
							html+='<a href="javascript:openEmailDetailInfo(\''+dataRows[row].ID+'\',\''+dataRows[row].TASKID+'\',\''+dataRows[row].TITLE+'\','+dataRows[row].EMAILTYPE+')" class="clearfix">';
							html+='<img   onerror="this.src=\'iwork_img/MailUnread.gif\'" src="iwork_file/USER_PHOTO/'+dataRows[row].OWNERUSERID+'.jpg"  class="msg-photo" />';
							html+='<span class="msg-body">';
							html+='<span class="msg-title">';
							html+=dataRows[row].TITLE+"-"+dataRows[row].MAILFROM+"-"+dataRows[row].CREATETIME;
							html+='</span>';
							html+='</span>';
							html+='</a>';
							html+='</li>';
						}
		                html+='</ul>';
		        	 	$("#mailNumTips").text(mailNum);
		                $("#mailNum").text(mailNum);
		                $("#mailNumList").html(html);
					}
				});
			}
			function addTab(subtitle,url,icon){ 
			   // var pageurl = "url:"+url;
			    setRedirect('',url);
				//openWin(subtitle,600,800,pageurl);
			}
			function openWin(title,height,width,pageurl,location,dialogId){
			if(dialogId=='undefined')dialogId="mainDialogWin"; 
			
			art.dialog.open(pageurl,{
					id:dialogId, 
					title:title,  
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:width,
				    height:height,
				    close:function(){
				    	if(location!=null){
				    		location.reload();
				    	}
				    }
				 });
				 
		}
		
		function showOnline(){
				var pageUrl = "showUserOnlineList.action";
				art.dialog.open(pageUrl,{
					id: 'userOnline', 
					title:"在线用户列表", 
					lock:true,
					left:'99%',
					top:'99%',
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width: 200, 
				    height: 400
				 });
				return;
		}
		function reloadWorkList(){
			  loadtodoTip();
		}
		</script>
		<style type="text/css">
			.gritter-diy{
				width:150px;
				background:#666;
				color:#fff;
				padding-left:20px;
			}
			.loading-container {
				z-index:2000;
				position:fixed;
				top:0;
				left:0;
				right:0;
				bottom:0;
				background:#efefef	;
			}
			.loading-container.loading-inactive {
				display:none
			}
			.loading-container.loading-inactive .loader {
				display:none
			}
			.loader{
				background-image:url(../iwork_img/login/single/loading.gif);
				 background-repeat:no-repeat;
			}
			.loading-container .loader {
				width:200px; 
				height:32px;
				margin:50vh auto;
				padding-left:20px;
				position:relative;
			}
		</style>
	</head>

	<body class="skin-3 no-skin">
	<!-- Loading Container -->
    <div class="loading-container">
        <div class="loader"><s:text name="login.loading.tips"/></div>
    </div>
		<!-- #section:basics/navbar.layout -->
		<div id="navbar" class="navbar navbar-default navbar-fixed-top"  style="background:#4169E1">
			<div class="navbar-container" id="navbar-container">
				<!-- #section:basics/sidebar.mobile.toggle -->
				<button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler" data-target="#sidebar">
					<span class="sr-only">Toggle sidebar</span>

					<span class="icon-bar"></span>

					<span class="icon-bar"></span>

					<span class="icon-bar"></span>
				</button>

				<!-- /section:basics/sidebar.mobile.toggle -->
				<div class="navbar-header pull-left" >
					<!-- #section:basics/navbar.layout.brand -->
					<a href="mainAction.action" class="navbar-brand">
						<small >
							<i class="fa fa-desktop"></i>
							<s:text name="system.name"/>
						</small>
					</a>
					
					<!-- /section:basics/navbar.layout.brand -->
					<!-- #section:basics/navbar.toggle -->
					<!-- /section:basics/navbar.toggle -->
				</div>
				<div  class="navbar-buttons navbar-header pull-center" >
								<s:property value="topMenuHtml" escapeHtml="false"/>
				</div>
				<!-- #section:basics/navbar.dropdown -->
				<div class="navbar-buttons navbar-header pull-right" style="text-align:right" role="navigation">
					<ul class="nav ace-nav"> 
					   <li class="blue">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#">
								<i class="ace-icon fa fa-tasks"></i>
								<span class="badge badge-grey"  id="todoNumTips">0</span>
							</a>

							<ul class="dropdown-menu-right dropdown-navbar dropdown-menu dropdown-caret dropdown-close">
								<li class="dropdown-header">
									<i class="ace-icon fa fa-check"></i>
									<span id="todoNum">0</span> <s:text name="home.mini.todo.label"/>
								</li>
								<li class="dropdown-content" >
									<span id="todolist">
									</span>
								</li>
								<li class="dropdown-footer">
									<a href="javascript:setRedirect('','process_desk_index.action')">
										 <s:text name="home.mini.todo.alllink"/>
										<i class="ace-icon fa fa-arrow-right"></i>
									</a>
								</li>
							</ul>
						</li>
						<li class="purple">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#">
								<i class="ace-icon fa fa-bell icon-animated-bell"></i>
								<span class="badge badge-important" id="sysmsgNumTips">0</span>
							</a>

							<ul class="dropdown-menu-right dropdown-navbar navbar-pink dropdown-menu dropdown-caret dropdown-close">
								<li class="dropdown-header">
									<i class="ace-icon fa fa-exclamation-triangle"></i>
									<span id="sysmsgNum">0</span> <s:text name="home.mini.notice.label"/>
								</li>
								<li class="dropdown-content" id="sysmsgList">
									
								</li>

								<li class="dropdown-footer">
								</li>
							</ul>
						</li>

						<li class="green">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#">
								<i class="ace-icon fa fa-envelope icon-animated-vertical"></i>
								<span class="badge badge-success" id="mailNumTips">0</span>
							</a>

							<ul class="dropdown-menu-right dropdown-navbar dropdown-menu dropdown-caret dropdown-close">
								<li class="dropdown-header">
									<i class="ace-icon fa fa-envelope-o"></i>
									<span  id="mailNum">0</span>未读邮件
								</li>

								<li class="dropdown-content"  id="mailNumList">
									
								</li>

								<li class="dropdown-footer">
								<a href="javascript:setRedirect('','iwork_email_index.action')">
										查看全部邮件
										<i class="ace-icon fa fa-arrow-right"></i>
									</a>
								</li>
							</ul>
						</li>

						<!-- #section:basics/navbar.user_menu -->
						<li class="light-blue">
							<a data-toggle="dropdown" href="#" class="dropdown-toggle">
									<s:property value="currentUserStr" escapeHtml="false"/>
								<i class="ace-icon fa fa-caret-down"></i>
							</a>
							<ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
								
								
								<li>
									<a  href="javascript:setRedirect('','syspersion_info.action')">
										<i class="ace-icon fa fa-user"></i>
										<s:text name="portal.topbar.set"></s:text>
									</a>
								</li>
								<li>
									<a  href="javascript:setRedirect('','persion_role_exchange.action')">
										<i class="ace-icon fa fa-exchange"></i>
										<s:text name="portal.role.set"></s:text>
									</a>
								</li>
								<li>
									<a  href="javascript:setRedirect('','syspersion_loadEntrustPage.action')">
										<i class="ace-icon fa glyphicon-share"></i>
										<s:text name="portal.topbar.entrct"></s:text>
									</a>
								</li>
								<li>
									<a  href="javascript:setRedirect('','syspersion_pwd_update.action')">
										<i class="ace-icon fa fa-lock"></i>
										<s:text name="portal.topbar.password"/>
									</a>
								</li>
								<li>
									<a  href="javascript:showOnline()">
										<i class="ace-icon fa fa-users"></i><s:text name="portal.topbar.online"></s:text>
										
									</a>
								</li>
								<li>
									<a  href="javascript:setRedirect('','syspersion_loadOtherParamPage.action')">
										<i class="ace-icon fa fa-cog"></i>
										<s:text name="portal.topbar.params"></s:text>
									</a>
								</li>
								<li class="divider"></li>

								<li> 
									<a href="#" id="loginOut">
										<i class="ace-icon fa fa-power-off"></i>
										<s:text name="portal.topbar.exit"></s:text>
									</a>
								</li>
							</ul>
						</li>

						<!-- /section:basics/navbar.user_menu -->
					</ul>
				</div>

				<!-- /section:basics/navbar.dropdown -->
			</div><!-- /.navbar-container -->
		</div>

		<!-- /section:basics/navbar.layout -->
		<div class="main-container" id="main-container">
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>
			<!-- #section:basics/sidebar -->
			<div id="sidebar" class="sidebar  responsive sidebar-fixed">

				<div class="sidebar-shortcuts" id="sidebar-shortcuts">
					<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
						
						<button class="btn btn-success" title="流程发起中心" onclick="setRedirect('','processLaunchCenter!index.action')">
							<i class="ace-icon fa fa-pencil-square-o"></i>
						</button>
						<button class="btn btn-info" title="流程处理中心"  onclick="setRedirect('','process_desk_index.action')">
							<i class="ace-icon fa fa-check-square-o"></i>
						</button>

						<!-- #section:basics/sidebar.layout.shortcuts -->
						<button class="btn btn-warning"  title="报表中心" onclick="setRedirect('','ireport_rt_showcenter.action')">
							<i class="ace-icon fa fa-bar-chart-o"></i>
						</button>

						<button class="btn btn-danger"  title="鹰眼检索" onclick="setRedirect('','eaglesSearch_Index.action')">
							<i class="ace-icon fa fa-eye"></i>
						</button>

						<!-- /section:basics/sidebar.layout.shortcuts -->
					</div>
					<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
						<span class="btn btn-success"  onclick="setRedirect('processLaunchCenter!index.action')"></span>

						<span class="btn btn-info"></span>

						<span class="btn btn-warning"></span>

						<span class="btn btn-danger"></span>
					</div>
				</div><!-- /.sidebar-shortcuts -->
<s:property value="leftMenuHtml" escapeHtml="false"/>
				<!-- /.nav-list -->

				<!-- #section:basics/sidebar.layout.minimize -->
				<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
					<i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
				</div>

				<!-- /section:basics/sidebar.layout.minimize -->
				<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
				</script> 
			</div>
			<!-- /section:basics/sidebar -->
			<div class="main-content">
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="ace-icon fa fa-home home-icon"></i>
							<a href="#">首页</a>
						</li>
						<li id="meunTitle" class="active"></li>
					</ul><!-- /.breadcrumb -->
					<!-- /section:basics/content.searchbox -->
				</div>

				
				<div class="page-content" style="padding:2px;">
					<iframe  id="ifm" name="ifm" style="background-color:#efefef;"  scrolling="auto" src="iworkMainPage.action?channelid=0"  frameborder="0" height="400"  width="100%"></iframe>
				</div>
			</div><!-- /.main-content -->
<!--
			<div class="footer">
				<div class="footer-inner">
					<div class="footer-content">
						<span class="bigger-100">
							<span class="blue bolder">HiBPM</span>
							Application &copy; 2010-2015
						</span>
					</div>
				</div>
			</div> -->

			<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
				<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
			</a>
		</div><!-- /.main-container -->

		<!-- basic scripts -->

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='../assets/js/jquery.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='../assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='../assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<script src="../assets/js/bootstrap.js"></script>
		<!-- page specific plugin scripts -->
		<!--[if lte IE 8]>
		  <script src="../assets/js/excanvas.js"></script>
		<![endif]-->
		<script src="../assets/js/jquery-ui.custom.js"></script>
		<script src="../assets/js/jquery.ui.touch-punch.js"></script>
		<script src="../assets/js/jquery.gritter.js"></script>  
		<!-- ace scripts -->
		<script src="../assets/js/ace/elements.scroller.js"></script>
		<script src="../assets/js/ace/elements.fileinput.js"></script>
		<script src="../assets/js/ace/elements.typeahead.js"></script>
		<script src="../assets/js/ace/elements.wysiwyg.js"></script>
		<script src="../assets/js/ace/elements.spinner.js"></script>
		<script src="../assets/js/ace/elements.treeview.js"></script>
		<script src="../assets/js/ace/elements.wizard.js"></script>
		<script src="../assets/js/ace/elements.aside.js"></script>
		<script src="../assets/js/ace/ace.js"></script>
		<script src="../assets/js/ace/ace.ajax-content.js"></script>
		<script src="../assets/js/ace/ace.touch-drag.js"></script>
		<script src="../assets/js/ace/ace.sidebar.js"></script>
		<script src="../assets/js/ace/ace.sidebar-scroll-1.js"></script>
		<script src="../assets/js/ace/ace.submenu-hover.js"></script>
		<script src="../assets/js/ace/ace.widget-box.js"></script>
		<script src="../assets/js/ace/ace.settings.js"></script>
		<script src="../assets/js/ace/ace.settings-rtl.js"></script>
		<!--<script src="../assets/js/ace/ace.settings-skin.js"></script>-->
		<script src="../assets/js/ace/ace.widget-on-reload.js"></script>
		<script src="../assets/js/ace/ace.searchbox-autocomplete.js"></script>
		<!-- inline scripts related to this page -->
		<script type="text/javascript">
			jQuery(function($) {
			$('#tabs2 li:eq(1) a').tab('show');
			$("#searcherTxt").keydown(function(){
					e = event ? event :(window.event ? window.event : null); 
					if(e.keyCode==13){ 
					//执行的方法 
							var searchTxt = $('#searcherTxt').val();
							if(searchTxt==null||searchTxt==""){
								//alert("请输入您要查询的内容");
								showTips('请输入您要查询的内容...'); 
								return ;
							}  
							showTips('正在检索,请稍候...'); 
							$('#form-search').attr('action','eaglesSearch_Do.action');
							$('#form-search').attr('target','ifm');
							$("#form-search").submit(); 
							return ;
						} 
					
								
				});
		     loadtodoTip()
			 loadsysmsgTip();
			 loademailTip();
				$('#loginOut').click(function() {
		               if(confirm('您确定要退出本次登录吗?')){
		                        $.ajax({
		        	       			url:'logout.action', //后台处理程序
		        	       			type:'post',         //数据发送方式
		        	       			dataType:'json'
		             			});
		                    location.href = 'login.action';
		                }
		            })
		      try{
			  	$.resize.throttleWindow = false;
			  }catch(e){}
				/////////////////////////////////////
				$(document).one('ajaxloadstart.page', function(e) {
					$tooltip.remove();
				});
				var d1 = [];
				for (var i = 0; i < Math.PI * 2; i += 0.5) {
					d1.push([i, Math.sin(i)]);
				}
			
				var d2 = [];
				for (var i = 0; i < Math.PI * 2; i += 0.5) {
					d2.push([i, Math.cos(i)]);
				}
			
				var d3 = [];
				for (var i = 0; i < Math.PI * 2; i += 0.2) {
					d3.push([i, Math.tan(i)]);
				}
			
				$('.dialogs,.comments').ace_scroll({
					size: 300
			    });
				
				var agent = navigator.userAgent.toLowerCase();
				if("ontouchstart" in document && /applewebkit/.test(agent) && /android/.test(agent))
				  $('#tasks').on('touchstart', function(e){
					var li = $(e.target).closest('#tasks li');
					if(li.length == 0)return;
					var label = li.find('label.inline').get(0);
					if(label == e.target || $.contains(label, e.target)) e.stopImmediatePropagation() ;
				});
			
				$('#tasks').sortable({
					opacity:0.8,
					revert:true,
					forceHelperSize:true,
					placeholder: 'draggable-placeholder',
					forcePlaceholderSize:true,
					tolerance:'pointer',
					stop: function( event, ui ) {
						//just for Chrome!!!! so that dropdowns on items don't appear below other items after being moved
						$(ui.item).css('z-index', 'auto');
					}
					}
				);
				$('#tasks').disableSelection();
				$('#tasks input:checkbox').removeAttr('checked').on('click', function(){
					if(this.checked) $(this).closest('li').addClass('selected');
					else $(this).closest('li').removeClass('selected');
				});
			
			
				//show the dropdowns on top or bottom depending on window height and menu position
				$('#task-tab .dropdown-hover').on('mouseenter', function(e) {
					var offset = $(this).offset();
			
					var $w = $(window)
					if (offset.top > $w.scrollTop() + $w.innerHeight() - 100) 
						$(this).addClass('dropup');
					else $(this).removeClass('dropup');
				});
				
			})
		</script>
 
		<!-- the following scripts are used in demo only for onpage help and you don't need them -->
		<link rel="stylesheet" href="../assets/css/ace.onpage-help.css" />
		<link rel="stylesheet" href="../docs/assets/js/themes/sunburst.css" />

		<script type="text/javascript"> ace.vars['base'] = '..'; </script>
		<script src="../assets/js/ace/elements.onpage-help.js"></script>
		<script src="../assets/js/ace/ace.onpage-help.js"></script>
		<script src="../docs/assets/js/rainbow.js"></script>
		<script src="../docs/assets/js/language/generic.js"></script>
		<script src="../docs/assets/js/language/html.js"></script>
		<script src="../docs/assets/js/language/css.js"></script>
		<script src="../docs/assets/js/language/javascript.js"></script>
	</body>
</html>
<script type="text/javascript">
	setCwinHeight($("#ifm"));
	window.onresize = function(){
		setCwinHeight($("#ifm"));
	}
	/*Loading*/
			$(window).load(function () {
			        setTimeout(function () {
			            $('.loading-container')
			                .addClass('loading-inactive');
			        }, 800);
			    });
	
</script>