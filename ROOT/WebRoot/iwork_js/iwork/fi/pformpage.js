   		var subReportSaveFunction = new Array();//存放子表的保存方法名
		 var mainFormValidator;
		 var mainFormAlertFlag=true;
		 var lockPageFlag=true;
		 var saveSubReportFlag=true;
		 $().ready(function() {
			 mainFormValidator =  $("#iformMain").validate({
				   errorPlacement: function (error, element) {
				   			//$(element).attr("style","border:1p solid red");
		               error.appendTo(element.parent());    //将错误信息添加当前元素的父结点后面
				   }
			 }); 
			 
				 mainFormValidator.resetForm();
				logInitPageElementValue();
				//滚动条事件  
		         $("#fpcontent").scroll(function () {
					if ($(this).scrollTop() > 150) {
						$('#back-top').fadeIn();
					} else {
						$('#back-top').fadeOut();
					}
				});
				$('#back-top a').click(function () {
					$('#fpcontent').animate({
						scrollTop: 0
					}, 100);
					return false;
				});
				//流程节点初始化脚本
				formInitEventScript();
				//获取消息文本
				showSysTips();
				
				//动态隐藏表单域title
				$("[actionSet='DynamicHide']").each(function(obj) {
					var fieldName=$(this).attr("id"); 
					$("#title_"+fieldName).html("");
				})
			});
		 function lockPage(){
				//先判断保存主表时是否需要覆盖层（当子表新增记录引发主表保存时不需要覆盖层）
				if(lockPageFlag){
					document.getElementById('blockPage').style.display='block';
				}else{
					document.getElementById('blockPage').style.display='none';
				} 
				lockPageFlag=true;
				return true;
			}
			function unLockPage(){
				document.getElementById('blockPage').style.display='none';
				return true;
			}
			function successResponse(responseText, statusText, xhr, $form)  { 
				//判断是否保存成功
				unLockPage();
				var isSaveSuccess = false;
				var alertMsg='';
				try{
			    	showSysTips();
				 }catch(e){}
				var arr=responseText.split(',');
				if(arr!=null&&arr.length==5){
					if(statusText=='success'&&arr[0]=='success'){
						//更新instanceId和dataid
						document.getElementById('instanceId').value=arr[1];
						document.getElementById('dataid').value=arr[2];
						document.getElementById('taskId').value=arr[3];
						document.getElementById('excutionId').value=arr[4];
						//重新记录主表页面元素初始值
						logInitPageElementValue();
						//先判断保存主表时是否需要执行保存子表
						if(saveSubReportFlag){
							var subFormSaveMsg='';
							for(var i=0;i<subReportSaveFunction.length;i++){
								var saveFun = subReportSaveFunction[i];
								if(typeof(saveFun)=='function'){
									var msg = saveFun();
									if(msg!=null&&msg.length>0){
										subFormSaveMsg+=msg;
									}
								}
							}
							if(subFormSaveMsg.length<1){
								alertMsg='保存成功';
								isSaveSuccess = true;
							}else{
								alertMsg=subFormSaveMsg;
							}
						}else{
							alertMsg='保存成功';
							isSaveSuccess = true;
						}
					}else{
						alertMsg='主表保存失败(错误编号:'+arr[0]+')';
						isSaveSuccess = false;
					} 
				}else{
					alertMsg='主表保存失败(错误编号:ERROR-00001)';
					isSaveSuccess = false;
				} 
				if(alertMsg!=null&&alertMsg.length>0){
					//判断是否提示保存异常的信息
					if(mainFormAlertFlag){
						//lhgdialog.tips(alertMsg,2,'tips.gif'); 
						if(alertMsg=='保存成功'){
							art.dialog.tips(alertMsg,2,'success.gif');
						}else{
							art.dialog.tips(alertMsg,2,'alert.gif'); 
						}
						
						if(alertMsg=='保存成功'&&$("#taskType").val()==0){ //判断办理菜单
							var actDefId = $("#actDefId").val();
							var prcDefId = $("#prcDefId").val();
							var instanceId = $("#instanceId").val();
							var excutionId = $("#excutionId").val();
							var taskId = $("#taskId").val();
							var formId = $("#formId").val();
							window.location.href = 'loadProcessFormPage.action?actDefId='+actDefId+'&prcDefId='+prcDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId+'&formId='+formId;
						}else if(!isSaveSuccess){
							var dialog = art.dialog({
							    title: '提示',
							    content: msg,
							    width: '20em',
							    button: [{
							    	name: '确定',
							        callback: function () {
							    	dialog.close();
							        },
							        focus: true
							    },{
							    	 name: '查看详情', 
							        callback: function () {
							    		showSysTips(); 
							        },
							        focus: true
							    }]
							});
						}
					}
					saveSubReportFlag=true;
					mainFormAlertFlag=true;
					return false;
				}else{
					saveSubReportFlag=true;
					mainFormAlertFlag=true;
					return true;
				}
			} 
			function failResponse(){
				document.getElementById('blockPage').style.display='none';
				art.dialog.tips("主表单保存失败(错误编号:ERROR-00001)",2,'error.gif');
				
			} 
			//页面装载时记录页面元素的初始值，便于记录页面元素是否更改
			function logInitPageElementValue(){ 
				$("input[id!=''][name!=''],textarea[id!=''][name!=''],select[id!=''][name!='']").each(function() {
					var v = $(this).val();
					if($(this).attr('type')!='radio'||($(this).attr('type')=='radio'&&$(this).attr('checked'))){
						$(this).attr('_value',v);
					}else if($(this).attr('type')=='radio'){
						/**如果是radio，但没有被选中，则赋予空值，以确保初始化的元素都被记录初始值。
						*在做元素是否被更改比较时，确保元素是初始的，没有增加（弹出子表时，会增加页面元素）。
						*/ 
						$(this).attr('_value','');
					}
				});
			} 
		//执行顺序流转动作
		function executeHandle(){
			//
			var valid = true; //执行校验操作
			if($("#formIsModify").val()==1){ 
				valid = mainFormValidator.form(); //执行校验操作
			} 
			if(!valid){ 
				return false;
			}else{
				if($("#formIsModify").val()==1){ 
					singleSave(); 
				} 
				//=====================================================
				//===============执行办理动作的表单自定义控制脚本================
				//=====================================================
				try{ 
					var flag = formTransEventScript();
					if(flag==false){
						return flag;
					}
				}catch(e){}
				//==================END==================================
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var formId = $("#formId").val();
				var dataid = $("#dataid").val();
				var pageUrl = "url:processRuntimeHandle.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
				this.showSendWindow(pageUrl,"任务办理",800,400);
			}
		}
		//执行审核跳转动作
		function executeManualJump(mjId,activityId,activityName){ 
			$("targetActivityId").val(activityId);
			var valid = true; //执行校验操作
			if($("#formIsModify").val()==1){ 
				valid = mainFormValidator.form(); //执行校验操作
			} 
			if(!valid){
				return false;
			}else{
				if($("#formIsModify").val()==1){ 
					singleSave();
				}
				//=====================================================
				//===============执行办理动作的表单自定义控制脚本================
				//=====================================================
				try{
					var flag = formTransEventScript();
					if(flag==false){
						return flag;
					}
				}catch(e){}
				//==================END==================================
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var formId = $("#formId").val();  
				var dataid = $("#dataid").val();
				var pageUrl = "url:processRuntimeManualJump.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid+"&mjId="+mjId+"&targetStepId="+activityId;
				this.showSendWindow(pageUrl,"任务办理",800,400);
			}
		}
		
		//执行审核跳转动作
		function selectStep(activityId){
			$("targetActivityId").val(activityId);
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}else{
				//=====================================================
				//===============执行办理动作的表单自定义控制脚本================
				//=====================================================
				try{
					var flag = formTransEventScript();
					if(flag==false){
						return flag;
					} 
				}catch(e){}
				//==================END==================================
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var formId = $("#formId").val();  
				var dataid = $("#dataid").val();
				var pageUrl = "url:processRuntimeManualJump.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid+"&targetStepId="+activityId;
				this.showSendWindow(pageUrl,"任务办理"); 
			}
		} 
		//驳回操作
		function executeBack(type){
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val(); 
				var prcDefId = $("#prcDefId").val();
				var formId = $("#formId").val();
				var dataid = $("#dataid").val();
				var pageUrl = "url:processRuntimeBack.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid+"&backType="+type;
				this.showSendWindow(pageUrl,"任务驳回",800,400);
		}
		//驳回操作
		function executeBackOther(){ 
			var actDefId = $("#actDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var excutionId = $("#excutionId").val();
			var taskId = $("#taskId").val();
			var instanceId = $("#instanceId").val();  
			var prcDefId = $("#prcDefId").val();
			var formId = $("#formId").val();
			var dataid = $("#dataid").val();
			var pageUrl = "url:processRuntimeBackOther.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
			this.showSendWindow(pageUrl,"任务驳回",800,400);
		}
		
		//转发操作
		function execForward(){
			var valid = true; //执行校验操作
			if($("#formIsModify").val()==1){ 
				valid = mainFormValidator.form(); //执行校验操作
			} 
			if(!valid){
				return false;
			}else{
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var formId = $("#formId").val();
				var dataid = $("#dataid").val();
				var pageUrl = "processRuntimeForward.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
				art.dialog.open(pageUrl,{
						id:'sendDialog',
						cover:true,
						title:"任务转发",
						width:500,
						height:500,
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true, 
						cache:true,
						lock: true,
						iconTitle:false,
						extendDrag:true,
						autoSize:true,
						resize:true
					});
			}
		}
		
		//加签操作
		function addSign(){
			var valid = true; //执行校验操作
			if($("#formIsModify").val()==1){ 
				valid = mainFormValidator.form(); //执行校验操作
			} 
			if(!valid){
				return false;
			}else{
				if($("#formIsModify").val()==1){ 
					singleSave();
				}
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val(); 
				var formId = $("#formId").val();
				var dataid = $("#dataid").val();
				var pageUrl = "url:processRuntimeAddSign.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
				this.showSendWindow(pageUrl,"加签操作",800,400);
			}
		}
		
		//加签完毕操作
		function backAddSign(){
			var valid = true; //执行校验操作
			if($("#formIsModify").val()==1){ 
				valid = mainFormValidator.form(); //执行校验操作
			} 
			if(!valid){
				return false;
			}else{
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var formId = $("#formId").val();
				var dataid = $("#dataid").val();
				var pageUrl = "url:processRuntimeBackAddSign.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
				this.showSendWindow(pageUrl,"加签操作",800,400);
			}
		}
		//打开副表单
		function openSubForm(stepformId){
			var valid = true; //执行校验操作
			if($("#formIsModify").val()==1){ 
				valid = mainFormValidator.form(); //执行校验操作
			} 
			if(!valid){
				return false;
			}else{
				var actDefId = $("#actDefId").val();
				var prcDefId = $("#prcDefId").val();
				var excutionId = $("#excutionId").val();
				var instanceId = $("#instanceId").val();
				var taskId = $("#taskId").val();
				var url = "loadProcessFormPage.action?actDefId="+actDefId+"&prcDefId="+prcDefId+"&instanceId="+instanceId+"&excutionId="+excutionId+"&stepformId="+stepformId+"&taskId="+taskId;
				this.location = url;
			}
		}
		//打印操作
		function print(){
			var valid = true; //执行校验操作
			if($("#formIsModify").val()==1){ 
				valid = mainFormValidator.form(); //执行校验操作
			} 
			if(!valid){
				return false;
			}else{
				var actDefId = $("#actDefId").val();
				var prcDefId = $("#prcDefId").val();
				var excutionId = $("#excutionId").val();
				var instanceId = $("#instanceId").val();
				var taskId = $("#taskId").val();
				//alert(actDefId+"?"+prcDefId+"?"+excutionId+"?"+instanceId+"?"+taskId);
				var pageurl = "processRuntimePrint.action?actDefId="+actDefId+"&prcDefId="+prcDefId+"&instanceId="+instanceId+"&excutionId="+excutionId+"&taskId="+taskId;
				this.location = pageurl;
			}
		}
		//保存表单
		function saveForm(){
			var valid = true; //执行校验操作
			if($("#formIsModify").val()==1){ 
				valid = mainFormValidator.form(); //执行校验操作
			} 
			if(!valid){
				return false;
			}else{
				 //集成weboffice
			 	try{ 
			 		SaveDocument();  
			 	}catch(e){} 
			 	 
				try{	
					var flag = formSaveEventScript();
						if(flag==false){
							return flag;
						}
				}catch(e){} 
				 var submitOption = {
					    	beforeSubmit:lockPage,
					    	error :failResponse,
					    	complete:unLockPage,
					    	success:successResponse,
					    	dataType:'text'
					    };
					$('#iformMain').attr('action','processRuntimeFormSave.action');
					$('#iformMain').ajaxSubmit(submitOption); 
			//	$('form').submit();
			}
		}
		
		function singleSave(){
			$.post('processRuntimeFormSave.action',$("#iformMain").serialize(),function(data)
		            {
				    	if(data=='success'){
				    		
				    	}else{
				    		
				    	}
				  }); 
		}
		
		//弹出提示信息，timeLimit大于0时，限时提醒，提醒信息只保留timeLimit
		function showMsg(msg,timeLimit){
			if(timeLimit>0){
				art.dialog.tips(msg,timeLimit);
			}else{
				art.dialog.tips(msg);
			}
		} 
		//抢签任务，执行任务领用
		function claimTask(){
			  $.post('processManagement_claimTask.action',$("#iformMain").serialize(),function(data)
		            {
				    	if(data=='success'){
				    		art.dialog.tips("任务领取成功",5);
				    		try{
								window.parent.opener.reloadPage();
							}catch(err){}
				    		var actDefId = $("#actDefId").val();
							var instanceId = $("#instanceId").val();
							var taskId = $("#taskId").val();
							window.location.href = 'loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+instanceId+'&taskId='+taskId;
				    	}else{
				    		art.dialog.tips("任务领取失败(错误编号:ERROR-00001)",5)
				    	}
				  }); 
		}
		//打开流转处理窗口
		function showSendWindow(pageUrl,title,width,height){
			if(title=='undefined'){
				title = "执行提交";
			}
			if(width=='undefined'){
				width='800';
			}
			if(height=='undefined'){
				height='400';
			}
			art.dialog.open(pageUrl,{
						id:'sendDialog',
						cover:true,
						title:title,
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						cache:false,
						width:width,
						height:height,
						lock: true,
						iconTitle:false,
						extendDrag:true,
						autoSize:false,
						resize:false
					});
					
				//dg.ShowDialog();
		}
		//打开流程任务监控窗口
		function openMonitorPage(){
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var excutionId = $("#excutionId").val(); 
				var pageUrl = "url:processInstanceMornitor.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&taskId="+taskId+"&instanceId="+instanceId+"&excutionId="+excutionId;
				art.dialog.open(pageUrl,{
						id:'mornitorDialog',
						cover:true,
						title:"流程跟踪",
						width:800,
						height:600,
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true, 
						cache:true,
						lock: true,
						iconTitle:false,
						extendDrag:true,
						autoSize:true,
						resize:true
					});
				//dg.ShowDialog();
		}
		function addOLWin(){
			
			var actDefId = $("#actDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var taskId = $("#taskId").val();
			var instanceId = $("#instanceId").val();
			var excutionId = $("#excutionId").val(); 
	        var pageurl='url:ProcessStepOL_add.action?actDefId='+actDefId+'&taskId='+taskId+'&actStepDefId='+actStepDefId+'&excutionId='+excutionId+'&instanceId='+instanceId;
	        art.dialog.open(pageUrl,{
	            id:'dg_OffLine',  
		        title:'添加任务反馈记录', 
		       	iconTitle:false, 
		       	cover:true,
		       	lock:false, 
	            background: '#DCE2F1',
	            opacity:0.5,
	            width:550,  
	            height:400        
	        });
	      //  olwin.ShowDialog();
	        return false; 
		}
		//打开流程任务监控窗口
		function openTalkListPage(){
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var pageUrl = "url:process_talk_load.action?actDefId="+actDefId+"&proDefId="+prcDefId+"&instanceId="+instanceId;
				art.dialog.open(pageUrl,{
						id:'TalkListDialog',
						cover:true,
						title:"流程讨论区",
						width:800,
						height:600,
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true, 
						cache:true,
						lock: true,
						iconTitle:false,
						extendDrag:true,
						autoSize:true,
						resize:true
					});
		}
		//隐藏办理窗口
		function hiddenSenWindow(){
			$.dialog.list["sendDialog"].close();
		}
		//关闭当前页面窗口 
		function closePage(){ 
			 var browserName=navigator.appName; 
			 if (browserName=="Netscape") { 
				 window.open('','_parent',''); window.close(); 
			  } else if (browserName=="Microsoft Internet Explorer") {
				  window.opener = "whocares"; window.close(); 
			  }
			window.close();
		} 
		//刷新待办列表页面
		function reloadParentPage(){
		    window.opener.location.reload();
		}
		//当表单类型为URL时，设置iframe高度
		function iFrameHeight(obj) { 
			var ifm= document.getElementById(obj); 
			if(ifm != null) { 
				ifm.height = document.body.scrollHeight-155; 
			} 
		}
		//判断页面元素是否修改
		function is_form_changed(){
			var is_changed =false;
			$("input[id!=''][name!=''],textarea[id!=''][name!=''],select[id!=''][name!='']").each(function() {
				//只校验为radio元素和被选中的radio无素，因为radio元素有相同的id
				if($(this).attr('type')!='radio'||($(this).attr('type')=='radio'&&$(this).attr('checked'))){
					var id = $(this).attr('id');
					//过滤id为jqg_和id_g等弹出子表带来的新页面元素
					if(id.indexOf('jqg_')<0&&id.indexOf('id_g')<0){
						var _v = $(this).attr('_value');
						//如果页面元素的‘value’值不存在，说明页面初始化没有该元素，不做比较（可能是弹出子表带入的新页面元素）
						if(typeof(_v) !='undefined'&&_v != $(this).val()){
							is_changed =true;
						}
					}
				}
			});
			return is_changed;
		}
		
	
		//打开子表数据字典
		function openSubGridDictionary(id,subformkey,subformid){
			var pageUrl = "sys_dictionary_runtime_show.action?dictionaryId="+id+"&subformkey="+subformkey+"&subformid="+subformid;
			art.dialog.open(pageUrl,{
					id:'DictionaryDialog',
					cover:true,
					title:"数据选择",
					width:800,
					height:520,
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true, 
					lock: true,
					iconTitle:false,
					extendDrag:true, 
					autoSize:false,
					resize:false
				});
		}
		//导入excel
		function openImpExcel(instanceId,subformid,subformkey,engineType){
			var modelId = $("#modelId").val();
			var pageUrl = "subform_openImpPage.action?modelId="+modelId+"&instanceId="+instanceId+"&subformid="+subformid+"&subformkey="+subformkey+"&engineType="+engineType;
			art.dialog.open(pageUrl,{
				id:'ExcelImpDialog',
				cover:true,
				title:"数据导入",
				width:500,
				height:350,
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true, 
				lock: true,
				iconTitle:false,
				extendDrag:true, 
				autoSize:false,
				resize:false
			});
		}
		//附件图片预览
		function previewPic(url){
			var pageUrl = url;
			var modelId = $("#modelId").val();
			art.dialog.open(pageUrl,{
				id:'previewPic',
				cover:true,
				title:"图片预览",
				width:1000,
				height:600,
				loadingText:'正在加载中,请稍后...', 
				bgcolor:'#999',
				rang:true, 
				lock: true,
				iconTitle:false,
				extendDrag:true, 
				autoSize:false,
				resize:false
			});
		}

		function closeWin(){
			
			try{
				window.opener=null;
				window.open('','_self');
				window.close();
			}catch(e){
				
			}
			
		}
		window.onbeforeunload =function(){
			if(is_form_changed()){
				//关闭weboffice文档
				try{
					UnLoad();
				}catch(e){}
				return '页面有修改未保存，是否关闭页面？';
			}
		}