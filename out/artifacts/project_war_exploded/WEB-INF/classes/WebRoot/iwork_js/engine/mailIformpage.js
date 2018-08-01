

var api,W;
try{
	api= frameElement.api;
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
			   initTree();
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
	document.getElementById('blockPage').style.display='';
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
	//	if(mainFormAlertFlag){
			//如果saveFlag为true，说明保存成功，提示5秒，否则说明保存有异常，
			showMsg(alertMsg,1);
			unLockPage();
			if(alertMsg=='保存成功'){
				 //集成weboffice
			 	try{ 
			 		SaveDocument();  
			 	}catch(e){} 
			 	
				var formId = $("#formid").val();
				var instanceId = $("#instanceId").val();
				var dataid = $("#dataid").val();
				var modelId = $("#modelId").val();
				var str = window.location.href;
				if(str.indexOf("createFormInstance")>0){ 
					window.location.href ="openFormPage.action?formid="+formId+"&instanceId="+instanceId+"&demId="+modelId; 
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
	//	}
		saveSubReportFlag=true;
		mainFormAlertFlag=true;
		return false;
	}else{
		saveSubReportFlag=true;
		mainFormAlertFlag=true;
		return false; 
	}
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
				lhgdialog.tips(msg,2);
			}else{ 
				$.dialog({ 
				    id: 'formPagemsg', 
				    title:'提示',  
				    icon: 'alert.gif',
				    width:'200px',
				    content:msg,  
				    button: [ 
				        { 
				            name:'确定', 
				            callback: function(){ 
				        		
				            }, 
				            focus: true 
				        }, 
				        { 
				            name: '查看详情', 
				            callback: function(){ 
				            	showSysTips(); 
				            } 
				        }
				    ] 
				});
			}
			  
		}
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
				return '页面有修改未保存，是否关闭页面?'; 
			}
		}
//设置页面菜单布局
function setMenuLayout(){
			$.dialog({
				id:"menuLayout",
				title: '菜单布局',
				content: 'url:menuLayoutEdit.action',
				pading: 0,
				lock: true,
				width: 400,
				height: 350
			});
		}
function showlog(){
	var instanceId = $("#instanceId").val();
	var modelId = $("#modelId").val();
	var formid = $("#formid").val(); 
	var pageUrl = "url:sysForm_showlog.action?formid="+formid+"&modelId="+modelId+"&instanceId="+instanceId;
	var dg =$.dialog({
			id:'updatelogDialog',
			cover:true,
			title:"更新日志",
			width:800,
			height:520,
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true, 
			lock: true,
			iconTitle:false,
			extendDrag:true, 
			autoSize:false,
			resize:false,
			content:pageUrl
		});
	dg.ShowDialog();
}

//打开数据字典
function openDictionary(uuid,subformkey,subformid){
	var pageUrl = "url:sys_dictionary_runtime_show.action?dictionaryUUID="+uuid+"&subformkey="+subformkey+"&subformid="+subformid;
	var dg =$.dialog({
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
			resize:false,
			content:pageUrl
		});
	dg.ShowDialog();
}
//打开子表数据字典
function openSubGridDictionary(id,subformkey,subformid){
	var instanceId = $("#instanceId").val();
	if(instanceId>0){
		var pageUrl = "url:sys_dictionary_runtime_show.action?dictionaryId="+id+"&subformkey="+subformkey+"&subformid="+subformid;
		var dg =$.dialog({
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
				resize:false,
				content:pageUrl
			});
		dg.ShowDialog();
	}else{
		
		showMsg("请先保存主表!");
	}
}
//导入excel
function openImpExcel(instanceId,subformid,subformkey,engineType){
	var modelId = $("#modelId").val();
	var pageUrl = "url:subform_openImpPage.action?modelId="+modelId+"&instanceId="+instanceId+"&subformid="+subformid+"&subformkey="+subformkey+"&engineType="+engineType;
	var dg =$.dialog({ 
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
		resize:false,
		content:pageUrl
	});
	dg.ShowDialog();
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

function previewPic(url){
	var pageUrl = "url:"+url;
	var modelId = $("#modelId").val();
	var dg =$.dialog({ 
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
		resize:false,
		content:pageUrl
	});
	dg.ShowDialog();
}

function pageClose(){ 
//	try{
//		api.close(); 
//	}catch(e){
//		window.close();
//	} 
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
function initTree(){
	var setting = {
				async: {
					enable: true, 
					url:"zqb_mail_mailJSON.action",  
					dataType:"json",
					autoParam:["id","nodeType"]
				},
				data: {
			   simpleData: {
				enable: true
			    }
		      },
				callback: {
					onClick:onClick
				} 
			};
		$.fn.zTree.init($("#mailTree"), setting);//加载导航树

}

//点击事件
function onClick(event, treeId, treeNode, clickFlag){
	var yxsjr=$("#sjr").val();
	if(treeNode.email!=undefined){
	     var strs= new Array(); 
	      strs=yxsjr.split(";"); //字符分割 
          for ( var i=0;i<strs.length ;i++ ) 
			{ 
			    var s=strs[i];
				if(s==treeNode.name+"<"+treeNode.email+">"){
				   return;
				} 
			} 
          
			$("#sjr").val(yxsjr+treeNode.name+"<"+treeNode.email+">;");
	}
}
function getGroupValue(id){
	var yxsjr = $("#sjr").val();
	var stra = new Array();
	stra = yxsjr.split(";");
	
	var url = "zqb_mail_mailgroupdata.action";
	$.post(url,{id:id}, function(data) {
		var strb = new Array();
		strb = data.split(";");
		var flag = false;
		
		for (var i = 0; i < strb.length; i++) {
			flag = false;
			var b = strb[i];
			for (var j = 0; j < stra.length; j++) {
				var a = stra[j];
				if(a==b){
					flag=true;
				}
			}
			if(!flag){
				$("#sjr").val($("#sjr").val() + b + ";");
				flag=false;
				stra = $("#sjr").val().split(";");
			}
		}
	});
}

function getGroupValue(id){
	var yxsjr = $("#sjr").val();
	var stra = new Array();
	stra = yxsjr.split(";");
	
	var url = "zqb_mail_mailgroupdata.action";
	$.post(url,{id:id}, function(data) {
		var strb = new Array();
		strb = data.split(";");
		var flag = false;
		
		for (var i = 0; i < strb.length; i++) {
			flag = false;
			var b = strb[i];
			for (var j = 0; j < stra.length; j++) {
				var a = stra[j];
				if(a==b){
					flag=true;
				}
			}
			if(!flag){
				$("#sjr").val($("#sjr").val() + b + ";");
				flag=false;
				stra = $("#sjr").val().split(";");
			}
		}
	});
}
//全部展开
function expandAll() {
	var zTree = $.fn.zTree.getZTreeObj("mailTree");
		zTree.expandAll(true);
}

	//全部折叠
function unExpandAll() {
	var zTree = $.fn.zTree.getZTreeObj("mailTree");
	zTree.expandAll(false);
}
