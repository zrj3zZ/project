

var api,W;
try{
	api=  art.dialog.open.api;
	W = api.opener;	
}catch(e){}
var subReportSaveFunction = new Array();//存放子表的保存方法名
		 var mainFormValidator;
		 var mainFormAlertFlag=true;
		 var lockPageFlag=true;
		 var saveSubReportFlag=true; 
   $().ready(function() { 
			//自定义的日期时间校验方法
			jQuery.validator.methods.dateTime = function(value, element, param) {
	            var dateTimeReg = /^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$/;
				return dateTimeReg.test(value);
	        };
	        //自定义的时间校验方法
	        jQuery.validator.methods.time = function(value, element, param) {
	            var timeReg = /^(\d{1,2})(:)?(\d{1,2})\2(\d{1,2})$/;
				return timeReg.test(value);
	        };
	        var submitOption = {
	        		beforeSubmit : lockPage,
	        		error :failResponse,
	        		complete : unLockPage,
	        		success:successResponse
	        	};
	        	mainFormValidator = $("#iformMain").validate({ 
	        		debug:false,
	        		errorPlacement: function (error, element) {
	        	           error.appendTo(element.parent());    //将错误信息添加当前元素的父结点后面
	        		   },
	        		submitHandler:function(form){
	        			try{
	        				if(!savebeforeEvent()){
	        					return;
	        				}
	        			}catch(e){}
	        			//无刷新的提交方式
	        			$('#iformMain').ajaxSubmit(submitOption);
	        			return false;
	        			/**刷新页面的提交方式
	        			form.action = "saveIform.action"; 
	        			form.submit();
	        			*/
	        		} 
	        	});
			logInitPageElementValue();
			getUrlAddress();
		});
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
	try{
	document.getElementById('blockPage').style.display='';
	}catch(e){}
	return true;
}
function successResponse(responseText, statusText, xhr, $form)  { 
	var alertMsg='';
	var saveFlag=true;
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
						try{
							var msg = saveFun();
							if(msg!=null&&msg.length>0){
								subFormSaveMsg+=msg;
							}
						}catch(e){}
					}
				}
				
				if(subFormSaveMsg.length<1){
					alertMsg='保存成功';
					
				}else{
					alertMsg=subFormSaveMsg;
					saveFlag=false;
				}
			}else{
				alertMsg='保存成功';
			}
		}else{
			alertMsg='主表保存失败';
			saveFlag=false;
		}
	}else{
		alertMsg='主表保存失败';
		saveFlag=false;
	}
	if(alertMsg!=null&&alertMsg.length>0){
		//判断是否提示保存异常的信息
//		if(mainFormAlertFlag){
			//如果saveFlag为true，说明保存成功，提示5秒，否则说明保存有异常，
			showMsg(alertMsg,1);
			unLockPage();
			if(alertMsg=='保存成功'){
				 //集成weboffice
			 	try{ 
			 		SaveDocument();  
			 	}catch(e){} 
			 	//保存富文本
			 	try{ 
			 		saveHtmlEditor();  
			 	}catch(e){} 
			 	
				var formId = $("#formid").val();
				var instanceId = $("#instanceId").val();
				var dataid = $("#dataid").val();
				var modelId = $("#modelId").val();
				var str = window.location.href;
				if(str.indexOf("createFormInstance")>0){
					var closeapi=false;
					if(api!=undefined){
						var object = api.get('iformPjrz',1);
						if(object!=null && object!=undefined){
							closeapi=true;
						}
						/*var shObject = api.get('projectItem',1);
						if(shObject!=null && shObject!=undefined){
							closeapi=true;
						}*/
					}
					if(closeapi){
						window.location.href ="openFormPage.action?formid="+formId+"&instanceId="+instanceId+"&demId="+modelId+"&closeapi=1"; 
					}else if (str.indexOf("isDwPj")>-1){
						window.location.href ="openFormPage.action?formid="+formId+"&instanceId="+instanceId+"&demId="+modelId+"&isDwPj=1"; 
					}else{
						window.location.href ="openFormPage.action?formid="+formId+"&instanceId="+instanceId+"&demId="+modelId; 
					}
				}
				//调用当前页面保存后回调js脚本，可在表单的js脚本中继承此方法
				try{
					saveAfterEvent();
				 }catch(e){} 
				 //刷新父页面列表
				try{ 
				window.opener.reloadGrid()
				}catch(e){} 
			}else{
				showMsg(alertMsg);
			}
//		}
		saveSubReportFlag=true;
		mainFormAlertFlag=true;
		return false;
	}else{
		var str = window.location.href;
		if(str.indexOf("createFormInstance")>0){ 
			window.location.href ="openFormPage.action?formid="+formId+"&instanceId="+instanceId+"&demId="+modelId; 
		}
		saveSubReportFlag=true;
		mainFormAlertFlag=true;
		return false; 
	}
}

function saveHtmlEditor(){
	//保存所有的富文本
	$("[form-type='htmlEditor']").each(function(){
		var name = $(this).attr("name");
		eval("editor"+name).sync();
	});
}

function failResponse(){
	//alert('主表保存失败');
	showMsg('主表保存失败');
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
//弹出提示信息，timeLimit大于0时，限时提醒，提醒信息只保留timeLimit
function showMsg(msg,timeLimit){
			if(timeLimit>0){
				art.dialog.tips(msg,2);
			}else{ 
				showSysTips(); 
			}
			  
		}
function saveForm(){
	//东莞项目关闭调整
	var f=$("#dggbxz").val();
	if(f=='1'){
		dgxmgb();
	}
	
	for (var int = 0; int < subReportSaveFunction.length; int++) {
		document.getElementById("saveSubReportData"+subReportSaveFunction[int]).click();
	}
	var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
	return false;
	}
	//保存富文本
 	try{ 
 		saveHtmlEditor();  
 	}catch(e){} 
	document.getElementById("submitbtn").click();	
} 
//东莞项目关闭调整
function dgxmgb(){
	var dgxmlx=$("#dgxmlx").val();
	var dginsid=$("#dginsid").val();
	$.post("dg_zqb_project_close.action",{instanceId:dginsid,xmlx:dgxmlx},function(data){ 
		if(data!='success'){
			alert("项目关闭失败，请重试");
		}
	});
}
//判断页面元素是否修改
function is_form_changed(){
			return false; 
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
			
//			return is_changed;
		}
window.onbeforeunload =function(){
			/*if(is_form_changed()){
				return '页面有修改未保存，是否关闭页面?'; 
			}*/
		}
//设置页面菜单布局
function setMenuLayout(){
	var pageUrl = "menuLayoutEdit.action";
	art.dialog.open(pageUrl,{
		 id:'updatelogDialog',
		 title: '菜单布局',
		 lock:true,
		 background: '#999', // 背景色
		 opacity: 0.87,	// 透明度
		 width: 400,
		 height: 350
	});
}
function showlog(){
	var instanceId = $("#instanceId").val();
	var modelId = $("#modelId").val();
	var formid = $("#formid").val(); 
	var pageUrl = "sysForm_showlog.action?formid="+formid+"&modelId="+modelId+"&instanceId="+instanceId;
	 art.dialog.open(pageUrl,{
		 id:'updatelogDialog',
	    	title:"更新日志",
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:'90%',
		    height:'90%'
		 });
}

//打开数据字典
function openDictionary(uuid,subformkey,subformid){
	var pageUrl = "sys_dictionary_runtime_show.action?dictionaryUUID="+uuid+"&subformkey="+subformkey+"&subformid="+subformid;
	art.dialog.open(pageUrl,{
			id:'DictionaryDialog',
			title:"数据选择",
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:'90%',
		    height:'90%'
		 });
}
//打开子表数据字典
function openSubGridDictionary(id,subformkey,subformid){
	var instanceId = $("#instanceId").val();
	if(instanceId>0){
		var pageUrl = "sys_dictionary_runtime_show.action?dictionaryId="+id+"&subformkey="+subformkey+"&subformid="+subformid;
		art.dialog.open(pageUrl,{
			id:'DictionaryDialog',
			title:"数据选择",
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:'90%',
		    height:'90%'
		 });
	}else{
		showMsg("请先保存主表!",2);
	}
}
//导入excel
function openImpExcel(instanceId,subformid,subformkey,engineType){
	var modelId = $("#modelId").val();
	var pageUrl = "subform_openImpPage.action?modelId="+modelId+"&instanceId="+instanceId+"&subformid="+subformid+"&subformkey="+subformkey+"&engineType="+engineType;
	art.dialog.open(pageUrl,{
		id:'DictionaryDialog',
		title:"数据选择",
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:'90%',
	    height:'90%'
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

function previewPic(pageUrl){
	var modelId = $("#modelId").val();
	art.dialog.open(pageUrl,{
		id:'previewPic',
		title:"图片预览",
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:'90%',
	    height:'90%'
	 });
}

function pageClose(){ 
	window.onbeforeunload = null;//禁用表单关闭浏览器询问
	if(typeof(api) =="undefined"){
		window.close();
	}else{ 
		api.close(); 
	}
}

function changeZl() {

	$("#JDZL").val("");
	$("#zlinfo").html("");
	var obj = document.getElementById('GROUPID');
	// 定位id
	var index = obj.selectedIndex;
	// 选中的文本
	var text = obj.options[index].text;
	// 值
	var value = obj.options[index].value;

	var pageUrl = "getDataformjson.action?demId=51&JDMC_152="+encodeURI(text);

	$.post(pageUrl, {
		"page.pageSize" : 100
	}, function(datas) {
		dataJson = eval("(" + datas + ")");
		var dataJson1 = dataJson['dataRows'];
		var j = 1;
		var zlinfo = "";
		var jdzl = "";
		for (var i = 0; i < dataJson["total"]; i++) {
			zlinfo = j+". "+dataJson1[i]['CONTENT']+"  ";
			jdzl = jdzl + zlinfo;
			j=j+1;
		}
		
		 $("#JDZL").val(jdzl);

	});
	
}

function getUrlAddress(){
	var str = window.location.href;
	if(str.indexOf("closeapi")>0){
		if(api!=undefined){
			var object = api.get('iformPjrz',1);
			if(object!=null && object!=undefined){
				setTimeout('closeapi();',500);
			}
			object = api.get('projectItem',1);
			if(object!=null && object!=undefined){
				setTimeout('closeapi();',500);
			}
			
		}
	}
}

function closeapi(){
	if(api!=undefined){
		var object = api.get('iformPjrz',1);
		if(object!=null && object!=undefined){
			object.close();
		}
		object = api.get('projectItem',1);
		if(object!=null && object!=undefined){
			object.close();
		}
	}
}
