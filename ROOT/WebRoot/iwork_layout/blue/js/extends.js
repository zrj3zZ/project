   function showUrl(url,title,icon,type){ 
            	if(type=="_blank"){
            		window.open(url,'_blank',''); 
            	}else{
            		addTab(title,url,icon);
            	}
            }
            function openWin(title,height,width,pageurl,location,dialogId){
				if(dialogId=='undefined')dialogId="mainDialogWin"; 
				art.dialog.open(pageurl,{ 
					   id:dialogId, 
						cover:true, 
						title:title,  
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						width:width,
						cache:false, 
						lock: true,
						esc: true,
						height:height,   
						iconTitle:false,  
						extendDrag:true,
						autoSize:true,
						close:function(){
							if(location!=null){
								location.reload();
							}
						}
						
					});
			}
           
			function unlogin(){
				 if(confirm('您确定要退出本次登录吗?')){
		                        $.ajax({
		        	       			url:'logout.action', //后台处理程序
		        	       			type:'post',         //数据发送方式
		        	       			dataType:'json'
		             			});
		                    location.href = 'login.action';
		                }
			} 
			//添加页签
		function addTab(subtitle,url,icon){  
			if(!$('#mainFrameTab').tabs('exists',subtitle)){
				$('#mainFrameTab').tabs('add',{
					title:subtitle,
					singleSelect:true,
					closable:true,
					content:createFrame(url),
					icon:icon
				}); 
			}else{
				$('#mainFrameTab').tabs('select',subtitle);
			}
			return;
		}
		function createFrame(url)
		{
			var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
			return s;
		} 
		function setPWD(){
			var url = "syspersion_pwd_update.action";
			addTab("修改口令",url,"icon-user");
		}