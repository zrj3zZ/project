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
				//流程节点初始化脚本
				formInitEventScript();
				//获取消息文本
				showSysTips();
			});
		  
		 function showMenu(){
			 var content = $("#MenuList").html();
			// alert(content);
			 art.dialog({
				 	title:"任务处理",
				 	background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				 	width:200,
				 	time: 5,
				 	lock:true,
				    follow: document.getElementById('transBtn'),
				    content:content
				}); 
		 }
			
		//加载审核意见列表
		function showOpinionList(){
			if($("#dataid").val()==0){
				alert('请保存主表单后编辑行项目子表');
				return;
			}
			var actDefId = $("#actDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var excutionId = $("#excutionId").val();
			var taskId = $("#taskId").val();
			var formId = $("#formId").val();
			var instanceId = $("#instanceId").val();
			var prcDefId = $("#prcDefId").val();
			var dataid = $("#dataid").val(); 
			var pageUrl = "weixin_showOpinionList.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
			
			this.showSendWindow(pageUrl); 
		}
		//加签操作
		function sendSigns(){
					var actDefId = $("#actDefId").val();
					var actStepDefId = $("#actStepDefId").val();
					var excutionId = $("#excutionId").val();
					var taskId = $("#taskId").val();
					var instanceId = $("#instanceId").val();
					var prcDefId = $("#prcDefId").val(); 
					var formId = $("#formId").val();
					var dataid = $("#dataid").val();
					var pageUrl = "processRuntimeSigns_dlg.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
					this.showSendWindow(pageUrl,"任务会签",800,400); 
				 
		}
		//会签操作
		function execSigns(){
					var actDefId = $("#actDefId").val();
					var actStepDefId = $("#actStepDefId").val();
					var excutionId = $("#excutionId").val();
					var taskId = $("#taskId").val();
					var instanceId = $("#instanceId").val();
					var prcDefId = $("#prcDefId").val(); 
					var formId = $("#formId").val();
					var dataid = $("#dataid").val();
					var pageUrl = "processRuntimeSigns_dlg.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
					art.dialog.open(pageUrl,{
						id:'sendDialog',
						title:'发送会签',
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:500,
						height:700,
						close:function(){
						window.location.reload(true);
					}
					 });
				 
		}
		
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
		//执行顺序流转动作
		function executeHandle(){
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
				var pageUrl = "weixin_processRuntimeHandle.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
				this.showSendWindow(pageUrl,"任务办理",800,400); 
		}
		
		//执行审核跳转动作
		function executeManualJump(mjId,activityId,activityName){ 
			$("#targetActivityId").val(activityId);
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
				var pageUrl = "weixin_processRuntimeManualJump.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid+"&mjId="+mjId+"&targetStepId="+activityId;
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
				var pageUrl = "weixin_processRuntimeManualJump.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid+"&targetStepId="+activityId;
				this.showSendWindow(pageUrl,"任务办理"); 
			}
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
				var pageUrl = "weixin_processRuntimeBack.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid+"&backType="+type;
				this.showSendWindow(pageUrl,"任务驳回",800,400);
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
			var pageUrl = "weixin_processRuntimeBackOther.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
			this.showSendWindow(pageUrl,"任务驳回",800,400); 
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
				var pageUrl = "weixin_processRuntimeForward.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
				this.showSendWindow(pageUrl,"任务专发",800,400); 
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
					var pageUrl = "weixin_processRuntimeAddSign.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
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
					var pageUrl = "weixin_processRuntimeBackAddSign.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
					this.showSendWindow(pageUrl,"加签操作",800,400);
				}else{
					showSysTips(); 
					return false;
				}
			}
		}
		//打开副表单
		function openSubForm(stepformId){
			if($("#dataid").val()==0){
				alert('请保存主表单后编辑行项目子表');
				return;
			}
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}else{
				var actDefId = $("#actDefId").val();
				var prcDefId = $("#prcDefId").val();
				var excutionId = $("#excutionId").val();
				var instanceId = $("#instanceId").val();
				var taskId = $("#taskId").val();
				var url = "weixin_processFormPage.action?actDefId="+actDefId+"&prcDefId="+prcDefId+"&instanceId="+instanceId+"&excutionId="+excutionId+"&stepformId="+stepformId+"&taskId="+taskId;
				this.location = url;
			}
		}
		
		//弹出提示信息，timeLimit大于0时，限时提醒，提醒信息只保留timeLimit
		function showMsg(msg,timeLimit){
			if(timeLimit>0){
				alert(msg);
			}else{
				alert(msg);
			}
		} 
		//抢签任务，执行任务领用
		function claimTask(){
			  $.post('processManagement_claimTask.action',$("#iformMain").serialize(),function(data)
		            {
				    	if(data=='success'){
				    		alert("任务领取成功");
				    		try{
								window.parent.opener.reloadPage();
							}catch(err){}
				    		var actDefId = $("#actDefId").val();
							var instanceId = $("#instanceId").val();
							var taskId = $("#taskId").val();
							window.location.href = 'loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+instanceId+'&taskId='+taskId;
				    	}else{
				    		alert("任务领取失败(错误编号:ERROR-00001)")
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
			window.location =pageUrl;	
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
					id:'dg_dictionary',  
				    title:'流程监控',
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'100%', 
		            height:'100%'  
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
		//打开数据字典
		function openDictionary(uuid){
			var pageUrl = "weixin_dictionary_runtime_show.action?dictionaryUUID="+uuid;
			art.dialog.open(pageUrl,{
				id:'dg_dictionary',  
			    title:'数据字典',
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:'100%', 
	            height:'100%'  
			 });
			
			//this.showSendWindow(pageUrl,"数据字典",800,400);  
		}
		//打开子表数据字典  
		function openSubGridDictionary(id,subformkey,subformid){
		
			var pageUrl = "weixin_dictionary_runtime_show.action?dictionaryId="+id+"&subformkey="+subformkey+"&subformid="+subformid;
			art.dialog.open(pageUrl,{
				id:'dg_dictionary',  
			    title:'数据字典',
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:'100%', 
	            height:'100%'  
			 });
		}
		//加载系统提示
		function showSysTips(){
			$.post("sysmq_showtip.action",{type:"tip"},function(data){
		         if(data!=''){
		        		alert(data);
		         }
		    });
		}