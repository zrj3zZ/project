<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="zh-cn">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="description" content="DSP数字化服务平台" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
	<style type="text/css"> 
		body {  
		     font-family: 宋体,Geneva, Arial, Helvetica, sans-serif;  
		     font-size:   80%;  
		     *font-size:  80%;   
		 }  
    </style> 
	<title><s:text name="system.name"/></title>
	  <!-- ZUI 标准版压缩后的 CSS 文件 -->
	<link rel="stylesheet" href="//cdn.bootcss.com/zui/1.5.0/css/zui.min.css">
		<link rel="stylesheet" href="iwork_layout/zui/css/zui-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="iwork_layout/zui/css/bootstrap-addtabs.css" type="text/css" media="screen" />
		<!-- ace styles -->
	<!-- ZUI Javascript 依赖 jQuery -->
	<script src="//cdn.bootcss.com/zui/1.5.0/lib/jquery/jquery.js"></script>
	<!-- ZUI 标准版压缩后的 JavaScript 文件 -->
	<script src="//cdn.bootcss.com/zui/1.5.0/js/zui.min.js"></script>
	<script src="iwork_layout/zui/js/bootstrap-addtabs.js"></script>
	<!--[if lt IE 9]>
	      <script src="iwork_layout/zui/lib/ieonly/html5shiv.js"></script>
	      <script src="iwork_layout/zui/lib/ieonly/respond.js"></script>
	      <script src="iwork_layout/zui/lib/ieonly/excanvas.js"></script>
	 <![endif]-->
    <script type="text/javascript">
    	$(document).ready(function(){
			dropdownOpen();//调用
		});
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
   
            $(function(){
                $('#tabs').addtabs({monitor:'.topbar'});
                $('#save').click(function(){
                    Addtabs.add({
                       id: $(this).attr('addtabs'),
                       title: $(this).attr('title') ? $(this).attr('title') : $(this).html(),
                       content: Addtabs.options.content ? Addtabs.options.content : $(this).attr('content'),
                       url: $(this).attr('url'),
                       ajax: $(this).attr('ajax') ? true : false
                    })
                });
            })
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
													html+='　<b>流程抄送/会签</b>';
													html+='</span>';
													html+='<span class="pull-right badge badge-info">+'+processNum+'</span>';
													
													html+='</div">';
										html+='</li>';
										html+='<li>';
										html+='<a href="javascript:setRedirect(\'\',\'sysmsg_index.action\')" class="clearfix">';
													html+='<div class="clearfix">';
													html+='<span class="pull-left">';
													html+='<i class="btn btn-xs no-hover btn-pink fa fa-bell-o"></i>';
													html+='　<b>系统消息</b>';
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
        	.logo{
        		background-image:url(iwork_img/but_logo_kzmb.jpg);
        		width:164px;
        		height:164px;
        		background-color:red;
        	}
        </style>
        
</head>
<body >
<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
  <div class="container-fluid">
    <!-- 导航头部 -->
    <div class="navbar-header">
        <a class="navbar-brand" href="#"><span class="logo">&nbsp;</span>DSP数字化服务平台</a>
    </div>
    <!-- 导航项目 -->
    <div class="collapse navbar-collapse navbar-collapse-example">
       <!-- 一般导航项目 -->
      <ul class="nav navbar-nav">
        <li class="active"><a href="your/nice/url" STYLE="font-size:16px">首页</a></li>
        <li><a href="your/nice/url"  STYLE="font-size:16px">审批</a></li>
		<li><a href="your/nice/url"  STYLE="font-size:16px">流程发起</a></li>
        <!-- 导航中的下拉菜单 -->
        <li class="dropdown">
          <a href="your/nice/url" class="dropdown-toggle" data-toggle="dropdown"  STYLE="font-size:16px">发起流程 <b class="caret"></b></a>
          <ul class="dropdown-menu" role="menu">
            <li>
            	<div>
            		<div class="btn-group btn-group-justified">
					  <div class="btn-group">
					    <button type="button" class="btn btn-default">Left</button>
					  </div>
					  <div class="btn-group">
					    <button type="button" class="btn btn-default">Middle</button>
					  </div>
					  <div class="btn-group">
					    <button type="button" class="btn btn-default">Right</button>
					  </div>
					</div>
            	</div>
            </li>
          </ul>
        </li>
      </ul>
      
      <!-- 右侧的导航项目 -->
      <ul class="nav navbar-nav navbar-right">
        <li><a href="your/nice/url">帮助</a></li>
        <li class="dropdown">
          <a href="your/nice/url" class="dropdown-toggle" data-toggle="dropdown">超级管理员 <b class="caret"></b></a>
          <ul class="dropdown-menu" role="menu">
            <li>
									<a  href="javascript:setRedirect('','syspersion_info.action')">
										<i class="ace-icon fa fa-user"></i>
										个人设置
									</a>
								</li>
								<li>
									<a  href="javascript:setRedirect('','persion_role_exchange.action')">
										<i class="ace-icon fa fa-exchange"></i>
										角色切换
									</a>
								</li>
								<li>
									<a  href="javascript:setRedirect('','syspersion_loadEntrustPage.action')">
										<i class="ace-icon fa glyphicon-share"></i>
										委托设置
									</a>
								</li>
								<li>
									<a  href="javascript:setRedirect('','syspersion_pwd_update.action')">
										<i class="ace-icon fa fa-lock"></i>
										密码设置
									</a>
								</li>
								<li>
									<a  href="javascript:showOnline()">
										<i class="ace-icon fa fa-users"></i>在线用户
										
									</a>
								</li>
								<li>
									<a  href="javascript:setRedirect('','syspersion_loadOtherParamPage.action')">
										<i class="ace-icon fa fa-cog"></i>
										参数设置
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
      </ul>
    </div><!-- END .navbar-collapse -->
  </div>
</nav>
  <div class="col-md-12">
            <div id="tabs">
                <!-- Nav tabs -->
                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">Home</a></li>                    
                </ul>
                <!-- Tab panes -->
                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane active" id="home">
                        <button type="button" class="btn btn-default" addtabs="save" id="save" url="/admin/save">
                            <i class="glyphicon glyphicon-floppy-disk"></i>
                            SAVE                            
                        </button>
                    </div>                    
                </div>

            </div>

        </div>
</body>
</html>