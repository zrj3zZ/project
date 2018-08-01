		 var subReportSaveFunction = new Array();//存放子表的保存方法名
		 var mainFormValidator;
		 var mainFormAlertFlag=true;
		 var lockPageFlag=true;
		 var saveSubReportFlag=true;
$().ready(function() { 
			function lockPage(){
				//先判断保存主表时是否需要覆盖层（当子表新增记录引发主表保存时不需要覆盖层）
				if(lockPageFlag){
					document.getElementById('blockPage').style.display='block';
				}else{
					document.getElementById('blockPage').style.display='';
				}
				lockPageFlag=true;
				return true;
			}
			function unLockPage(){
				document.getElementById('blockPage').style.display='';
				return true;
			}
			function successResponse(responseText, statusText, xhr, $form)  { 
				var alertMsg='';
				var arr=responseText.split(',');
				if(arr!=null&&arr.length==3){
					if(statusText=='success'&&arr[0]=='true'){
						//更新instanceId和dataid
						document.getElementById('instanceId').value=arr[1];
						document.getElementById('dataid').value=arr[2];
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
							}else{
								alertMsg=subFormSaveMsg;
							}
						}else{
							alertMsg='保存成功';
						}
					}else{
						alertMsg='主表保存失败';
					}
				}else{
					alertMsg='主表保存失败';
				}
				if(alertMsg!=null&&alertMsg.length>0){
					//判断是否提示保存异常的信息
					if(mainFormAlertFlag){
						alert(alertMsg);
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
				alert('主表保存失败');
			}
		    var submitOption = {
		    	beforeSubmit : lockPage,
		    	error :failResponse,
		    	complete : unLockPage,
		    	success:successResponse
		    };
			mainFormValidator = $("#iformMain").validate({
				debug:false,
				submitHandler:function(form){
					//无刷新的提交方式
					$('#iformMain').ajaxSubmit(submitOption);
					return false;
					/**刷新页面的提交方式
					form.action = "saveIform.action";
					form.submit();
					*/
				} 
			});
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
			logInitPageElementValue();
		});
function saveForm(){
			document.getElementById("submitbtn").click();	
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
window.onbeforeunload =function(){
			if(is_form_changed()){
				return '页面有修改未保存，是否关闭页面？';
			}
		}