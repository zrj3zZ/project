	$(function(){
			//自动设置右侧关闭
			var p = $('body').layout('panel','west').panel({
				onCollapse:function(){
				}
			});
			//加载导航树
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
			/*
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
			$.fn.zTree.init($("#navtree"), setting);//加载导航树
			$.fn.zTree.init($("#favtree"), setting_fav);//加载收藏夹树
			*/
			//  add_portlet();
             //双击关闭tab
             tabClose();
             //注册邮件菜单
			 tabCloseEven();
			 //右下角弹出框
			 popUpSysMsg();  
			 //本地时钟
			 clockon();
			 //设置浏览器类型
			 setWebBrowser();
			 //导航菜单搜索条
			 $("#input_focus").change(function(){
					$('.search_btn3').attr('class','search_btn2');
					}).keyup(function(){  
				   if(event.keyCode == 13){    
					   dosearch();
				      $('.search_btn3').attr('class','search_btn2'); 
				   }   
			 });
			 
			 $("#search_btn").click(function(){
				 if($("#input_focus").val()!='输入菜单名搜索'){
					 $('.search_btn2').attr('class','search_btn3');
		              $("#input_focus").val("输入菜单名搜索"); 
		              $.fn.zTree.init($("#navtree"), setting);//加载导航树
					  if($("#input_focus").val()!=("#input_focus").defaultValue){  
		                 $("#input_focus").val() ==("#input_focus").defaultValue;
				      } 
				 }else{
					 $("#input_focus").focus();
				 }
			 });
			 $("#search_btn1").click(function(){
				  dosearch();
			      $('.search_btn3').attr('class','search_btn2'); 
			 });
			 $("#input_focus").focus(function(){
				  if($(this).val() ==this.defaultValue){  
	                  $(this).val("");            
				  } 
				$('.tree_sr_box').attr('class','tree_sr_box2'); 
			}).blur(function(){
				 if ($(this).val() == '') {
	                $(this).val(this.defaultValue);
	             }
				$('.tree_sr_box2').attr('class','tree_sr_box');  
			});
		});
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
	
	function dosearch(){
		var searchOrg = $("#input_focus").val();
		if(searchOrg==""||searchOrg=="输入菜单名搜索"){
			$("#input_focus").focus();
			return;
		}
		var url = "sysNode_dosearch.action?searchkey="+encodeURI(searchOrg);
		var setting = {
				async: {
					enable: true, 
					url:url, 
					dataType:"json",
					autoParam:["id","nodeType"] 
				}, 
				callback: { 
					onClick:onClick
				} 
			};
		$.fn.zTree.init($("#navtree"), setting);//加载导航树 
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
							 addTab(treeNode.name,pageUrl,"");
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
		//添加至收藏夹
		function addcoll(){
			var treeObj = $.fn.zTree.getZTreeObj("navtree");
			var nodes = treeObj.getSelectedNodes();
			if(nodes.length<1){
				$.dialog.tips('数据加载中...',600,'loading.gif');
				return;
			}
			for (var i=0, l=nodes.length; i<l; i++) {
				if(nodes[i].isParent){
					lhgdialog.tips("请选择功能菜单添加至收藏夹",2);
					break;
				}else{
					var url='addfav.action';
					document.forms[0].action=url;  
					document.forms[0].funid.value=nodes[i].id;
			        document.forms[0].funtext.value=nodes[i].name;
	                document.forms[0].target="hidden_frame";
	                document.forms[0].submit(); 
	 		        return false; 
				} 
			}
		}
		//刷新导航树
		function reloadnav(){
			var treeObj = $.fn.zTree.getZTreeObj("navtree");
			treeObj.reAsyncChildNodes(null,"refresh",true);
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
					autoSize:false,
					content:pageUrl
				});
		}
		//刷新收藏夹
		function reloadfav(){
            var treeObj = $.fn.zTree.getZTreeObj("favtree");
			treeObj.reAsyncChildNodes(null,"refresh",true);
		}
		//添加页签
		function addTab(subtitle,url,icon){ 
			if(!$('#mainFrameTab').tabs('exists',subtitle)){
				$('#mainFrameTab').tabs('add',{
					title:subtitle,
					content:createFrame(url),
					closable:true,
					icon:icon
				});
			}else{
				$('#mainFrameTab').tabs('select',subtitle);
			}
			//tabClose();
			return;
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
					$('#createCenter').click(
						function(){
							addTab('发起中心','processLaunchCenter!index.action','');
							return false;
						});
					$('#reportCenter').click(
							function(){
								addTab('报表中心','ireport_rt_showcenter.action',''); 
								return false;
							});
					$('#sysmsg').click(
							function(){
								addTab('系统消息','sysmsg_index.action',''); 
								return false;
							});
					$('#workflow').click(
						function(){
							addTab('流程处理中心','process_desk_index.action','');
							return false;
						});	
					$('#eaglesSearch').click(
						function(){
							addTab('鹰眼检索','eaglesSearch_Index.action','');
							return false;
						});	
					$('#userOnLine').click(
							function(){
								showOnline();
								return false;
							});	
					$('#personConfig').click(
						function(){
							addTab('个人设置','syspersion_info.action','');
							return false;
						});	
		            $('#loginOut').click(function() {
		            	exitOut();
		            })
       			 });
				 function exitOut(){
					 if(confirm('您确定要退出本次登录吗?')){
	                        $.ajax({
	        	       			url:'logout.action', //后台处理程序
	        	       			type:'post',         //数据发送方式
	        	       			dataType:'json'
	             			});
	                    location.href = 'login.action';
	                }
				 }
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
				    var arr_week = new Array(["星期日"], ["星期一"], ["星期二"], ["星期三"], ["星期四"], ["星期五"],["星期六"]);
				    week = arr_week[day];
				    var time = ""; 
				    time = year + ["年"] + month + ["月"] + date + ["日"] + " " + hour + ":" + minu + ":" + sec + " " + week;
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
			if(json!=null){ 
				$('#sysmsg').html('(' + json.msgNums + ')');
				var sysTitle = "温馨提示：";
				var sysMsg = "<div  class='tipDiv'><div class='tipTitle'>" + json.title + "</div><div  class='tipContent'>" + json.content + "</div></div>";
				art.dialog({
				    id: 'msg',
				    title: sysTitle,
				    content:sysMsg,
				    width: 300,
				    height: 140,
				    left: '100%',
				    top: '100%',
				    fixed: true,
				    drag: false,
				    resize: false
				})
			}
		}
		
		function openMsgAndReadOne(id) {
			var numStr = $('#sysmsg').text();
			numStr = numStr.substring(numStr.indexOf('(') + 1, numStr.indexOf(')'));
			numStr = numStr - 1;
			$('#sysmsg').html('(' + numStr + ')');
			addTab('系统消息','sysmsg_index.action?id='+id);
			$.messager.close();		
		}
		
		/**
		 * 重新加载首页
		 * @return
		 */
		function showMainPage(){
			this.location = "mainAction.action";
		}
		
		/**
		 * 加载在线用户
		 * @return
		 */
		function showOnline(){
				var url = "showUserOnlineList.action";
				art.dialog.open(pageUrl,{
					id: 'userOnline', 
					title: $lang["在线用户列表"], 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    left: '99%', 
				    top: '90%', 
				    width: 200, 
				    height: 400
				 });
				return false;
		}
		/**
		 * 弹出窗口
		 * @param title
		 * @param height
		 * @param width
		 * @param pageurl
		 * @param location
		 * @param dialogId
		 * @return
		 */
		function openWin(title,height,width,pageurl,location,dialogId){
			if(typeof(dialogId)=='undefined'){
				dialogId = "winDiv"
			}
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
		/**
		 * 执行鹰眼检索
		 * @return
		 */
		function eagleSearch(){
			var search = $("#searchKey").val();
			if(search==''){
				$("#searchKey").focus();
				return;
			}else{
				var pageURl = "eaglesSearch_Do.action?searchType=all&&searcherTxt="+encodeURI(search);
				addTab('鹰眼检索',pageURl,'');
				return;
			}
			return;
		}
		 function enterKey(){
				if (window.event.keyCode==13){
					eagleSearch();
					return;
				}
			} 
		
		