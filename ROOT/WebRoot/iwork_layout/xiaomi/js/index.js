$(function(){
			//�Զ������Ҳ�ر�
			var p = $('body').layout('panel','west').panel({
				onCollapse:function(){
					alert('collapse');
				}
			});
			setTimeout(function(){
				$('body').layout('collapse','east');
			},0);
			
			//���ص�����
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
             
              //�����ղؼ�
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
             
             //������ҳ�Ż�portal
			//  add_portlet();
             //˫���ر�tab
             tabClose();
             //ע���ʼ��˵�
			 tabCloseEven();
			 //����ʱ��
			 clockon();
			  
       			  
					
       			 
		});
		
		//������ղؼ�
		function addcoll(){
		var node = $('#navtree').tree('getSelected');
		if(node==null){
        $.messager.alert('ϵͳ��ʾ','��ѡ��ϵͳ�˵�!','error');
        return false;
        }
        var b = $('#navtree').tree('isLeaf', node.target);  
        if(b)//�ж��Ƿ�Ϊ��ײ�ڵ�
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
            $.messager.alert('ϵͳ��ʾ','��ĩ���ڵ㣬�������!','error');
				 return false;
            }
		}
		
		//ˢ�µ�����
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
		
		//���ղؼ�
		function arrcoll(){
		document.getElementById("myfav").innerHTML="<iframe id='edit' src='myfav.action' scrolling='no' frameborder='0' style='width:100%;height:100%;'></iframe>";
		$('#iwindow').window('open');		
		}
		
		//ˢ���ղؼ�
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
				/*˫���ر�TABѡ�*/
				$(".tabs-inner").dblclick(function(){
					var subtitle = $(this).children(".tabs-closable").text();
					$('#mainFrameTab').tabs('close',subtitle);
				})
				/*Ϊѡ����Ҽ�*/
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
				//���Ҽ��˵��¼�
			function tabCloseEven(){
					//�رյ�ǰ
					$('#mm-tabclose').click(function(){
						var currtab_title = $('#mm').data("currtab");
						$('#mainFrameTab').tabs('close',currtab_title);
					})
					//ȫ���ر�
					$('#mm-tabcloseall').click(function(){
						$('.tabs-inner span').each(function(i,n){
							var t = $(n).text();
							$('#mainFrameTab').tabs('close',t);
						});	
					});
					//�رճ���ǰ֮���TAB
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
					//�رյ�ǰ�Ҳ��TAB
					$('#mm-tabcloseright').click(function(){
						var nextall = $('.tabs-selected').nextAll();
						if(nextall.length==0){
							//msgShow('ϵͳ��ʾ','���û����~~','error');
							alert('���û����~~');
							return false;
						}
						nextall.each(function(i,n){
							var t=$('a:eq(0) span',$(n)).text();
							$('#mainFrameTab').tabs('close',t);
						});
						return false;
					});
					//�رյ�ǰ����TAB
					$('#mm-tabcloseleft').click(function(){
						var prevall = $('.tabs-selected').prevAll();
						if(prevall.length==0){
							alert('��ͷ�ˣ�ǰ��û����~~');
							return false;
						}
						prevall.each(function(i,n){
							var t=$('a:eq(0) span',$(n)).text();
							$('#mainFrameTab').tabs('close',t);
						});
						return false;
					});
				
					//�˳�
					$("#mm-exit").click(function(){
						$('#mm').menu('hide');
					})
				}
				
				//������Ϣ���� title:���� msgString:��ʾ��Ϣ msgType:��Ϣ���� [error,info,question,warning]
				function msgShow(title, msgString, msgType) {
					$.messager.alert(title, msgString, msgType);
				}
				//����TOP����ݲ˵�
				 $(function() {
					$('#sysmsg').click(
						function(){
							addTab('ϵͳ��Ϣ','sysmsg_list.action','');
					
						})
		            $('#loginOut').click(function() {
		                $.messager.confirm('ϵͳ��ʾ', '��ȷ��Ҫ�˳����ε�¼��?', function(r) {
		 
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
				
				//����ʱ��
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
				    var arr_week = new Array("������", "����һ", "���ڶ�", "������", "������", "������", "������");
				    week = arr_week[day];
				    var time = "";
				    time = year + "��" + month + "��" + date + "��" + " " + hour + ":" + minu + ":" + sec + " " + week;
				
				    $("#bgclock").html(time);
				
				    var timer = setTimeout("clockon()", 200);
				}
				//ͨ�ö�������
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