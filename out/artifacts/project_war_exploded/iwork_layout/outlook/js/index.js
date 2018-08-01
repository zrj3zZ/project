	$(function(){
			$('a.basic').cluetip({
				cluetipClass: 'rounded', 
				dropShadow: false, 
				positionBy: 'mouse',
				showTitle: false,
				cursor:''
			});
			
			var setting_fav = {
					async: {
						enable: true, 
						url:"favjson.action",
						dataType:"json",
						autoParam:["id","nodeType"]
					}, 
					callback: {
						onClick:onClickFav
					} 
				}; 
			$.fn.zTree.init($("#favtree"), setting_fav);//加载收藏夹树
			
			//自动设置右侧关闭
			var p = $('body').layout('panel','west').panel({
				onCollapse:function(){
				}
			});
             //双击关闭tab
             tabClose();
             //注册邮件菜单
			 tabCloseEven();
			 //本地时钟
			 clockon();
		});
	//点击收藏夹
	function onClickFav(event, treeId, treeNode, clickFlag){
			if(treeNode.isParent){
				var zTree = $.fn.zTree.getZTreeObj("favtree");
		 		zTree.expandNode(treeNode, true, null, null, true); 
			}else{
				var pageUrl = ""; 
				if(treeNode.target=='mainFrame'){
					pageUrl = treeNode.pageurl;
					addTab(treeNode.name,pageUrl,treeNode.icon);
				}else{
					pageUrl = treeNode.pageurl;
					var newWin=treeNode.target;	
					window.open(pageUrl,newWin,'location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=no,resizable=no,width=800 height=380');
				}
			}
	}
			//点击事件
			function onClick(event, treeId, treeNode, clickFlag){
					if(treeNode.isParent){
						var zTree = $.fn.zTree.getZTreeObj("navtree");
				 		zTree.expandNode(treeNode, true, null, null, true);
					}else{ 
						var pageUrl = ""; 
						if(treeNode.nodeType=='SYS'){
							pageUrl = treeNode.pageurl;
						}else{
							pageUrl = treeNode.pageurl;
						}
						
						 if(typeof(pageUrl)!='undefined'){
							 addTab(treeNode.name,pageUrl,treeNode.icon);
						 }else{
							 var setting = {
										async: {
											enable: true, 
											url:"openjson.action",
											dataType:"json",
											autoParam:["id","nodeType"]
										},
										callback: {
											onClick:onClick
										} 
							 };
							 $.fn.zTree.init($("#navtree"), setting);//加载导航树
						 }
					}
			}
		//打开收藏夹
		function arrcoll(){
				var pageUrl = "myfav.action";	
				art.dialog.open(pageUrl,{ 
					id:'fav_show',
					cover:true,
					title:'整理收藏夹',
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:680,
					cache:false,
					lock: true,
					height:480, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false
				});
		}
		//添加页签
		function addTab(subtitle,url,li){ 
			if(!$('#mainFrameTab').tabs('exists',subtitle)){
				$('#mainFrameTab').tabs('add',{
					title:subtitle,
					content:createFrame(url),
					closable:true
				});
			}else{
				$('#mainFrameTab').tabs('select',subtitle);
			}
			$("#Z_SubList").hide();
			return false;
		}
		function createFrame(url)
		{
			var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
			return s;
		}
		function tabClose()
		{ 
			/*双击关闭TAB选项卡*/
			$(".tabs-inner").dblclick(function(){
				var subtitle = $(this).children(".tabs-closable").text();
				$('#mainFrameTab').tabs('close',subtitle);
			})
			/*为选项卡绑定右键*/
			$(".tabs-inner").bind('contextmenu',function(e){
				$('#mm').menu('show', {
					left: e.pageX,
					top: e.pageY
				});
				
				var subtitle =$(this).children(".tabs-closable").text();
				
				$('#mm').data("currtab",subtitle);
				$('#mainFrameTab').tabs('select',subtitle);
				return false;
			});
		}
				//绑定右键菜单事件
			function tabCloseEven(){
					//关闭当前
					$('#mm-tabclose').click(function(){
						var currtab_title = $('#mm').data("currtab");
						$('#mainFrameTab').tabs('close',currtab_title);
					})
					//全部关闭
					$('#mm-tabcloseall').click(function(){
						$('.tabs-inner span').each(function(i,n){
							var t = $(n).text();
							$('#mainFrameTab').tabs('close',t);
						});	
					});
					//关闭除当前之外的TAB
					$('#mm-tabcloseother').click(function(){
						var othertabs = $('.tabs-selected').siblings();
						if(othertabs.length>0){
							othertabs.each(function(i,n){
								var t=$('a:eq(0) span',$(n)).text();
								$('#mainFrameTab').tabs('close',t);
							});
							return false;
						}
					});
					//关闭当前右侧的TAB
					$('#mm-tabcloseright').click(function(){
						var nextall = $('.tabs-selected').nextAll();
						if(nextall.length==0){
							//msgShow('系统提示','后边没有啦~~','error');
							alert('已经到最后了');
							return false;
						}
						nextall.each(function(i,n){
							var t=$('a:eq(0) span',$(n)).text();
							$('#mainFrameTab').tabs('close',t);
						});
						return false;
					});
					//关闭当前左侧的TAB
					$('#mm-tabcloseleft').click(function(){
						var prevall = $('.tabs-selected').prevAll();
						if(prevall.length==0){
							return false;
						}
						prevall.each(function(i,n){
							var t=$('a:eq(0) span',$(n)).text();
							$('#mainFrameTab').tabs('close',t);
						});
						return false;
					});
				
					//退出
					$("#mm-exit").click(function(){
						$('#mm').menu('hide');
					})
				}
				
				//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
				function msgShow(title, msgString, msgType) {
					$.messager.alert(title, msgString, msgType);
				}
				//加载TOP区快捷菜单
				 $(function() {
					$('#sysmsg').click(
						function(){
							addTab('系统消息','sysmsg_index.action',''); 
							return false;
						});
					$('#workflow').click(
						function(){
							addTab('流程处理中心','processManagementCenter!index.action','');
							return false;
						});	
					$('#eaglesSearch').click(
						function(){
							addTab('鹰眼检索','eaglesSearch_Index.action','');
							return false;
						});	
					$('#personConfig').click(
						function(){
							addTab('个人设置','syspersion_info.action','');
							return false;
						});	
		            $('#loginOut').click(function() {
		               if(confirm('您确定要退出本次登录吗?')){
		                        location.href = 'logout.action';
		                }
		            })
       			 });
				function add_portlet(){
					for(var i=0; i<3; i++){
						var p = $('<div/>').appendTo('body');
						p.panel({
							title:'Title'+i,
							content:'<div style="padding:5px;">Content'+(i+1)+'</div>',
							height:100,
							closable:true,
							collapsible:true
						});
						$('#pp').portal('add_portlet', {
							panel:p,
							columnIndex:i
						});
					}
					$('#pp').portal('resize');
				}
				function remove(){
					$('#pp').portal('remove',$('#pgrid'));
					$('#pp').portal('resize');
				}
				//本地时钟
				function clockon() {
				    var now = new Date();
				    var year = now.getFullYear(); //getFullYear getYear
				    var month = now.getMonth();
				    var date = now.getDate();
				    var day = now.getDay();
				    var hour = now.getHours();
				    var minu = now.getMinutes();
				    var sec = now.getSeconds();
				    var week;
				    month = month + 1;
				    if (month < 10) month = "0" + month;
				    if (date < 10) date = "0" + date;
				    if (hour < 10) hour = "0" + hour;
				    if (minu < 10) minu = "0" + minu;
				    if (sec < 10) sec = "0" + sec;
				    var arr_week = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
				    week = arr_week[day];
				    var time = "";
				    time = year + "年" + month + "月" + date + "日" + " " + hour + ":" + minu + ":" + sec + " " + week;
				    $("#bgclock").html(time);
				    var timer = setTimeout("clockon()", 200);
				}
		//系统消息
		function showTips(type) {
			if(type=='workflow'){
				$.ajax({
	       			url:'showTipsInfo.action', //后台处理程序
	       			type:'post',         //数据发送方式
	       			dataType:'json',     //接受数据格式
	       			data:{type:'workflow'},
	       			success:updateTips //回传函数(这里是函数名)
     			}); 
			}else if(type=='sysmsg'){
				$.ajax({
	       			url:'showTipsInfo.action', //后台处理程序
	       			type:'post',         //数据发送方式
	       			data:'type=sysmsg',
	       			dataType:'json',     //接受数据格式
	       			success:updateTips //回传函数(这里是函数名)
     			});
			}
			
		}
		function updateTips(json){
			if(json.type=='sysmsg'){
				$('#sysmsg').html('(' + json.count + ')');
			}else if(json.type=='workflow'){
				$('#workflow').html('(' + json.count + ')');
			}
		}
		
		//系统消息
		function popUpSysMsg() {
			$.ajax({
       			url:'sysMessageAction!popUpSysMsg.action', //后台处理程序
       			type:'post',         //数据发送方式
       			dataType:'json',     //接受数据格式
       			success:showSysMsg //回传函数(这里是函数名)
     		});
		}
		function showSysMsg(json) {			
			$('#sysmsg').html('(' + json.msgNums + ')');
			$.messager.lays(300, 200);
			var sysTitle = '<div><img src="../iwork_img/sysmsg/sys_msg_popup_logo.png">系统消息提醒</div>';
			var sysMsg = "<table width='100%' height='120'><tr><td height='20px' valign='top' align='center'>" + json.title + "</td></tr><tr><td valign='top' align='center'>" + json.content + "</td></tr></table>";
			sysMsg+="<hr style='FILTER: alpha(opacity=100,finishopacity=0,style=3)' width='100%' color=#987cb9 SIZE=2 >";
			sysMsg+="<table height='30' width='100%'><tr><td  valign='top' align='right'><img  style='vertical-align:middle' src='../iwork_img/sysmsg/user.gif'>" + json.userName + "&nbsp;<img style='vertical-align:middle' border='0' width='16' height='15' src='../iwork_img/sysmsg/logoSysMsg.gif'>(" + json.msgNums + ")</td></tr><tr height='2px'><td></td></tr></table>";
			$.messager.show(sysTitle, sysMsg);
		}
		function openMsgAndReadOne(id) {
			var numStr = $('#sysmsg').text();
			numStr = numStr.substring(numStr.indexOf('(') + 1, numStr.indexOf(')'));
			numStr = numStr - 1;
			$('#sysmsg').html('(' + numStr + ')');
			addTab('系统消息','sysMessageAction!listInit.action?id=' + id,'');
			$.messager.close();		
		}
		function showMainPage(){
			this.location = "mainAction.action";
		}
		function showOnline(){
				var url = "url:showUserOnlineList.action";
				art.dialog.open(url,{ 
				    id: 'userOnline', 
				    title: '在线用户列表', 
				    width: 200, 
				    height: 400, 
				    left: '100%', 
				    top: '90%',  
				    drag: false,  
				    resize: false 
				});
				return false;
		}
		
		function openWin(title,height,width,pageurl,location){
			art.dialog.open(pageurl,{ 
				   id:'groupWinDiv', 
					cover:true, 
					title:title,  
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:width,
					width:520,
					cache:false, 
					lock: true,
					esc: true,
					height:height,   
					iconTitle:false,  
					extendDrag:true,
					autoSize:true,
					close:function(){
					   location.reload();
					}
					
				});
		}