   		var subReportSaveFunction = new Array();//存放子表的保存方法名
		 var mainFormValidator;
		 var mainFormAlertFlag=true;
		 var lockPageFlag=true;
		 var saveSubReportFlag=true;
		 var opra;
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
					var fieldName=$(this).attr("name");
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
								try{
									var msg =  eval("saveSubReportData"+saveFun+"();");
									if(msg!=null&&msg.length>0){
										subFormSaveMsg+=msg;
									} 
								}catch(e){alert(e);}
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
//					if(mainFormAlertFlag){
					art.dialog.tips(alertMsg,2);
						if(alertMsg=='保存成功'&&$("#taskType").val()==0){ //判断办理菜单
							var actDefId = $("#actDefId").val();
							var prcDefId = $("#prcDefId").val();
							var instanceId = $("#instanceId").val();
							var excutionId = $("#excutionId").val();
							var taskId = $("#taskId").val();
							var formId = $("#formId").val();
							window.location.href = 'loadProcessFormPage.action?actDefId='+actDefId+'&prcDefId='+prcDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId+'&formId='+formId;
						}else if(!isSaveSuccess){
							art.dialog.tips(alertMsg,2); 
							showSysTips(); 
						}
//					}
					saveSubReportFlag=true;
					mainFormAlertFlag=true;
					return false;
				}else{
					saveSubReportFlag=true;
					mainFormAlertFlag=true;
					window.location.reload();
					return true;
				}
			} 
			function failResponse(){
				document.getElementById('blockPage').style.display='none';
				art.dialog.tips("主表单保存失败(错误编号:ERROR-00001)",2);
				
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
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}else{
				var flag = singleSave();
				if(!flag){
					showSysTips(); 
					return false;
				}
				//=====================================================
				//===============执行办理动作的表单自定义控制脚本================
				//=====================================================
				try{
					flag = formTransEventScript();
					if(flag==false){
						showSysTips(); 
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
				var pageUrl = "processRuntimeHandle.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
				this.showSendWindow(pageUrl,"任务办理",820,520);
			}
		}
		//抄送操作
		function executeCC(){ 
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var formId = $("#formId").val();
				var dataid = $("#dataid").val();
				var pageUrl = "processRuntimeLoadCCDlg.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
				this.showSendWindow(pageUrl,"抄送",800,400);
		}
		//会签操作
		function doSignsNext(){ 
			var actDefId = $("#actDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var excutionId = $("#excutionId").val();
			var taskId = $("#taskId").val();
			var instanceId = $("#instanceId").val();  
			var prcDefId = $("#prcDefId").val();
			var formId = $("#formId").val();
			var dataid = $("#dataid").val();　
			var pageUrl = "processRuntimedoSignNextDlg.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
			this.showSendWindow(pageUrl,"会签",800,400);
		}
		
		//执行审核跳转动作
		function executeManualJump(mjId,activityId,activityName){ 
			$("targetActivityId").val(activityId);
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}else{
				var flag = singleSave();
				if(!flag){
					showSysTips(); 
					return false;
				}
				//=====================================================
				//===============执行办理动作的表单自定义控制脚本================
				//=====================================================
				try{
					flag = formTransEventScript();
					if(flag==false){
						showSysTips(); 
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
				var pageUrl = encodeURI("processRuntimeManualJump.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid+"&mjId="+mjId+"&targetStepId="+activityId);
				this.showSendWindow(pageUrl,"任务办理",800,500);
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
				var flag = singleSave();
				if(!flag){
					showSysTips(); 
					return false;
				}
				try{
					flag = formTransEventScript();
					if(flag==false){
						showSysTips(); 
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
				var pageUrl = "processRuntimeManualJump.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid+"&targetStepId="+activityId;
				this.showSendWindow(pageUrl,"任务办理"); 
			}
		} 
		
		//添加审核意见
		function addAuditOpinion(){
			var valid = mainFormValidator.form(); //执行校验操作
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
			    	var pageUrl=encodeURI('process_opin_showFrame.action?actDefId='+actDefId+'&proDefId='+prcDefId+'&actStepId='+actStepDefId+'&actStepName=审核意见&instanceid='+instanceId+'&taskid='+taskId+'&excutionid='+excutionId);
			    	art.dialog.open(pageUrl,{
			    		 id: "dg_addAuditOpinion" ,
					      title: "发表意见", 
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:550, 
			            height:400   
					 });	 
			}
		}
		
		//发起讨论
		function execTalk(){
			var actDefId = $("#actDefId").val();
			var prcDefId = $("#prcDefId").val();
	        var stepId = $("#actStepDefId").val();
	        var stepName='节点名字'; 
	        var instanceId =  $("#instanceId").val(); 
	        var title_url= document.getElementsByTagName('title')[0].text;
	        var pageurl='process_talk_startTalk.action?actDefId='+actDefId+'&proDefId='+prcDefId+'&stepId='+stepId+'&stepName='+stepName+'&instanceId='+instanceId+'&processTitle='+title_url;
			pageurl = encodeURI(pageurl);
			art.dialog.open(pageUrl,{
				id:'dg_newTalk',  
			    title:'发起流程讨论',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:550, 
	            height:400   
			 });
		}
		
		//驳回操作
		function executeBack(type){
			try{
				var flag = processBackEventScript();
				if(flag==false){
					return flag;
				}
			}catch(e){}
			
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val(); 
				var prcDefId = $("#prcDefId").val();
				var formId = $("#formId").val();
				var dataid = $("#dataid").val();
				var pageUrl = "processRuntimeBack.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid+"&backType="+type;
				this.showSendWindow(pageUrl,"任务驳回",800,500);
		}
		//驳回操作
		function executeBackOther(){ 
			try{
				var flag = processBackOtherEventScript();
				if(flag==false){
					return flag;
				}
			}catch(e){}
			var actDefId = $("#actDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var excutionId = $("#excutionId").val();
			var taskId = $("#taskId").val();
			var instanceId = $("#instanceId").val();  
			var prcDefId = $("#prcDefId").val();
			var formId = $("#formId").val();
			var dataid = $("#dataid").val();
			var pageUrl = "processRuntimeBackOther.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
			this.showSendWindow(pageUrl,"任务驳回",820,540);
		}
		//驳回其他后返回 
		function executeBackfh(){
			try{
				var flag = processBackOtherEventScript();
				if(flag==false){
					return flag;
				}
			}catch(e){}
			var actDefId = $("#actDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var excutionId = $("#excutionId").val();
			var taskId = $("#taskId").val();
			var instanceId = $("#instanceId").val();  
			var prcDefId = $("#prcDefId").val();
			var formId = $("#formId").val();
			var dataid = $("#dataid").val();
			var pageUrl = "processRuntimeBackOtherfh.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
			this.showSendWindow(pageUrl,"任务驳回",820,540);
		}
		//会签操作
		function doSigns(){ 
			var actDefId = $("#actDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var excutionId = $("#excutionId").val();
			var taskId = $("#taskId").val();
			var instanceId = $("#instanceId").val();  
			var prcDefId = $("#prcDefId").val();
			var formId = $("#formId").val();
			var dataid = $("#dataid").val();　
			var pageUrl = "processRuntimedoSignDlg.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
			this.showSendWindow(pageUrl,"会签",800,400);
		}
		
		//转发操作
		function execForward(){
			var valid = mainFormValidator.form(); //执行校验操作
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
					id:'dg_newTalk',  
					title:"任务转发",
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
					height:500,
					close:function(){
						window.location.reload(true);
					}
				 });
			}
		}
		//加签操作
		function sendSigns(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}else{ 
				var flag = singleSave();
				if(flag){
					var actDefId = $("#actDefId").val();
					var actStepDefId = $("#actStepDefId").val();
					var excutionId = $("#excutionId").val();
					var taskId = $("#taskId").val();
					var instanceId = $("#instanceId").val();
					var prcDefId = $("#prcDefId").val(); 
					var formId = $("#formId").val();
					var dataid = $("#dataid").val();
					var pageUrl = "processRuntimeSigns_dlg.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
					opra = art.dialog.open(pageUrl,{
						id:'sendDialog',
						title:'发送会签',
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:450,
						height:350,
						close:function(){
						window.location.reload(true);
					}
					 });
				}else{
					showSysTips();  
					return false;
				}
				
			}
		}
		
		//完成会签操作
		function finishSigns(){
			if(confirm("确认终止会签吗？终止会签后未会签用户的会签任务将自动撤销!")){
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val(); 
				var formId = $("#formId").val();
				var dataid = $("#dataid").val();
				$.post('processRuntimeSigns_finish.action',{actDefId:actDefId,prcDefId:prcDefId,actStepDefId:actStepDefId,taskId:taskId,instanceId:instanceId,excutionId:excutionId},function(data){
					if(data=='success'){
						window.location.reload(true);
					}else{
						alert("发送会签异常，请稍后重试或联系管理员！");
					}
				});
			}
		}
		//加签操作
		function addSign(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}else{
				var flag = singleSave();
				if(flag){
					var actDefId = $("#actDefId").val();
					var actStepDefId = $("#actStepDefId").val();
					var excutionId = $("#excutionId").val();
					var taskId = $("#taskId").val();
					var instanceId = $("#instanceId").val();
					var prcDefId = $("#prcDefId").val(); 
					var formId = $("#formId").val();
					var dataid = $("#dataid").val();
					var pageUrl = "processRuntimeAddSign.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
					this.showSendWindow(pageUrl,"加签操作",800,400);
				}else{
					showSysTips();  
					return false;
				}
				
			}
		}
		
		//加签完毕操作
		function backAddSign(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}else{
				/*var flag = singleSave();
				if(flag){*/
					var actDefId = $("#actDefId").val();
					var actStepDefId = $("#actStepDefId").val();
					var excutionId = $("#excutionId").val();
					var taskId = $("#taskId").val();
					var instanceId = $("#instanceId").val();
					var prcDefId = $("#prcDefId").val();
					var formId = $("#formId").val();
					var dataid = $("#dataid").val();
					var pageUrl = "processRuntimeBackAddSign.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
					this.showSendWindow(pageUrl,"加签操作",800,400);
				/*}else{
					showSysTips(); 
					return false;
				}*/
			}
		}
		//打开副表单
		function openSubForm(stepformId){
			var taskId = $("#taskId").val();
			if(taskId=='0'){
				return false;
			}
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}else{
				var flag = singleSave();
				if(!flag){
					showSysTips(); 
					return false;
				}
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var prcDefId = $("#prcDefId").val();
				var excutionId = $("#excutionId").val();
				var instanceId = $("#instanceId").val();
				//var taskId = $("#taskId").val();
				
				var url = "loadProcessFormPage.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&instanceId="+instanceId+"&excutionId="+excutionId+"&stepformId="+stepformId+"&taskId="+taskId;
				this.location = url;
			}
		}
		//打印操作
		function print(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}else{
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var prcDefId = $("#prcDefId").val();
				var excutionId = $("#excutionId").val();
				var instanceId = $("#instanceId").val();
				var taskId = $("#taskId").val();
				var formId = $("#formId").val();
				//alert(actDefId+"?"+prcDefId+"?"+excutionId+"?"+instanceId+"?"+taskId);
				var pageurl = "processRuntimePrint.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&instanceId="+instanceId+"&excutionId="+excutionId+"&taskId="+taskId+"&formId="+formId;
				this.location = pageurl;
			}
		}
		//保存表单
		function saveForm(){
			
		
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}else{
				 //集成weboffice
			 	try{ 
			 		$("[form-type='GovWord']").each(function(){
			 		    $(this)[0].contentWindow.SaveDocument(); 
			 		  });
			 	}catch(e){} 
			 	//保存富文本
			 	try{ 
			 		saveHtmlEditor();  
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
			}
		}
		
		function saveHtmlEditor(){
			//保存所有的富文本
			$("[form-type='htmlEditor']").each(function(){
				var name = $(this).attr("name");
				eval("editor"+name).sync();
			});
		}
		
		function singleSave(){
			var isModify = 0;
		 	try{ 
		 		if(typeof($("#formIsModify").val())!='undefined'){
		 			isModify = $("#formIsModify").val();
		 		}
		 	}catch(e){} 
		 	if(isModify==0){
				return true;
			}
		 	//保存富文本
		 	try{
		 		saveHtmlEditor();  
		 	}catch(e){} 
		 	
			var url = 'processRuntimeFormSave.action';
			var para = $("#iformMain").serialize();
			var flag = false;
			$.ajax({
				   url:url,
				   data:para,
				   type:'POST',
				   async:false,//(默认: true) 默认设置下，所有请求均为异步请求。如果需要发送同步请求，请将此选项设置为 false。注意，同步请求将锁住浏览器，用户其它操作必须等待请求完成才可以执行。
				   success: function(data){
						if(data.indexOf('success')>-1){
							flag = true;
				    	}else{
				    		flag = false;
				    	}
				  }
			});
			var subFormSaveMsg='';
			for(var i=0;i<subReportSaveFunction.length;i++){
				var saveFun = subReportSaveFunction[i];
				if(typeof(saveFun)=='function'){
					try{
						var msg = saveFun();
						if(msg!=null&&msg.length>0){
							subFormSaveMsg+=msg;
						}
					}catch(e){}
				}
			}
			return flag;
//			$.post(,,function(data)
//		            {
//						alert(data);
//				    	if(data.indexOf('success')>-1){
//				    		return true;
//				    	}else{
//				    		return false;
//				    	}
//				  }); 
		}
		
		//弹出提示信息，timeLimit大于0时，限时提醒，提醒信息只保留timeLimit
		function showMsg(msg,timeLimit){
			if(timeLimit>0){
				art.dialog.tips(msg,timeLimit);
			}else{
				art.dialog.alert(msg);
			}
		} 
		//抢签任务，执行任务领用
		function claimTask(){
			  $.post('processManagement_claimTask.action',$("#iformMain").serialize(),function(data)
		            {
				    	if(data=='success'){
				    		art.dialog.tips("任务领取成功", 2);
				    		try{
								window.parent.opener.reloadPage();
							}catch(err){}
				    		var actDefId = $("#actDefId").val();
							var instanceId = $("#instanceId").val();
							var taskId = $("#taskId").val();
							window.location.href = 'loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+instanceId+'&taskId='+taskId;
				    	}else{
				    		art.dialog.tips("任务领取失败(错误编号:ERROR-00001)", 2);
				    	}
				  }); 
		}

		/**
		 * 子表录入项回调页面事件方法
		 * @param subformKey
		 * @param colName
		 * @param val
		 * @param id
		 * @param type
		 * @return
		 */
		function subformInputEvent(subformKey,colName,val,id,type){
			var rowId = id.substring(0,id.indexOf("_"));
			try{
				//
				if(type=='onchange'){
					subformHandleEventOnChange(subformKey,colName,val,rowId);
				}else if(type=='onblur'){
					subformHandleEventOnBlur(subformKey,colName,val,rowId);
				}else if(type=='keypress'){
					subformHandleEventKeyPress(subformKey,colName,val,rowId);
				}
			}catch(e){
			}
		}

		function getRealValue(pValue) {
		    if ($.browser.msie) {
		        var oReg2 = /(\s+\w+=)('|")?(.*?)('|")?(?=\s+\w+=|\s*>|\s*\/>)/gi;
		        pValue = pValue.replace(oReg2, "$1\"$3\"");
		        var oReg = /^<(input|INPUT).*id=\"(.*?)\"/m;
		        var val = pValue.match(oReg);
		        return !val ? pValue : $("#" + val[2]).val();
		    }
		    else {
		        var regExp = /^<input.*id=\"(\w*)\".*>$/;
		        var val = pValue.match(regExp);
		        return !val ? pValue : $("#" + val[1]).val();
		    }
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
			
			opra = art.dialog.open(pageUrl,{
				title:title,
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width: width, 
			    height: height,
			    close:function(){
			    	window.location.reload();
				}
			 });
		}
		//打开流程任务监控窗口
		function openMonitorPage(){
			var actDefId = $("#actDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var taskId = $("#taskId").val();
			var instanceId = $("#instanceId").val();
			var prcDefId = $("#prcDefId").val();
			var excutionId = $("#excutionId").val(); 
			var pageUrl = "processInstanceMornitor.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&taskId="+taskId+"&instanceId="+instanceId+"&excutionId="+excutionId;
			art.dialog.open(pageUrl,{
				title:"流程跟踪",
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:800,
				height:600
			 });
		}
		function addOLWin(){
			var actDefId = $("#actDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var taskId = $("#taskId").val();
			var instanceId = $("#instanceId").val();
			var excutionId = $("#excutionId").val(); 
	        var pageUrl='ProcessStepOL_add.action?actDefId='+actDefId+'&taskId='+taskId+'&actStepDefId='+actStepDefId+'&excutionId='+excutionId+'&instanceId='+instanceId;
	        art.dialog.open(pageUrl,{
	        	 title:'添加任务反馈记录', 
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:550,  
	            height:400  
			 });
	        return; 
		}
		//打开流程任务监控窗口
		function openTalkListPage(){
			var actDefId = $("#actDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var taskId = $("#taskId").val();
			var instanceId = $("#instanceId").val();
			var prcDefId = $("#prcDefId").val();
			var pageUrl = "process_talk_load.action?actDefId="+actDefId+"&proDefId="+prcDefId+"&instanceId="+instanceId;
			
			 art.dialog.open(pageUrl,{
				title:"流程讨论区",
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:800,
				height:600
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
			var valid = mainFormValidator.form();
			if(!valid){
				return;
			}
			var instanceId = document.getElementById('instanceId').value;
			if(instanceId==null||instanceId==''||instanceId==0){
					art.dialog.tips('请先对主表单进行保存!',2);
					return;
			}
			var pageUrl = "sys_dictionary_runtime_show.action?dictionaryId="+id+"&subformkey="+subformkey+"&subformid="+subformid;
			opra = art.dialog.open(pageUrl,{
				title:"数据选择",
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:'95%',
			    height:'90%' 
			 });
		}
		//导入excel
		function openImpExcel(instanceId,subformid,subformkey,engineType){
			var modelId = $("#modelId").val();
			var pageUrl = "subform_openImpPage.action?modelId="+modelId+"&instanceId="+instanceId+"&subformid="+subformid+"&subformkey="+subformkey+"&engineType="+engineType;
			opra = art.dialog.open(pageUrl,{
				id:'ExcelImpDialog',
				title:"数据导入",
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:500,
				height:350,
				close:function(){
					$("#subform"+subformkey).trigger('reloadGrid');
				}
			 });
		}
		//附件图片预览
		function previewPic(url){
			var pageUrl = url;
			var modelId = $("#modelId").val();
			opra = art.dialog.open(pageUrl,{
				id:'previewPic',
				title:"图片预览",
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:500,
				height:350
			 });
		}

		function closeWin(){
			try{
				opra.close();
				window.opener=null;
				window.open('','_self');
				window.close();
			}catch(e){
				alert(e);
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