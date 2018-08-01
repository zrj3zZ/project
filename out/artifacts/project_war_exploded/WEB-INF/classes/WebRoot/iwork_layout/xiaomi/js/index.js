$(function(){
			//自动设置右侧关闭
			var p = $('body').layout('panel','west').panel({
				onCollapse:function(){
					alert('collapse');
				}
			});
			setTimeout(function(){
				$('body').layout('collapse','east');
			},0);
			
			//加载导航树
			$('#navtree').tree({   
                 url: 'openjson.action',   
               	 onBeforeExpand:function(node){
               	 	//alert(node.attributes.type);
                     $('#navtree').tree('options').url = "openjson.action?pid=" + node.id+"&navtype="+node.attributes.type;// change the url                       
                 },             
               onClick:function(node){
               	//	alert(node.state);
               		if(node.state=='closed'){
               			$(this).tree('toggle', node.target);
               			
               		}else if(node.state=='open'){
               			$(this).tree('toggle', node.target);
               		}else{
               			addTab(node.text,node.attributes.url,node.iconCls);
               		}      
               }
                 
             });
             
              //加载收藏夹
			$('#favtree').tree({ 
                 url: 'favjson.action',            
                 onClick:function(node){
                    if(node.state=='closed'){
               			$(this).tree('toggle', node.target);   			
               		}else if(node.state=='open'){
               			$(this).tree('toggle', node.target);
               		}else{       
               			addTab(node.text,node.attributes.url,node.iconCls); 
               	    }  
               }
                 
             });
             
             $('#iwindow').window('close');
             
             /*
             $('#pp').portal({
				border:false,
				fit:true
			});
			*/
             
             //加载首页门户portal
			//  add_portlet();
             //双击关闭tab
             tabClose();
             //注册邮件菜单
			 tabCloseEven();
			 //本地时钟
			 clockon();
			  
       			  
					
       			 
		});
		
		//添加至收藏夹
		function addcoll(){
		var node = $('#navtree').tree('getSelected');
		if(node==null){
        $.messager.alert('系统提示','请选择系统菜单!','error');
        return false;
        }
        var b = $('#navtree').tree('isLeaf', node.target);  
        if(b)//判断是否为最底层节点
            {
		        
				var url='addfav.action';
				document.forms[0].action=url;  
				document.forms[0].funid.value=node.id;
		        document.forms[0].funtext.value=node.text;
               //document.forms[0].method="post";  
               //document.forms[0].enctype="multipart/form-data" 
                document.forms[0].target="hidden_frame";
                document.forms[0].submit();
 		        return false; 	   
            }else{
            $.messager.alert('系统提示','非末级节点，不可添加!','error');
				 return false;
            }
		}
		
		//刷新导航树
		function reloadnav(){
			$('#navtree').tree({   
                 url: 'openjson.action',   
               	 onBeforeExpand:function(node){
               	 	//alert(node.attributes.type);
                     $('#navtree').tree('options').url = "openjson.action?pid=" + node.id+"&navtype="+node.attributes.type;// change the url                       
                 },             
               onClick:function(node){
               	//	alert(node.state);
               		if(node.state=='closed'){
               			$(this).tree('toggle', node.target);
               			
               		}else if(node.state=='open'){
               			$(this).tree('toggle', node.target);
               		}else{
               			addTab(node.text,node.attributes.url,node.iconCls);
               		}      
               }
                 
             });	
		}
		
		//打开收藏夹
		function arrcoll(){
		document.getElementById("myfav").innerHTML="<iframe id='edit' src='myfav.action' scrolling='no' frameborder='0' style='width:100%;height:100%;'></iframe>";
		$('#iwindow').window('open');		
		}
		
		//刷新收藏夹
		function reloadfav(){
        $('#favtree').tree({ 
                 url: 'favjson.action',            
                 onClick:function(node){
                    if(node.state=='closed'){
               			$(this).tree('toggle', node.target);   			
               		}else if(node.state=='open'){
               			$(this).tree('toggle', node.target);
               		}else{       
               			addTab(node.text,node.attributes.url,node.iconCls); 
               	    }  
               }
                 
             });
		}
		
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
			tabClose();
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
							alert('后边没有啦~~');
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
							alert('到头了，前边没有啦~~');
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
							addTab('系统消息','sysmsg_list.action','');
					
						})
		            $('#loginOut').click(function() {
		                $.messager.confirm('系统提示', '您确定要退出本次登录吗?', function(r) {
		 
		                    if (r) {
		                        location.href = 'logout.action';
		                    }
		                });
		            })
       			 });
       			 
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
				//通用顶级窗口
				function showWindow(title,content,href,width,height){
					if(href!=''){
						var content = '<iframe  scrolling="auto"  frameborder="0"  src="'+href+'" style="width:'+(width-10)+';height:'+(height-10)+';"></iframe>';
					}
					$('#syswindow').window({
					    title: title,
					   // href:href,
					    content:content,
						top:20,
					    width: width,
						height: height, 
					    modal: true,
					    shadow: true,
					    closed: true,
					    resizable:true
					    });
					$('#syswindow').window("open");
				}