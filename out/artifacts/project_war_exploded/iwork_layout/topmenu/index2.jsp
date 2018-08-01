<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
	<head>
		<title><s:text name="system.name"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=8" >
		<link rel="stylesheet" href="../iwork_layout/topmenu/css/bootstrap-2.css" />
		<link rel="stylesheet" href="../iwork_layout/topmenu/css/jquery-ui.custom.css" />
		<link rel="stylesheet" href="../iwork_layout/topmenu/css/jquery.gritter.css" />
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css" /> 
		<link rel="stylesheet" href="../iwork_layout/topmenu/css/ace-2.css"/>
		<link rel="stylesheet" type="text/css" href="iwork_layout/topmenu/css/less/easyui-2.css">
		<link rel="stylesheet" href="../iwork_layout/topmenu/css/font-awesome.css" />
		<link rel="stylesheet" href="../iwork_layout/topmenu/css/font-awesome.min.css" />
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="admin/js/jquery.easyui.min.js"></script>
		
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>	
		<script type="text/javascript">
		$(document).ready(function(){
			var mod_menu=$(".mod-menu");//导航模块区
			var menu=function(){
				var menuItem=$(".menu-item li");//选择导航列表
				menuItem.each(function(){
					var _index=$(this).index();//获取当前选择菜单列表的索引
					$(this).mouseenter(function(){
						var y = $(this).position().top+1;//获取当前鼠标滑过的列表的顶部坐标
						$(".menu-cont").show();
						$(".menu-cont").css("top",y);//需要显示的对应索引内容
						$(this).addClass("mouse-bg").siblings().removeClass("mouse-bg");
						$(".menu-cont>div").eq(_index).show().siblings().hide();
					});
				});/*导航菜单菜单*/
				$(".mod-menu").mouseleave(function(){
					$(".menu-cont").hide();
					menuItem.removeClass("mouse-bg");
				})
			}//展开二级菜单	
				menu();//执行展开二级菜单函
				dropdownOpen();//调用
				$('#loginOut').click(function() {
	               if(confirm('您确定要退出本次登录吗?')){
	                        $.ajax({
	        	       			url:'logout.action', //后台处理程序
	        	       			type:'post',         //数据发送方式
	        	       			dataType:'json'
	             			});
	                    location.href = 'login.action';
	                }
		        });
				loadtodoTip()
				loadsysmsgTip();
				loademailTip();
				setWebBrowser();
				setInterval("popUpSysMsg()",480000);
				popUpSysMsg();
			});
		//系统消息
		function popUpSysMsg() {
			$.ajax({
       			url:'sysGGMessageAction!ggPopUpSysMsg.action', //后台处理程序
       			type:'post',         //数据发送方式
       			dataType:'json',     //接受数据格式
       			success:showSysMsg //回传函数(这里是函数名)
     		});
		}
		function showSysMsg(json) {
			var sysMsg = "<div class='tipDiv'><div class='tipTitle'>" + json.title + "</div><div  class='tipContent'>" + json.content + "</div></div>";
			var ti=json.lx;
			
			var dialog = art.dialog({
				title:ti,
				width: 300, 
			    height: 100,
				content: sysMsg,
				follow: document.getElementById('dialogMsg')
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
		function showtzgg(ggid){
			updckqk(ggid);
			var pageUrl = "zqb_announcement_notice_reply_add.action?ggid=" + ggid + "&hfqkid=0";
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
		
		function setWebBrowser(){
			var Sys = {}; 
	        var ua = navigator.userAgent.toLowerCase(); 
	        var s; 
	        (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] : 
	        (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] : 
	        (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] : 
	        (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] : 
	        (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
	        var browserType = 'IE';
	        //以下进行测试
	        if (Sys.firefox) browserType = 'Firefox'; 
	        if (Sys.ie)browserType = 'IE'; 
	        if (Sys.chrome)browserType = 'Chrome'; 
	        if (Sys.opera)browserType = 'Opera'; 
	        if (Sys.safari)browserType = 'Safari';  
	        var url = "setWebBrowserType.action";
	        $.post(url,{browserType:browserType},function(response){});
		}
			/**
			 * 鼠标划过就展开子菜单，免得需要点击才能展开
			 */
			function dropdownOpen() {
				var $dropdownLi = $('li.dropdown');
				$dropdownLi.mouseover(function() {
					$(this).addClass('open');
				}).mouseout(function() {
					$(this).removeClass('open');
				});
			}
			
			
			function setCwinHeight(obj){ 
			 var height =   $(window).height()-120; 
			 	$(obj).css("height",height);
			}
			
			function setMenuHtml(obj){ 
			 var width =   $(window).width()-320; 
			 	$("#subMenuArea").css("width",width);
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
				//初始化
				$("#ifm").attr("src",url);
			    $("#meunTitle").text(title);
			}
			function openTaskPage(title,actDefId,instanceId,excutionId,taskId){
				var url = 'loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId;
				var target = "iform_"+taskId;
				var win_width = window.screen.width-50;
				var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
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
													html+='<a href="javascript:setRedirect(\'\',\'process_desk_notice.action\',\'流程处理中心\')" class="clearfix">';
													html+='<div class="clearfix">';
													html+='<span class="pull-left">';
													html+='<i class="btn btn-xs no-hover btn-pink fa fa-bullhorn"></i>';
													html+='　<b><s:text name="home.mini.notice.process"/></b>';
													html+='</span>';
													html+='<span class="pull-right badge badge-info">+'+processNum+'</span>';
													
													html+='</div">';
										html+='</li>';
										html+='<li>';
										html+='<a href="javascript:setRedirect(\'\',\'sysmsg_index.action\',\'系统消息\')" class="clearfix">';
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
			
			function openWin(title,height,width,pageurl,location,dialogId){
			if(dialogId=='undefined')dialogId="mainDialogWin"; 
			art.dialog.open(pageurl,{
					id:dialogId, 
					title:title,  
					lock:true,
					background: '#999',
				    opacity: 0.87,
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
					background: '#999',
				    opacity: 0.87,
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
			.mainMenu{
				
			}
			.mainMenu li{
				 display:inline;
			}

			.mainMenu .submenu li{
			 display:none;
			}
			ul{margin:0;padding:0;list-style-type:none;}
				a{text-decoration:none;}
				
				/* mod-menu */
				.mold_open_hover,.mold_open{display:inline-block;width:10px;height:6px;margin-left:1px;position:relative;top:-2px;}
				.mold_open{background-position:0 -6px;}
				.mold_open_hover{background-position:0 0;}
				.mod-menu{position:relative;z-index:1000;left:-1px;margin-left:10px;margin-top:0px;}
				.submenu{min-height:500px;max-height:500px;overflow: auto}
				.menu-cont-list a:hover{text-decoration:underline;}
				.mod-menu .menu-item{width:220px;min-height:500px;border-top:solid 1px #f1f1f1;border-bottom:solid 2px #f1f1f1;position:relative;z-index:22;}
				.mod-menu .menu-item li{height:47px;line-height:47px;background:#fdfbfb;border-top:solid 1px #fdfbfb;}
				.mod-menu .menu-item li.mouse-bg{background:url(../image/mouse-menu-bg.jpg) no-repeat #fff;position:relative;z-index:22;margin-right:-4px;}
				.mod-menu .menu-item a{color:#000;font-size:16px;padding-left:33px;display:block;height:45px;border-top:solid 1px #f1f2f7;}
				.mod-menu .menu-item a:hover{ text-decoration:none;}
				.mod-menu .menu-cont{position:absolute;left:220px;top:1px;background:#fff;width:573px;border:solid 1px #fdfbfb;box-shadow:2px 0 10px rgba(119,119,119,0.25);z-index:20;}
				.mod-menu .menu-cont-list{padding:0 30px;}
				.mod-menu .menu-cont-list li{border-bottom:dotted 1px #f0bfd3;padding:10px 0;}
				.mod-menu .menu-cont-list li:last-child{border-bottom:none;}
				.mod-menu .menu-cont-list h3{font-size:14px;font-weight:700;}
				.mod-menu .menu-cont-list h3 a{color:#222;}
				.mod-menu .menu-list-link a{color:#666;line-height:24px;}
				.mod-menu .menu-list-link .long-string{color:#ccc;font-size:12px;padding:0 10px;}
			.breadcrumbs {
			  position: relative;
			  z-index: auto;
			  border-bottom: 1px solid #e5e5e5;
			  background-color: #f5f5f5;
			  min-height: 31px;
			  line-height: 30px;
			  padding: 0 12px 0 0;
			}
			.breadcrumb {
			  background-color: transparent;
			  display: inline-block;
			  line-height: 25px;
			  margin: 4px 22px 0 12px;
			  padding: 0;
			  font-size: 13px;
			  color: #333;
			  border-radius: 0;
			}
			.menu-item {
			    border-color: transparent;
			    height: 30px;
			    line-height:30px;
			    padding-top: 4px;
			}
			a.m-btn-plain-active {
			  background: transparent;
			  padding: 0 0 0 0;
			  border-width: 0;
			  border-style: solid;
			  -moz-border-radius: 0px 0px 0px 0px;
			  -webkit-border-radius: 0px 0px 0px 0px;
			  border-radius: 0px 0px 0px 0px;
			}
			.dropdown a{
			  border: 1px solid transparent;
			}
			.navbar-nav > li {
			    float: left;
			}
			.navbar-fixed-top + .main-container {
			  padding-top: 45px;
			}
		</style>
	</head>

	<body class="no-skin" style="overflow-y:hidden; overflow-x:hidden;">
    <div class="loading-container">
        <div class="loader"><s:text name="login.loading.tips"/></div>
    </div>
	<div id="navbar" class="navbar navbar-default navbar-fixed-top">
		<div class="navbar-container" id="navbar-container" style="padding-left: 0px;">
				<%-- <button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler" data-target="#sidebar">
					<span class="sr-only">Toggle sidebar</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button> --%>

			<div class="navbar-header pull-left" style="width:162px;height:46px;padding-top:3px;background-image:url(../iwork_img/login/single/logo.png);background-size:162px 46px;background-position:center center;background-repeat:no-repeat;">
				<a href="mainAction.action" class="navbar-brand">
					<%-- <small >
						<i class="fa fa-desktop"></i>
						<s:text name="system.name"/>
					</small> --%>
				</a> 
			</div> 
			<!-- 导航项目 -->
			<div class="collapse navbar-collapse navbar-collapse-example" style="width:100%;"> 
			<!-- 一般导航项目 --> 
				<ul class="nav navbar-nav"> 
					<li><a href="#" onclick="setRedirect('','iworkMainPage.action?channelid=0')" STYLE="font-size:16px"><i class="menu-icon fa fa-home"></i></a></li>
					<li class="dropdown">
						<a data-toggle="dropdown" class="dropdown-toggle" style="font-size:14px;cursor:pointer;font-family:宋体;"  onclick="setRedirect('','process_desk_index.action','流程处理中心')" >
							审批
						</a>
					</li>
					<s:property value="topMenuHtml" escapeHtml="false"/>
					<li class="dropdown" style="float:right;">
						<a data-toggle="dropdown" class="dropdown-toggle" href="#">
							<s:property value="currentUserStr" escapeHtml="false"/>
						</a>
						<ul class="dropdown-menu" role="menu">
							<li>
								<a  href="javascript:setRedirect('','syspersion_info.action','个人设置')">
									<i class="ace-icon fa fa-user"></i>
									个人设置
								</a>
							</li>
							<li class="divider"></li>
							<li>
								<a href="#" id="loginOut">
									<i class="ace-icon fa fa-power-off"></i>
									退出
								</a>
							</li>
						</ul>
					</li>
					<li class="dropdown" style="float:right;">
						<a data-toggle="dropdown" class="dropdown-toggle" href="#">
							<i class="ace-icon fa fa-bell icon-animated-bell"></i>
							<span class="badge badge-important" id="sysmsgNumTips">0</span>
						</a>
						<ul class="dropdown-menu-right dropdown-navbar  dropdown-menu dropdown-caret dropdown-close">
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
					<li class="dropdown" style="float:right;">
						<a data-toggle="dropdown" class="dropdown-toggle" href="#">
							<i class="ace-icon fa fa-tasks"></i>
							<span class="badge badge-grey"  id="todoNumTips">0</span>
						</a>
						<ul class="dropdown-menu" role="menu">
							<li class="dropdown-header">
								<i class="ace-icon fa fa-check blue"></i>
								<span id="todoNum">0</span> <s:text name="home.mini.todo.label"/>
							</li>
							<li class="dropdown-content" >
								<span id="todolist"></span>
							</li>
							<li class="dropdown-footer">
								<a href="javascript:setRedirect('','process_desk_index.action','流程处理中心')">
									<s:text name="home.mini.todo.alllink"/>
									<i class="ace-icon fa fa-arrow-right"></i>
								</a>
							</li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
	</div>

	<div class="main-container" id="main-container">
		<div class="main-content">
			<div class="breadcrumbs" id="breadcrumbs">
				<ul class="breadcrumb">
					<li>
						<i class="ace-icon fa fa-home home-icon"></i>
						<a href="mainAction.action">首页</a>
					</li>
					<li id="meunTitle" class="active"></li>
				</ul>
			</div>
			<div class="page-content" style="padding:2px;">
				<iframe id="ifm" name="ifm" style="background-color:#efefef;" scrolling="yes" frameborder="0" height="95%" width="100%" src="iworkMainPage.action?channelid=0"></iframe>
			</div>
		</div>
			<!-- <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
				<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
			</a> -->
	</div>
	<div id="dialogMsg" style="position:fixed;right:0;bottom:0;"><span>.</span></div>
	<script type="text/javascript">
		jQuery(function($) {
	     loadtodoTip()
		 loadsysmsgTip();
		 loademailTip();
	      try{
		  	$.resize.throttleWindow = false;
		  }catch(e){}
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
			
			var agent = navigator.userAgent.toLowerCase();
			if("ontouchstart" in document && /applewebkit/.test(agent) && /android/.test(agent))
			  $('#tasks').on('touchstart', function(e){
				var li = $(e.target).closest('#tasks li');
				if(li.length == 0)return;
				var label = li.find('label.inline').get(0);
				if(label == e.target || $.contains(label, e.target)) e.stopImmediatePropagation() ;
			});
			
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
			
		});
		</script>
 
	</body>
</html>
<script type="text/javascript">
	setCwinHeight($("#ifm"));
	setMenuHtml();
	window.onresize = function(){
		setCwinHeight($("#ifm"));
		setMenuHtml();
	}
	
			$(window).load(function () {
			        setTimeout(function () {
			            $('.loading-container')
			                .addClass('loading-inactive');
			        }, 800);
			    });
	
</script>